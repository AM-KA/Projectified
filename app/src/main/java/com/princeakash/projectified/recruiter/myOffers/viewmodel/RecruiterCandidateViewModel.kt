package com.princeakash.projectified.recruiter.myOffers.viewmodel

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
import com.princeakash.projectified.recruiter.*
import com.princeakash.projectified.recruiter.addOffer.model.BodyAddOffer
import com.princeakash.projectified.recruiter.addOffer.model.ResponseAddOffer
import com.princeakash.projectified.recruiter.myOffers.model.*
import com.princeakash.projectified.user.ProfileRepository
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class RecruiterCandidateViewModel(val app: Application) : AndroidViewModel(app) {

    //RecruiterRepository instance, guaranteed to be singular because of being
    val recruiterRepository: RecruiterRepository = (app as MyApplication).recruiterRepository
    val profileRepository: ProfileRepository = (app as MyApplication).profileRepository
    val candidateRepository: CandidateRepository = (app as MyApplication).candidateRepository

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
                safeToVisitOfferList.postValue(Event(true))
                getApplicationsByCandidate()
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
                refreshOffer()
            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    fun refreshOffer(){
        val token = profileRepository.getToken()
        if(token.equals("")) {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        viewModelScope.launch {
            try {
                val offerID = currentOfferId.value!!
                responseGetOfferByIdRecruiter.postValue(recruiterRepository.getOfferByIdRecruiter("Bearer $token", offerID))
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
                currentOfferId.postValue(OFFERS_REQUESTED_ONCE.toString())
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

    fun markSeen(applicationID: String?, is_Seen: Boolean){
        val token = profileRepository.getToken()
        if(token.equals("")) {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        var app_id = applicationID
        if(app_id==null){
            app_id= currentApplicationId.value!!
        }
        val bodyMarkAsSeen = BodyMarkAsSeen(is_Seen)
        viewModelScope.launch {
            try {
                val response = recruiterRepository.markSeen("Bearer $token", app_id!!, bodyMarkAsSeen)
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

    fun markSelected(applicationID: String?, is_Selected: Boolean){
        val token = profileRepository.getToken()
        if(token.equals("")) {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }

        var app_id = applicationID
        if(app_id==null){
            app_id = currentApplicationId.value!!
        }

        val bodyMarkAsSelected = BodyMarkAsSelected(is_Selected)
        viewModelScope.launch {
            try {
                val response = recruiterRepository.markSelected("Bearer $token", app_id!!, bodyMarkAsSelected)
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
            errorString.postValue(Event(RecruiterCandidateViewModel.INVALID_TOKEN))
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
                getOffersByDomain(currentDomainName.value!!)
            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    fun setDomain(domainName: String){
        currentDomainName.postValue(domainName)
    }

    private var responseUpdateApplication: MutableLiveData<Event<ResponseUpdateApplication>> = MutableLiveData()
    private var responseDeleteApplication: MutableLiveData<Event<ResponseDeleteApplication>> = MutableLiveData()
    private var responseGetApplicationByCandidate: MutableLiveData<ResponseGetApplicationsByCandidate> = MutableLiveData()
    private var responseGetApplicationDetailByIdCandidate: MutableLiveData<ResponseGetApplicationDetailByIdCandidate> = MutableLiveData()
    private var responseGetOffersByDomain: MutableLiveData<ResponseGetOffersByDomain> = MutableLiveData()
    private var responseAddApplication: MutableLiveData<Event<ResponseAddApplication>> = MutableLiveData()
    private var responseGetOfferById: MutableLiveData<ResponseGetOfferById> = MutableLiveData()
    private var safeToVisitDomainOffers: MutableLiveData<Event<Boolean>> = MutableLiveData()
    private var safeToVisitDomainOfferDetails: MutableLiveData<Event<Boolean>> = MutableLiveData()
    private var safeToVisitApplicationDetails:MutableLiveData<Event<Boolean>> = MutableLiveData()
    private var safeToVisitApplicationList: MutableLiveData<Event<Boolean>> = MutableLiveData()

    fun responseUpdateApplication(): LiveData<Event<ResponseUpdateApplication>> = responseUpdateApplication
    fun responseDeleteApplication(): LiveData<Event<ResponseDeleteApplication>> = responseDeleteApplication
    fun responseGetApplicationByCandidate(): LiveData<ResponseGetApplicationsByCandidate> = responseGetApplicationByCandidate
    fun responseGetApplicationDetailByIdCandidate(): LiveData<ResponseGetApplicationDetailByIdCandidate> = responseGetApplicationDetailByIdCandidate
    fun responseGetOffersByDomain(): LiveData<ResponseGetOffersByDomain> = responseGetOffersByDomain
    fun responseAddApplication(): LiveData<Event<ResponseAddApplication>> = responseAddApplication
    fun responseGetOfferById(): LiveData<ResponseGetOfferById> = responseGetOfferById
    fun safeToVisitDomainOffers(): LiveData<Event<Boolean>> = safeToVisitDomainOffers
    fun safeToVisitDomainOfferDetails(): LiveData<Event<Boolean>> = safeToVisitDomainOfferDetails
    fun safeToVisitApplicationDetails(): LiveData<Event<Boolean>> = safeToVisitApplicationDetails
    fun safeToVisitApplicationList(): LiveData<Event<Boolean>> = safeToVisitApplicationList
    fun currentDomainName(): LiveData<String> = currentDomainName

    fun getApplicationsByCandidate() {
        val token: String = profileRepository.getToken()
        val applicantID: String = profileRepository.getUserId()
        if (token.equals("")) {
            errorString.postValue(Event(RecruiterCandidateViewModel.INVALID_TOKEN))
            return
        }
        if (applicantID.equals("")) {
            errorString.postValue(Event("Invalid User ID. Please log in again."))
            return
        }
        viewModelScope.launch {
            try {
                responseGetApplicationByCandidate.postValue(candidateRepository.getApplicationByCandidate("Bearer $token", applicantID))
                safeToVisitApplicationList.postValue(Event(true))
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }

    fun getApplicationDetailByIdCandidate(applicationID: String) {
        val token = profileRepository.getToken()
        if (token.equals("")) {
            errorString.postValue(Event(RecruiterCandidateViewModel.INVALID_TOKEN))
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
            errorString.postValue(Event(RecruiterCandidateViewModel.INVALID_TOKEN))
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
            errorString.postValue(Event(RecruiterCandidateViewModel.INVALID_TOKEN))
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

    fun getOffersByDomain(domainName: String) {
        val token = profileRepository.getToken()
        if (token == "") {
            errorString.postValue(Event(RecruiterCandidateViewModel.INVALID_TOKEN))
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
            errorString.postValue(Event(RecruiterCandidateViewModel.INVALID_TOKEN))
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

    fun getOfferById(offerId: String) {
        val token = profileRepository.getToken()
        if (token == "") {
            errorString.postValue(Event(RecruiterCandidateViewModel.INVALID_TOKEN))
            return
        }
        viewModelScope.launch {
            try {
                currentOfferId.postValue(offerId)
                responseGetOfferById.postValue(candidateRepository.getOfferById("Bearer $token", offerId))
                safeToVisitDomainOfferDetails.postValue(Event(true))
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }

    //Nullifier Functions
    fun nullifySafeToVisitOfferList(){
        safeToVisitOfferList.postValue(Event(false));
    }
    fun nullifySafeToVisitOfferDetails(){
        safeToVisitOfferDetails.postValue(Event(false));
    }
    fun nullifySafeToVisitCandidateList(){
        safeToVisitCandidates.postValue(Event(false));
    }
    fun nullifySafeToVisitCandidateDetails(){
        safeToVisitCandidateDetails.postValue(Event(false));
    }
    fun nullifySafeToVisitDomainOffers(){
        safeToVisitDomainOffers.postValue(Event(false));
    }
    fun nullifySafeToVisitDomainOfferDetails(){
        safeToVisitDomainOfferDetails.postValue(Event(false));
    }
    fun nullifySafeToVisitApplicationsList(){
        safeToVisitApplicationList.postValue(Event(false));
    }
    fun nullifySafeToVisitApplicationDetails(){
        safeToVisitApplicationDetails.postValue(Event(false));
    }
    fun getLocalProfile() = profileRepository.getLocalProfile()


    companion object {
        const val INVALID_TOKEN = "Invalid Token. Please log in again.";
        const val OFFERS_REQUESTED_ONCE = 0;
    }
}