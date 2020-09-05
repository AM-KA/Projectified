package com.princeakash.projectified.candidate.addApplication.viewModel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.princeakash.projectified.MyApplication
import com.princeakash.projectified.candidate.CandidateRepository
import com.princeakash.projectified.candidate.addApplication.model.BodyAddApplication
import com.princeakash.projectified.candidate.addApplication.model.ResponseAddApplication
import com.princeakash.projectified.candidate.myApplications.model.ResponseGetOfferById
import com.princeakash.projectified.candidate.myApplications.model.ResponseGetOffersByDomain
import com.princeakash.projectified.recruiter.RecruiterRepository
import com.princeakash.projectified.user.ProfileRepository
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class CandidateAddApplicationViewModel(val app: Application): AndroidViewModel(app) {

        //RecruiterRepository instance, guaranteed to be singular because of being
        //picked up from instance of MyApplication.
        val candidateRepository: CandidateRepository = (app as MyApplication).candidateRepository
        val profileRepository: ProfileRepository = (app as MyApplication).profileRepository
        var errorString: MutableLiveData<String> = MutableLiveData()
        val recruiterRepository : RecruiterRepository=(app as MyApplication).recruiterRepository

        //MutableLiveData variables of responses for all kinds of requests handled by RecruiterViewModel
        //which can be put to observation in Activities/Fragments
        var responseGetOffersByDomain : MutableLiveData<ResponseGetOffersByDomain> = MutableLiveData()
        var responseAddApplication: MutableLiveData<ResponseAddApplication> = MutableLiveData()
        var responseGetOffersById: MutableLiveData<ResponseGetOfferById> = MutableLiveData()


        fun getOffersByDomain( domainName:String ){
            val token = profileRepository.getToken()
            if(token == "") {
                errorString.postValue("Invalid Token. Please log in again.")
                return
            }
            viewModelScope.launch {
                try {

                      responseGetOffersByDomain.postValue(candidateRepository.getOffersByDomain(token,domainName))
                } catch(e: Exception){

                    //Change the Mutable LiveData so that change can be detected in Fragment/Activity. One extra Observer per ViewModel per Activity
                    errorString.postValue("Haha! You got an error!!" + e.localizedMessage)

                    //Show Toast with Application Context. No extra Observers.
                    (app as MyApplication).showToast(e.localizedMessage)
                }
            }
        }

    fun addApplication(Resume:String , PreviousWork: String ,offerId: String){
        val token = profileRepository.getToken()

     //  / val recruiterID: String = profileRepository///
        val applicantID: String =profileRepository.getUserId()

        if(token == "") {
            errorString.postValue("Invalid Token. Please log in again.")
            return
        }

        viewModelScope.launch {
            try {

                TODO("Get Recuirter id from repo")
               val   bodyAddApplication= BodyAddApplication(Date(),Resume,PreviousWork,applicantID ,"Offer Id","recuirter Id")
                responseAddApplication.postValue(candidateRepository.addApplication(token,bodyAddApplication))
            } catch(e: Exception){

                //Change the Mutable LiveData so that change can be detected in Fragment/Activity. One extra Observer per ViewModel per Activity
                errorString.postValue("Haha! You got an error!!" + e.localizedMessage)

                //Show Toast with Application Context. No extra Observers.
                (app as MyApplication).showToast(e.localizedMessage)
            }
        }
    }
      fun getoffersById(offerId: String) {
          val token = profileRepository.getToken()
          if(token == "") {
              errorString.postValue("Invalid Token. Please log in again.")
              return
          }
          viewModelScope.launch {
              try {

                 responseGetOffersById.postValue(candidateRepository.getOfferById(token,offerId))
              } catch(e: Exception){

                  //Change the Mutable LiveData so that change can be detected in Fragment/Activity. One extra Observer per ViewModel per Activity
                  errorString.postValue("Haha! You got an error!!" + e.localizedMessage)

                  //Show Toast with Application Context. No extra Observers.
                  (app as MyApplication).showToast(e.localizedMessage)
              }
          }
      }
    }
