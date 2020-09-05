package com.princeakash.projectified.recruiter.addOffer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.princeakash.projectified.MyApplication
import com.princeakash.projectified.recruiter.RecruiterRepository
import com.princeakash.projectified.recruiter.addOffer.model.BodyAddOffer
import com.princeakash.projectified.recruiter.addOffer.model.ResponseAddOffer
import com.princeakash.projectified.user.ProfileRepository
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class RecruiterAddOffersViewModel(val app: Application): AndroidViewModel(app) {

    //RecruiterRepository instance, guaranteed to be singular because of being
    //picked up from instance of MyApplication.
    val recruiterRepository: RecruiterRepository = (app as MyApplication).recruiterRepository
    val profileRepository: ProfileRepository = (app as MyApplication).profileRepository
    var errorString: MutableLiveData<String> = MutableLiveData()

    //MutableLiveData variables of responses for all kinds of requests handled by RecruiterViewModel
    //which can be put to observation in Activities/Fragments
    var responseAddOffer : MutableLiveData<ResponseAddOffer> = MutableLiveData()

    fun addOffer(requirements: String, skills: String, expectation: String){
        val token = profileRepository.getToken()
        val recruiterID: String = profileRepository.getUserId()
        if(token == "") {
            errorString.postValue("Invalid Token. Please log in again.")
            return
        }
        if(recruiterID.equals("")){
            errorString.postValue("Invalid User ID. Please log in again.")
            return
        }
        viewModelScope.launch {
            try {
                TODO("Get Domain Name from Profile Repository")
                TODO("Get Offer Name from Layout")
                val bodyAddOffer = BodyAddOffer(Date(), "Offer Name", "Domain Name", requirements, skills, expectation, recruiterID)
                responseAddOffer.postValue(recruiterRepository.addOffer(bodyAddOffer, token))
            } catch(e: Exception){

                //Change the Mutable LiveData so that change can be detected in Fragment/Activity. One extra Observer per ViewModel per Activity
                errorString.postValue("Haha! You got an error!!" + e.localizedMessage)

                //Show Toast with Application Context. No extra Observers.
                (app as MyApplication).showToast(e.localizedMessage)
            }
        }
    }
}