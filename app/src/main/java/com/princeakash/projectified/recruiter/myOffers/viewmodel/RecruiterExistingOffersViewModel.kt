package com.princeakash.projectified.recruiter.myOffers.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.princeakash.projectified.Event
import com.princeakash.projectified.MyApplication
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
    var responseGetOffersByRecruiter: MutableLiveData<ResponseGetOffersByRecruiter> = MutableLiveData()
    var responseGetOfferByIdRecruiter: MutableLiveData<ResponseGetOfferByIdRecruiter> = MutableLiveData()
    var responseGetOfferApplicants: MutableLiveData<ResponseGetOfferApplicants> = MutableLiveData()
    var responseUpdateOffer: MutableLiveData<Event<ResponseUpdateOffer>> = MutableLiveData()
    var responseToggleVisibility: MutableLiveData<Event<ResponseToggleVisibility>> = MutableLiveData()
    var responseDeleteOffer: MutableLiveData<Event<ResponseDeleteOffer>> = MutableLiveData()
    var responseGetApplicationById : MutableLiveData<ResponseGetApplicationByIdRecruiter> = MutableLiveData()
    var responseMarkAsSeen: MutableLiveData<Event<ResponseMarkAsSeen>> = MutableLiveData()
    var responseMarkAsSelected: MutableLiveData<Event<ResponseMarkAsSelected>> = MutableLiveData()
    var errorString: MutableLiveData<Event<String>> = MutableLiveData()

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
                responseGetOffersByRecruiter.postValue(recruiterRepository.getOffersByRecruiter("Bearer "+token, recruiterID))
            } catch(e: Exception){
                handleError(e)
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
                responseGetOfferByIdRecruiter.postValue(recruiterRepository.getOfferByIdRecruiter("Bearer "+token, offerID))
            } catch(e: Exception){
                handleError(e)
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
                responseGetOfferApplicants.postValue(recruiterRepository.getOfferApplicants("Bearer "+token, offerID))
            } catch(e: Exception){
                handleError(e)
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
                responseUpdateOffer.postValue(Event(recruiterRepository.updateOffer("Bearer "+token, offerID, bodyUpdateOffer)))
            } catch(e: Exception){
                handleError(e)
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
                responseToggleVisibility.postValue(Event(recruiterRepository.toggleVisibility("Bearer "+token, offerID, bodyToggleVisibility)))
            } catch(e: Exception){
                handleError(e)
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
                responseDeleteOffer.postValue(Event(recruiterRepository.deleteOffer("Bearer "+token, offerID)))
            } catch(e: Exception){
                handleError(e)
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
                responseGetApplicationById.postValue(recruiterRepository.getApplicationById("Bearer "+token, applicationID))
            } catch(e: Exception){
                handleError(e)
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
                responseMarkAsSeen.postValue(Event(recruiterRepository.markSeen("Bearer "+token, applicationID, bodyMarkAsSeen)))
            } catch(e: Exception){
                handleError(e)
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
                responseMarkAsSelected.postValue(Event(recruiterRepository.markSelected("Bearer "+token, applicationID, bodyMarkAsSelected)))
            } catch(e: Exception){
                handleError(e)
            }
        }
    }

    fun handleError(e: Exception){
        e.printStackTrace()

        //Change the Mutable LiveData so that change can be detected in Fragment/Activity. One extra Observer per ViewModel per Activity
        errorString.postValue(Event("Haha! You got an error!!" + e.localizedMessage))
    }

    /*
    //Functions to expose all MutableLiveData instances as LiveData instances
    fun responseAddOffer() : LiveData<ResponseAddOffer> = responseAddOffer
    fun responseGetOffersByRecruiter() : LiveData<ResponseGetOffersByRecruiter> = responseGetOffersByRecruiter
    fun responseGetOfferByIdRecruiter(): LiveData<ResponseGetOfferByIdRecruiter> = responseGetOfferByIdRecruiter
    fun responseGetOfferApplicants() : LiveData<ResponseGetOfferApplicants> = responseGetOfferApplicants
    fun responseUpdateOffer(): LiveData<ResponseUpdateOffer> = responseUpdateOffer
    fun responseToggleVisibility(): LiveData<ResponseToggleVisibility> = responseToggleVisibility
    fun responseDeleteOffer(): LiveData<ResponseDeleteOffer> = responseDeleteOffer
    fun responseGetApplicationById() : LiveData<ResponseGetApplicationByIdRecruiter> = responseGetApplicationById
    fun responseMarkAsSeen() : LiveData<ResponseMarkAsSeen> = responseMarkAsSeen
    fun responseMarkAsSelected(): LiveData<ResponseMarkAsSelected> = responseMarkAsSelected*/

    companion object {
        const val INVALID_TOKEN = "Invalid Token. Please log in again.";
    }
}