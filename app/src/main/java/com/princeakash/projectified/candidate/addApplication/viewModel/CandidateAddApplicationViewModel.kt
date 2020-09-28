package com.princeakash.projectified.candidate.addApplication.viewModel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.princeakash.projectified.Event
import com.princeakash.projectified.MyApplication
import com.princeakash.projectified.candidate.CandidateRepository
import com.princeakash.projectified.candidate.addApplication.model.BodyAddApplication
import com.princeakash.projectified.candidate.addApplication.model.ResponseAddApplication
import com.princeakash.projectified.candidate.addApplication.model.ResponseGetOfferById
import com.princeakash.projectified.candidate.addApplication.model.ResponseGetOffersByDomain
import com.princeakash.projectified.recruiter.RecruiterRepository
import com.princeakash.projectified.recruiter.myOffers.viewmodel.RecruiterExistingOffersViewModel
import com.princeakash.projectified.user.ProfileRepository
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class CandidateAddApplicationViewModel(val app: Application): AndroidViewModel(app) {

        //CandidateRepository instance, guaranteed to be singular because of being
        //picked up from instance of MyApplication.
        val candidateRepository: CandidateRepository = (app as MyApplication).candidateRepository
        val profileRepository: ProfileRepository = (app as MyApplication).profileRepository
        val recruiterRepository : RecruiterRepository=(app as MyApplication).recruiterRepository

        //MutableLiveData variables of responses for all kinds of requests handled by RecruiterViewModel
        //which can be put to observation in Activities/Fragments
        var responseGetOffersByDomain : MutableLiveData<ResponseGetOffersByDomain> = MutableLiveData()
        var responseAddApplication: MutableLiveData<Event<ResponseAddApplication>> = MutableLiveData()
        var responseGetOfferById: MutableLiveData<ResponseGetOfferById> = MutableLiveData()
        var errorString: MutableLiveData<Event<String>> = MutableLiveData()

        fun getOffersByDomain( domainName:String ){
            val token = profileRepository.getToken()
            if(token == "") {
                errorString.postValue(Event(RecruiterExistingOffersViewModel.INVALID_TOKEN))
                return
            }
            viewModelScope.launch {
                try {
                      responseGetOffersByDomain.postValue(candidateRepository.getOffersByDomain("Bearer $token",domainName))
                } catch(e: Exception){
                    handleError(e)
                }
            }
        }

    fun addApplication(Resume:String , PreviousWork: String ,offerId: String){
        val token = profileRepository.getToken()
        val applicantID:String=profileRepository.getUserId()
        if(token == "") {
            errorString.postValue(Event(RecruiterExistingOffersViewModel.INVALID_TOKEN))
            return
        }
        viewModelScope.launch {
            try {

               val bodyAddApplication= BodyAddApplication(Date(),Resume,PreviousWork,applicantID ,offerId)
                responseAddApplication.postValue(Event(candidateRepository.addApplication("Bearer $token",bodyAddApplication)))
            } catch(e: Exception){
                handleError(e)
            }
        }
    }
      fun getoffersById(offerId: String) {
          val token = profileRepository.getToken()
          if(token == "") {
              errorString.postValue(Event(RecruiterExistingOffersViewModel.INVALID_TOKEN))
              return
          }
          viewModelScope.launch {
              try {
                 responseGetOfferById.postValue(candidateRepository.getOfferById("Bearer " + token,offerId))
              } catch(e: Exception){
                  handleError(e)
              }
          }
      }

    fun getLocalProfile() = profileRepository.getLocalProfile()

    fun handleError(e: Exception){
        e.printStackTrace()

        //Change the Mutable LiveData so that change can be detected in Fragment/Activity. One extra Observer per ViewModel per Activity
        errorString.postValue(Event("Haha! You got an error!!" + e.localizedMessage))
    }
}
