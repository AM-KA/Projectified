package com.princeakash.projectified.recruiter.addOffer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.princeakash.projectified.Event
import com.princeakash.projectified.MyApplication
import com.princeakash.projectified.MyApplication.Companion.handleError
import com.princeakash.projectified.recruiter.RecruiterRepository
import com.princeakash.projectified.recruiter.addOffer.model.BodyAddOffer
import com.princeakash.projectified.recruiter.addOffer.model.ResponseAddOffer
import com.princeakash.projectified.recruiter.myOffers.viewmodel.RecruiterExistingOffersViewModel
import com.princeakash.projectified.user.ProfileRepository
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class RecruiterAddOffersViewModel(val app: Application): AndroidViewModel(app) {

    //RecruiterRepository instance, guaranteed to be singular because of being
    //picked up from instance of MyApplication.
    val recruiterRepository: RecruiterRepository = (app as MyApplication).recruiterRepository
    val profileRepository: ProfileRepository = (app as MyApplication).profileRepository

    //MutableLiveData variables of responses for all kinds of requests handled by RecruiterAddOffersViewModel
    //which can be put to observation in Activities/Fragments
    private val responseAddOffer : MutableLiveData<Event<ResponseAddOffer>> = MutableLiveData()
    private val errorString: MutableLiveData<Event<String>> = MutableLiveData()

    //LiveData variables of responses for all kinds of requests handled by RecruiterAddOffersViewModel
    //which can be put to observation in Activities/Fragments
    fun responseAddOffer() : LiveData<Event<ResponseAddOffer>> = responseAddOffer
    fun errorString(): LiveData<Event<String>> = errorString

    fun addOffer(offerName:String, domainName:String, requirements: String, skills: String, expectation: String){
        val token = profileRepository.getToken()
        val recruiterID: String = profileRepository.getUserId()
        if(token == "") {
            errorString.postValue(Event(RecruiterExistingOffersViewModel.INVALID_TOKEN))
            return
        }
        if(recruiterID.equals("")){
            errorString.postValue(Event("Invalid User ID. Please log in again."))
            return
        }
        viewModelScope.launch {
            try {
                val bodyAddOffer = BodyAddOffer(offerName, domainName, requirements, skills, expectation, recruiterID)
                responseAddOffer.postValue(Event(recruiterRepository.addOffer(bodyAddOffer, "Bearer $token")))
            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    fun getLocalProfile() = profileRepository.getLocalProfile()
}