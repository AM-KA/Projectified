package com.princeakash.projectified.candidate.myApplications.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.princeakash.projectified.Event
import com.princeakash.projectified.MyApplication
import com.princeakash.projectified.MyApplication.Companion.handleError
import com.princeakash.projectified.candidate.CandidateRepository
import com.princeakash.projectified.candidate.addApplication.model.BodyAddApplication
import com.princeakash.projectified.candidate.addApplication.model.ResponseAddApplication
import com.princeakash.projectified.candidate.addApplication.model.ResponseGetOfferById
import com.princeakash.projectified.candidate.addApplication.model.ResponseGetOffersByDomain
import com.princeakash.projectified.candidate.myApplications.model.*
import com.princeakash.projectified.recruiter.myOffers.viewmodel.RecruiterViewModel
import com.princeakash.projectified.user.ProfileRepository
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class CandidateViewModel(val app: Application) : AndroidViewModel(app) {

    //CandidaterRepository instance, guaranteed to be singular because of being
    val profileRepository: ProfileRepository = (app as MyApplication).profileRepository
    val candidateRepository: CandidateRepository = (app as MyApplication).candidateRepository

    //MutableLiveData variables of responses for all kinds of requests h
    //picked up from instance of MyApplication handled by RecruiterViewModel
    //which can be put to observation in Activities/Fragments
    private var responseUpdateApplication: MutableLiveData<Event<ResponseUpdateApplication>> = MutableLiveData()
    private var responseDeleteApplication: MutableLiveData<Event<ResponseDeleteApplication>> = MutableLiveData()
    private var responseGetApplicationByCandidate: MutableLiveData<ResponseGetApplicationsByCandidate> = MutableLiveData()
    private var responseGetApplicationDetailByIdCandidate: MutableLiveData<ResponseGetApplicationDetailByIdCandidate> = MutableLiveData()
    private var responseGetOffersByDomain: MutableLiveData<ResponseGetOffersByDomain> = MutableLiveData()
    private var responseAddApplication: MutableLiveData<Event<ResponseAddApplication>> = MutableLiveData()
    private var responseGetOfferById: MutableLiveData<ResponseGetOfferById> = MutableLiveData()
    private var safeToVisitDomainOffers: MutableLiveData<Event<Boolean>> = MutableLiveData()
    private var safeToVisitOfferDetails: MutableLiveData<Event<Boolean>> = MutableLiveData()
    private var safeToVisitApplicationDetails:MutableLiveData<Event<Boolean>> = MutableLiveData()
    private var safeToVisitApplicationList: MutableLiveData<Event<Boolean>> = MutableLiveData()
    private var currentDomainName: MutableLiveData<String> = MutableLiveData()
    private var currentOfferId: MutableLiveData<String> = MutableLiveData()
    private var currentApplicationId: MutableLiveData<String> = MutableLiveData()
    private var initialInstruction: MutableLiveData<Event<Boolean>> = MutableLiveData()
    private var errorString: MutableLiveData<Event<String>> = MutableLiveData()

    //MutableLiveData variables of responses for all kinds of requests h
    //picked up from instance of MyApplication handled by RecruiterViewModel
    //which can be put to observation in Activities/Fragments
    fun responseUpdateApplication(): LiveData<Event<ResponseUpdateApplication>> = responseUpdateApplication
    fun responseDeleteApplication(): LiveData<Event<ResponseDeleteApplication>> = responseDeleteApplication
    fun responseGetApplicationByCandidate(): LiveData<ResponseGetApplicationsByCandidate> = responseGetApplicationByCandidate
    fun responseGetApplicationDetailByIdCandidate(): LiveData<ResponseGetApplicationDetailByIdCandidate> = responseGetApplicationDetailByIdCandidate
    fun responseGetOffersByDomain(): LiveData<ResponseGetOffersByDomain> = responseGetOffersByDomain
    fun responseAddApplication(): LiveData<Event<ResponseAddApplication>> = responseAddApplication
    fun responseGetOfferById(): LiveData<ResponseGetOfferById> = responseGetOfferById
    fun safeToVisitDomainOffers(): LiveData<Event<Boolean>> = safeToVisitDomainOffers
    fun safeToVisitOfferDetails(): LiveData<Event<Boolean>> = safeToVisitOfferDetails
    fun safeToVisitApplicationDetails(): LiveData<Event<Boolean>> = safeToVisitApplicationDetails
    fun safeToVisitApplicationList(): LiveData<Event<Boolean>> = safeToVisitApplicationList
    fun currentDomainName(): LiveData<String> = currentDomainName
    fun errorString(): LiveData<Event<String>> = errorString

    fun getApplicationsByCandidate() {
        profileRepository.getUserId()
        val token: String = profileRepository.getToken()
        val applicantID: String = profileRepository.getUserId()
        if (token.equals("")) {
            errorString.postValue(Event(RecruiterViewModel.INVALID_TOKEN))
            return
        }
        if (applicantID.equals("")) {
            errorString.postValue(Event("Invalid User ID. Please log in again."))
            return
        }
        viewModelScope.launch {
            try {
                responseGetApplicationByCandidate.postValue(candidateRepository.getApplicationByCandidate("Bearer $token", applicantID))
                responseDeleteApplication.value?.let{
                    safeToVisitApplicationList.postValue(Event(true))
                }
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }

    fun getApplicationDetailByIdCandidate(applicationID: String) {
        val token = profileRepository.getToken()
        if (token.equals("")) {
            errorString.postValue(Event(RecruiterViewModel.INVALID_TOKEN))
            return
        }
        viewModelScope.launch {
            try {
                currentApplicationId.postValue(applicationID)
                responseGetApplicationDetailByIdCandidate.postValue(candidateRepository.getApplicationDetailByIdCandidate("Bearer $token", applicationID))
                safeToVisitApplicationDetails.postValue(Event(true))
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }


    fun updateApplication(resume:String, previousWork:String) {
        val token = profileRepository.getToken()
        if (token.equals("")) {
            errorString.postValue(Event(RecruiterViewModel.INVALID_TOKEN))
            return
        }
        val applicationID = currentApplicationId.value!!
        val bodyUpdateApplication = BodyUpdateApplication(resume, previousWork)
        viewModelScope.launch {
            try {
                responseUpdateApplication.postValue(Event(candidateRepository.updateApplication("Bearer $token", applicationID, bodyUpdateApplication)))
                getApplicationDetailByIdCandidate(currentApplicationId.value!!)
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }


    fun deleteApplication() {
        val token = profileRepository.getToken()
        if (token.equals("")) {
            errorString.postValue(Event(RecruiterViewModel.INVALID_TOKEN))
            return
        }
        val applicationID = currentApplicationId.value!!
        viewModelScope.launch {
            try {
                responseDeleteApplication.postValue(Event(candidateRepository.deleteApplication("Bearer $token", applicationID)))
                getApplicationsByCandidate()
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }

    fun issueInitialInstructions(){
        if(initialInstruction.value == null) {
            initialInstruction.postValue(Event(true))
            getApplicationsByCandidate()
        }
    }

    fun getOffersByDomain(domainName: String) {
        val token = profileRepository.getToken()
        if (token == "") {
            errorString.postValue(Event(RecruiterViewModel.INVALID_TOKEN))
            return
        }
        viewModelScope.launch {
            try {
                currentDomainName.postValue(domainName)
                responseGetOffersByDomain.postValue(candidateRepository.getOffersByDomain("Bearer $token", domainName))
                safeToVisitDomainOffers.postValue(Event(true))
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }

    fun addApplication(Resume: String, PreviousWork: String) {
        val token = profileRepository.getToken()
        val applicantID: String = profileRepository.getUserId()
        if (token == "") {
            errorString.postValue(Event(RecruiterViewModel.INVALID_TOKEN))
            return
        }
        val offerId = currentOfferId.value!!
        viewModelScope.launch {
            try {

                val bodyAddApplication = BodyAddApplication(Date(), Resume, PreviousWork, applicantID, offerId)
                responseAddApplication.postValue(Event(candidateRepository.addApplication("Bearer $token", bodyAddApplication)))
                getApplicationsByCandidate()
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }

    fun getoffersById(offerId: String) {
        val token = profileRepository.getToken()
        if (token == "") {
            errorString.postValue(Event(RecruiterViewModel.INVALID_TOKEN))
            return
        }
        viewModelScope.launch {
            try {
                currentOfferId.postValue(offerId)
                responseGetOfferById.postValue(candidateRepository.getOfferById("Bearer $token", offerId))
                safeToVisitOfferDetails.postValue(Event(true))
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }

    fun getLocalProfile() = profileRepository.getLocalProfile()
}