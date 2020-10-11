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
import com.princeakash.projectified.recruiter.myOffers.model.*
import com.princeakash.projectified.user.ProfileRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class RecruiterExistingOffersViewModel(val app: Application) : AndroidViewModel(app) {

    //RecruiterRepository instance, guaranteed to be singular because of being
    val recruiterRepository: RecruiterRepository = (app as MyApplication).recruiterRepository
    val profileRepository: ProfileRepository = (app as MyApplication).profileRepository

    //MutableLiveData variables of responses for all kinds of requests h
    //picked up from instance of MyApplication.andled by RecruiterViewModel
    //which can be put to observation in Activities/Fragments
    private var responseGetOffersByRecruiter: MutableLiveData<ResponseGetOffersByRecruiter> = MutableLiveData()
    private var responseGetOfferByIdRecruiter: MutableLiveData<ResponseGetOfferByIdRecruiter> = MutableLiveData()
    private var responseGetOfferApplicants: MutableLiveData<ResponseGetOfferApplicants> = MutableLiveData()
    private var responseUpdateOffer: MutableLiveData<Event<ResponseUpdateOffer>> = MutableLiveData()
    private var responseToggleVisibility: MutableLiveData<Event<ResponseToggleVisibility>> = MutableLiveData()
    private var responseDeleteOffer: MutableLiveData<Event<ResponseDeleteOffer>> = MutableLiveData()
    private var responseGetApplicationById : MutableLiveData<ResponseGetApplicationByIdRecruiter> = MutableLiveData()
    private var responseMarkAsSeen: MutableLiveData<Event<ResponseMarkAsSeen>> = MutableLiveData()
    private var responseMarkAsSelected: MutableLiveData<Event<ResponseMarkAsSelected>> = MutableLiveData()
    private var errorString: MutableLiveData<Event<String>> = MutableLiveData()

    //Functions to expose all MutableLiveData instances as LiveData instances
    fun responseGetOffersByRecruiter() : LiveData<ResponseGetOffersByRecruiter> = responseGetOffersByRecruiter
    fun responseGetOfferByIdRecruiter(): LiveData<ResponseGetOfferByIdRecruiter> = responseGetOfferByIdRecruiter
    fun responseGetOfferApplicants() : LiveData<ResponseGetOfferApplicants> = responseGetOfferApplicants
    fun responseUpdateOffer(): LiveData<Event<ResponseUpdateOffer>> = responseUpdateOffer
    fun responseToggleVisibility(): LiveData<Event<ResponseToggleVisibility>> = responseToggleVisibility
    fun responseDeleteOffer(): LiveData<Event<ResponseDeleteOffer>> = responseDeleteOffer
    fun responseGetApplicationById() : LiveData<ResponseGetApplicationByIdRecruiter> = responseGetApplicationById
    fun responseMarkAsSeen() : LiveData<Event<ResponseMarkAsSeen>> = responseMarkAsSeen
    fun responseMarkAsSelected(): LiveData<Event<ResponseMarkAsSelected>> = responseMarkAsSelected
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
                responseGetOfferByIdRecruiter.postValue(recruiterRepository.getOfferByIdRecruiter("Bearer $token", offerID))
            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    fun getOfferApplicants(offerID:String){
        val token = profileRepository.getToken()
        if(token.equals("")) {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        viewModelScope.launch {
            try {
                responseGetOfferApplicants.postValue(recruiterRepository.getOfferApplicants("Bearer $token", offerID))
            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    fun updateOffer(offerID:String, bodyUpdateOffer: BodyUpdateOffer){
        val token = profileRepository.getToken()
        if(token.equals("")) {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        viewModelScope.launch {
            try {
                responseUpdateOffer.postValue(Event(recruiterRepository.updateOffer("Bearer $token", offerID, bodyUpdateOffer)))
            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    fun toggleVisibility(offerID:String, bodyToggleVisibility: BodyToggleVisibility){
        val token = profileRepository.getToken()
        if(token.equals("")) {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        viewModelScope.launch {
            try {
                responseToggleVisibility.postValue(Event(recruiterRepository.toggleVisibility("Bearer $token", offerID, bodyToggleVisibility)))
            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    fun deleteOffer(offerID:String){
        val token = profileRepository.getToken()
        if(token.equals("")) {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        viewModelScope.launch {
            try {
                responseDeleteOffer.postValue(Event(recruiterRepository.deleteOffer("Bearer $token", offerID)))
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
                responseGetApplicationById.postValue(recruiterRepository.getApplicationById("Bearer $token", applicationID))
            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    fun markSeen(applicationID:String, bodyMarkAsSeen: BodyMarkAsSeen){
        val token = profileRepository.getToken()
        if(token.equals("")) {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        viewModelScope.launch {
            try {
                responseMarkAsSeen.postValue(Event(recruiterRepository.markSeen("Bearer $token", applicationID, bodyMarkAsSeen)))
            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    fun markSelected(applicationID:String, bodyMarkAsSelected: BodyMarkAsSelected){
        val token = profileRepository.getToken()
        if(token.equals("")) {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        viewModelScope.launch {
            try {
                responseMarkAsSelected.postValue(Event(recruiterRepository.markSelected("Bearer $token", applicationID, bodyMarkAsSelected)))
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

    companion object {
        const val INVALID_TOKEN = "Invalid Token. Please log in again.";
    }
}