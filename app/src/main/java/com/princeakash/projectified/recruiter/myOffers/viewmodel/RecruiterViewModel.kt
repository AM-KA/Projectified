package com.princeakash.projectified.recruiter.myOffers.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.princeakash.projectified.Event
import com.princeakash.projectified.MyApplication
import com.princeakash.projectified.MyApplication.Companion.handleError
import com.princeakash.projectified.recruiter.*
import com.princeakash.projectified.recruiter.addOffer.model.BodyAddOffer
import com.princeakash.projectified.recruiter.addOffer.model.ResponseAddOffer
import com.princeakash.projectified.recruiter.myOffers.model.*
import com.princeakash.projectified.user.ProfileRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class RecruiterViewModel(val app: Application) : AndroidViewModel(app) {

    //RecruiterRepository instance, guaranteed to be singular because of being
    val recruiterRepository: RecruiterRepository = (app as MyApplication).recruiterRepository
    val profileRepository: ProfileRepository = (app as MyApplication).profileRepository

    //MutableLiveData variables of responses for all kinds of requests
    //which can be put to observation in Activities/Fragments
    private val responseAddOffer : MutableLiveData<Event<ResponseAddOffer>> = MutableLiveData()
    private var responseGetOffersByRecruiter: MutableLiveData<ResponseGetOffersByRecruiter> = MutableLiveData()
    private var responseGetOfferByIdRecruiter: MutableLiveData<ResponseGetOfferByIdRecruiter> = MutableLiveData()
    private var responseGetOfferApplicants: MutableLiveData<ResponseGetOfferApplicants> = MutableLiveData()
    private var responseUpdateOffer: MutableLiveData<Event<ResponseUpdateOffer>> = MutableLiveData()
    private var responseToggleVisibility: MutableLiveData<Event<ResponseToggleVisibility>> = MutableLiveData()
    private var responseDeleteOffer: MutableLiveData<Event<ResponseDeleteOffer>> = MutableLiveData()
    private var responseGetApplicationById : MutableLiveData<ResponseGetApplicationByIdRecruiter> = MutableLiveData()
    private var responseMarkAsSeen: MutableLiveData<Event<ResponseMarkAsSeen>> = MutableLiveData()
    private var responseMarkAsSelected: MutableLiveData<Event<ResponseMarkAsSelected>> = MutableLiveData()
    private var safeToVisitOfferList: MutableLiveData<Event<Boolean>> = MutableLiveData()
    private var safeToVisitOfferDetails: MutableLiveData<Event<Boolean>> = MutableLiveData()
    private var safeToVisitCandidates: MutableLiveData<Event<Boolean>> = MutableLiveData()
    private var safeToVisitCandidateDetails: MutableLiveData<Event<Boolean>> = MutableLiveData()
    private var currentOfferId: MutableLiveData<String> = MutableLiveData()
    private var currentApplicationId: MutableLiveData<String> = MutableLiveData()
    private val currentDomainName: MutableLiveData<String> = MutableLiveData()
    private var initialInstruction: MutableLiveData<Event<Boolean>> = MutableLiveData()
    private var errorString: MutableLiveData<Event<String>> = MutableLiveData()

    //Functions to expose all MutableLiveData instances as LiveData instances
    fun responseAddOffer() : LiveData<Event<ResponseAddOffer>> = responseAddOffer
    fun responseGetOffersByRecruiter() : LiveData<ResponseGetOffersByRecruiter> = responseGetOffersByRecruiter
    fun responseGetOfferByIdRecruiter(): LiveData<ResponseGetOfferByIdRecruiter> = responseGetOfferByIdRecruiter
    fun responseGetOfferApplicants() : LiveData<ResponseGetOfferApplicants> = responseGetOfferApplicants
    fun responseUpdateOffer(): LiveData<Event<ResponseUpdateOffer>> = responseUpdateOffer
    fun responseToggleVisibility(): LiveData<Event<ResponseToggleVisibility>> = responseToggleVisibility
    fun responseDeleteOffer(): LiveData<Event<ResponseDeleteOffer>> = responseDeleteOffer
    fun responseGetApplicationById() : LiveData<ResponseGetApplicationByIdRecruiter> = responseGetApplicationById
    fun responseMarkAsSeen() : LiveData<Event<ResponseMarkAsSeen>> = responseMarkAsSeen
    fun responseMarkAsSelected(): LiveData<Event<ResponseMarkAsSelected>> = responseMarkAsSelected
    fun safeToVisitOfferList(): LiveData<Event<Boolean>> = safeToVisitOfferList
    fun safeToVisitOfferDetails(): LiveData<Event<Boolean>> = safeToVisitOfferDetails
    fun safeToVisitCandidates(): LiveData<Event<Boolean>> = safeToVisitCandidates
    fun safeToVisitCandidateDetails(): LiveData<Event<Boolean>> = safeToVisitCandidateDetails
    fun errorString(): LiveData<Event<String>> = errorString

    fun getOffersByRecruiter(){
        val token:String = profileRepository.getToken()
        val recruiterID: String = profileRepository.getUserId()
        if(token.equals("")) {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        if(recruiterID.equals("")){
            errorString.postValue(Event("Invalid User ID. Please log in again."))
            return
        }
        viewModelScope.launch {
            try {
                responseGetOffersByRecruiter.postValue(recruiterRepository.getOffersByRecruiter("Bearer $token", recruiterID))
                responseDeleteOffer.value?.let{
                    safeToVisitOfferList.postValue(Event(true))
                }
            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    fun getOfferByIdRecruiter(offerID:String){
        val token = profileRepository.getToken()
        if(token.equals("")) {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        viewModelScope.launch {
            try {
                currentOfferId.postValue(offerID)
                responseGetOfferByIdRecruiter.postValue(recruiterRepository.getOfferByIdRecruiter("Bearer $token", offerID))
                safeToVisitOfferDetails.postValue(Event(true))
            } catch(e: Exception){
                handleError(e, errorString)
                safeToVisitOfferDetails.postValue(Event(false))
            }
        }
    }

    fun getOfferApplicants(){
        val token = profileRepository.getToken()
        if(token.equals("")) {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        val offerID = currentOfferId.value!!
        viewModelScope.launch {
            try {
                responseGetOfferApplicants.postValue(recruiterRepository.getOfferApplicants("Bearer $token", offerID))
                safeToVisitCandidates.postValue(Event(true))
            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    fun updateOffer(offerName: String, requirements: String, skills:String, expectation: String){
        val token = profileRepository.getToken()
        if(token.equals("")) {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        val bodyUpdateOffer = BodyUpdateOffer(offerName, requirements, skills, expectation)
        val offerID = currentOfferId.value!!
        viewModelScope.launch {
            try {
                responseUpdateOffer.postValue(Event(recruiterRepository.updateOffer("Bearer $token", offerID, bodyUpdateOffer)))
                getOfferByIdRecruiter(currentOfferId.value!!)
            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    fun toggleVisibility(isChecked: Boolean){
        val token = profileRepository.getToken()
        if(token.equals("")) {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        val bodyToggleVisibility = BodyToggleVisibility(isChecked)
        val offerID = currentOfferId.value!!
        viewModelScope.launch {
            try {
                responseToggleVisibility.postValue(Event(recruiterRepository.toggleVisibility("Bearer $token", offerID, bodyToggleVisibility)))
            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    fun deleteOffer(){
        val token = profileRepository.getToken()
        if(token.equals("")) {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        val offerID = currentOfferId.value!!
        viewModelScope.launch {
            try {
                responseDeleteOffer.postValue(Event(recruiterRepository.deleteOffer("Bearer $token", offerID)))
                getOffersByRecruiter()
            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    fun getApplicationById(applicationID:String){
        val token = profileRepository.getToken()
        if(token.equals("")) {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        viewModelScope.launch {
            try {
                currentApplicationId.postValue(applicationID)
                responseGetApplicationById.postValue(recruiterRepository.getApplicationById("Bearer $token", applicationID))
                safeToVisitCandidateDetails.postValue(Event(true))
            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    fun markSeen(is_Seen: Boolean){
        val token = profileRepository.getToken()
        if(token.equals("")) {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        val bodyMarkAsSeen = BodyMarkAsSeen(is_Seen)
        val applicationID = currentApplicationId.value!!
        viewModelScope.launch {
            try {
                val response = recruiterRepository.markSeen("Bearer $token", applicationID, bodyMarkAsSeen)
                responseMarkAsSeen.postValue(Event(response))
            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    fun markSelected(is_Selected: Boolean){
        val token = profileRepository.getToken()
        if(token.equals("")) {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        val bodyMarkAsSelected = BodyMarkAsSelected(is_Selected)
        val applicationID = currentApplicationId.value!!
        viewModelScope.launch {
            try {
                responseMarkAsSelected.postValue(Event(recruiterRepository.markSelected("Bearer $token", applicationID, bodyMarkAsSelected)))
            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    fun markSeen(applicationID: String, is_Seen: Boolean){
        val token = profileRepository.getToken()
        if(token.equals("")) {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        val bodyMarkAsSeen = BodyMarkAsSeen(is_Seen)
        viewModelScope.launch {
            try {
                val response = recruiterRepository.markSeen("Bearer $token", applicationID, bodyMarkAsSeen)
                responseMarkAsSeen.postValue(Event(response))
                (responseGetOfferApplicants.value!!.applicants as ArrayList<ApplicantCardModel>)?.let {
                    for (listItem in it) {
                        if (listItem.application_id.equals(currentApplicationId.value!!)) {
                            listItem.is_Seen = true
                            refreshApplicants(it)
                            break
                        }
                    }
                }
            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    fun markSelected(applicationID: String, is_Selected: Boolean){
        val token = profileRepository.getToken()
        if(token.equals("")) {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        val bodyMarkAsSelected = BodyMarkAsSelected(is_Selected)
        viewModelScope.launch {
            try {
                val response = recruiterRepository.markSelected("Bearer $token", applicationID, bodyMarkAsSelected)
                responseMarkAsSelected.postValue(Event(response))
                (responseGetOfferApplicants.value!!.applicants as ArrayList<ApplicantCardModel>)?.let {
                    for (listItem in it) {
                        if (listItem.application_id.equals(currentApplicationId.value!!)) {
                            listItem.is_Selected = true
                            refreshApplicants(it)
                            break
                        }
                    }
                }
            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    fun refreshApplicants(applicantList: ArrayList<ApplicantCardModel>){
        val res = responseGetOfferApplicants.value!!
        res.applicants = applicantList
        responseGetOfferApplicants.postValue(res)
    }

    fun issueInitialInstructions(){
        if(initialInstruction.value == null) {
            initialInstruction.postValue(Event(true))
            getOffersByRecruiter()
        }
    }

    fun addOffer(offerName:String, requirements: String, skills: String, expectation: String){
        val token = profileRepository.getToken()
        val recruiterID: String = profileRepository.getUserId()
        if(token == "") {
            errorString.postValue(Event(RecruiterViewModel.INVALID_TOKEN))
            return
        }
        if(recruiterID.equals("")){
            errorString.postValue(Event("Invalid User ID. Please log in again."))
            return
        }
        val domainName = currentDomainName.value!!
        viewModelScope.launch {
            try {
                val bodyAddOffer = BodyAddOffer(offerName, domainName, requirements, skills, expectation, recruiterID)
                responseAddOffer.postValue(Event(recruiterRepository.addOffer(bodyAddOffer, "Bearer $token")))
                getOffersByRecruiter()
            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    fun setDomain(domainName: String){
        currentDomainName.postValue(domainName)
    }

    fun getLocalProfile() = profileRepository.getLocalProfile()

    companion object {
        const val INVALID_TOKEN = "Invalid Token. Please log in again.";
    }
}