package com.princeakash.projectified.recruiter.myOffers.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.princeakash.projectified.MyApplication
import com.princeakash.projectified.ProfileFragment
import com.princeakash.projectified.recruiter.*
import com.princeakash.projectified.user.ProfileRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class RecruiterExistingOffersViewModel(val app: Application) : AndroidViewModel(app) {

    //RecruiterRepository instance, guaranteed to be singular because of being
    //picked up from instance of MyApplication.
    val recruiterRepository: RecruiterRepository = (app as MyApplication).recruiterRepository
    val profileRepository: ProfileRepository = (app as MyApplication).profileRepository

    //MutableLiveData variables of responses for all kinds of requests handled by RecruiterViewModel
    //which can be put to observation in Activities/Fragments
    var responseGetOffersByRecruiter: MutableLiveData<ResponseGetOffersByRecruiter> = MutableLiveData()
    var responseGetOfferByIdRecruiter: MutableLiveData<ResponseGetOfferByIdRecruiter> = MutableLiveData()
    var responseGetOfferApplicants: MutableLiveData<ResponseGetOfferApplicants> = MutableLiveData()
    var responseUpdateOffer: MutableLiveData<ResponseUpdateOffer> = MutableLiveData()
    var responseToggleVisibility: MutableLiveData<ResponseToggleVisibility> = MutableLiveData()
    var responseDeleteOffer: MutableLiveData<ResponseDeleteOffer> = MutableLiveData()
    var responseGetApplicationById : MutableLiveData<ResponseGetApplicationByIdRecruiter> = MutableLiveData()
    var responseMarkAsSeen: MutableLiveData<ResponseMarkAsSeen> = MutableLiveData()
    var responseMarkAsSelected: MutableLiveData<ResponseMarkAsSelected> = MutableLiveData()
    var errorString: MutableLiveData<String> = MutableLiveData()

    fun getOffersByRecruiter(){
        val token:String = profileRepository.getToken()
        val recruiterID: String = profileRepository.getUserId()
        if(token.equals("")) {
            errorString.postValue("Invalid Token. Please log in again.")
            return
        }
        if(recruiterID.equals("")){
            errorString.postValue("Invalid User ID. Please log in again.")
            return
        }
        viewModelScope.launch {
            try {
                responseGetOffersByRecruiter.postValue(recruiterRepository.getOffersByRecruiter(token, recruiterID))
            } catch(e: Exception){

                //Change the Mutable LiveData so that change can be detected in Fragment/Activity. One extra Observer per ViewModel per Activity
                errorString.postValue("Haha! You got an error!!" + e.localizedMessage)

                //Show Toast with Application Context. No extra Observers.
                (app as MyApplication).showToast(e.localizedMessage)
            }
        }
    }

    fun getOfferByIdRecruiter(offerID:String){
        val token = profileRepository.getToken()
        if(token.equals("")) {
            errorString.postValue("Invalid Token. Please log in again.")
            return
        }
        viewModelScope.launch {
            try {
                responseGetOfferByIdRecruiter.postValue(recruiterRepository.getOfferByIdRecruiter(token, offerID))
            } catch(e: Exception){

                //Change the Mutable LiveData so that change can be detected in Fragment/Activity. One extra Observer per ViewModel per Activity
                errorString.postValue("Haha! You got an error!!" + e.localizedMessage)

                //Show Toast with Application Context. No extra Observers.
                (app as MyApplication).showToast(e.localizedMessage)
            }
        }
    }

    fun getOfferApplicants(offerID:String){
        val token = profileRepository.getToken()
        if(token.equals("")) {
            errorString.postValue("Invalid Token. Please log in again.")
            return
        }
        viewModelScope.launch {
            try {
                responseGetOfferApplicants.postValue(recruiterRepository.getOfferApplicants(token, offerID))
            } catch(e: Exception){

                //Change the Mutable LiveData so that change can be detected in Fragment/Activity. One extra Observer per ViewModel per Activity
                errorString.postValue("Haha! You got an error!!" + e.localizedMessage)

                //Show Toast with Application Context. No extra Observers.
                (app as MyApplication).showToast(e.localizedMessage)
            }
        }
    }

    fun updateOffer(offerID:String, bodyUpdateOffer: BodyUpdateOffer){
        val token = profileRepository.getToken()
        if(token.equals("")) {
            errorString.postValue("Invalid Token. Please log in again.")
            return
        }
        viewModelScope.launch {
            try {
                responseUpdateOffer.postValue(recruiterRepository.updateOffer(token, offerID, bodyUpdateOffer))
            } catch(e: Exception){

                //Change the Mutable LiveData so that change can be detected in Fragment/Activity. One extra Observer per ViewModel per Activity
                errorString.postValue("Haha! You got an error!!" + e.localizedMessage)

                //Show Toast with Application Context. No extra Observers.
                (app as MyApplication).showToast(e.localizedMessage)
            }
        }
    }

    fun toggleVisibility(offerID:String, bodyToggleVisibility: BodyToggleVisibility){
        val token = profileRepository.getToken()
        if(token.equals("")) {
            errorString.postValue("Invalid Token. Please log in again.")
            return
        }
        viewModelScope.launch {
            try {
                responseToggleVisibility.postValue(recruiterRepository.toggleVisibility(token, offerID, bodyToggleVisibility))
            } catch(e: Exception){

                //Change the Mutable LiveData so that change can be detected in Fragment/Activity. One extra Observer per ViewModel per Activity
                errorString.postValue("Haha! You got an error!!" + e.localizedMessage)

                //Show Toast with Application Context. No extra Observers.
                (app as MyApplication).showToast(e.localizedMessage)
            }
        }
    }

    fun deleteOffer(offerID:String){
        val token = profileRepository.getToken()
        if(token.equals("")) {
            errorString.postValue("Invalid Token. Please log in again.")
            return
        }
        viewModelScope.launch {
            try {
                responseDeleteOffer.postValue(recruiterRepository.deleteOffer(token, offerID))
            } catch(e: Exception){

                //Change the Mutable LiveData so that change can be detected in Fragment/Activity. One extra Observer per ViewModel per Activity
                errorString.postValue("Haha! You got an error!!" + e.localizedMessage)

                //Show Toast with Application Context. No extra Observers.
                (app as MyApplication).showToast(e.localizedMessage)
            }
        }
    }

    fun getApplicationById(applicationID:String){
        val token = profileRepository.getToken()
        if(token.equals("")) {
            errorString.postValue("Invalid Token. Please log in again.")
            return
        }
        viewModelScope.launch {
            try {
                responseGetApplicationById.postValue(recruiterRepository.getApplicationById(token, applicationID))
            } catch(e: Exception){

                //Change the Mutable LiveData so that change can be detected in Fragment/Activity. One extra Observer per ViewModel per Activity
                errorString.postValue("Haha! You got an error!!" + e.localizedMessage)

                //Show Toast with Application Context. No extra Observers.
                (app as MyApplication).showToast(e.localizedMessage)
            }
        }
    }

    fun markSeen(applicationID:String, bodyMarkAsSeen: BodyMarkAsSeen){
        val token = profileRepository.getToken()
        if(token.equals("")) {
            errorString.postValue("Invalid Token. Please log in again.")
            return
        }
        viewModelScope.launch {
            try {
                responseMarkAsSeen.postValue(recruiterRepository.markSeen(token, applicationID, bodyMarkAsSeen))
            } catch(e: Exception){

                //Change the Mutable LiveData so that change can be detected in Fragment/Activity. One extra Observer per ViewModel per Activity
                errorString.postValue("Haha! You got an error!!" + e.localizedMessage)

                //Show Toast with Application Context. No extra Observers.
                (app as MyApplication).showToast(e.localizedMessage)
            }
        }
    }

    fun markSelected(applicationID:String, bodyMarkAsSelected: BodyMarkAsSelected){
        val token = profileRepository.getToken()
        if(token.equals("")) {
            errorString.postValue("Invalid Token. Please log in again.")
            return
        }
        viewModelScope.launch {
            try {
                responseMarkAsSelected.postValue(recruiterRepository.markSelected(token, applicationID, bodyMarkAsSelected))
            } catch(e: Exception){

                //Change the Mutable LiveData so that change can be detected in Fragment/Activity. One extra Observer per ViewModel per Activity
                errorString.postValue("Haha! You got an error!!" + e.localizedMessage)

                //Show Toast with Application Context. No extra Observers.
                (app as MyApplication).showToast(e.localizedMessage)
            }
        }
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
}