package com.princeakash.projectified

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.MalformedURLException

class RecruiterViewModel(val app: Application) : AndroidViewModel(app) {
    val recruiterRepository: RecruiterRepository = (app as MyApplication).recruiterRepository

    var responseAddOffer : MutableLiveData<ResponseAddOffer> = MutableLiveData()
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

    fun addOffer(token:String, bodyAddOffer: BodyAddOffer){
        viewModelScope.launch {
            try {
                responseAddOffer.postValue(recruiterRepository.addOffer(bodyAddOffer, token))
            } catch(e: Exception){

                //Change the Mutable LiveData so that change can be detected in Fragment/Activity. One extra Observer per ViewModel per Activity
                errorString.postValue("Haha! You got an error!!" + e.localizedMessage)

                //Show Toast with Application Context. No extra Observers.
                (app as MyApplication).showToast(e.localizedMessage)
            }
        }
    }

    fun getOffersByRecruiter(token:String, recruiterID: String){
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

    fun getOfferByIdRecruiter(token:String, offerID:String){
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

    fun getOfferApplicants(token:String, offerID:String){
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

    fun updateOffer(token:String, offerID:String, bodyUpdateOffer:BodyUpdateOffer){
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

    fun toggleVisibility(token:String, offerID:String, bodyToggleVisibility: BodyToggleVisibility){
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

    fun deleteOffer(token:String, offerID:String){
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

    fun getApplicationById(token:String, applicationID:String){
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

    fun markSeen(token:String, applicationID:String, bodyMarkAsSeen: BodyMarkAsSeen){
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

    fun markSelected(token:String, applicationID:String, bodyMarkAsSelected: BodyMarkAsSelected){
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

    /*fun responseAddOffer() : LiveData<ResponseAddOffer> = responseAddOffer
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