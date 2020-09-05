package com.princeakash.projectified.candidate.myApplications.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.princeakash.projectified.MyApplication
import com.princeakash.projectified.candidate.CandidateRepository
import com.princeakash.projectified.candidate.myApplications.model.*
import com.princeakash.projectified.user.ProfileRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class CandidateExistingApplicationViewModel(val app: Application) : AndroidViewModel(app) {

    //CandidaterRepository instance, guaranteed to be singular because of being
    val profileRepository: ProfileRepository = (app as MyApplication).profileRepository
    val candidateRepository: CandidateRepository = (app as MyApplication).candidateRepository

    //MutableLiveData variables of responses for all kinds of requests h
    //picked up from instance of MyApplication handled by RecruiterViewModel
    //which can be put to observation in Activities/Fragments

    var responseUpdateApplication: MutableLiveData<ResponseUpdateApplication> = MutableLiveData()
    var responseDeleteApplication: MutableLiveData<ResponseDeleteApplication> = MutableLiveData()
    var responseGetApplicationByCandidate: MutableLiveData<ResponseGetApplicationsByCandidate> = MutableLiveData()
    var responseGetApplicationDetailByIdCandidate: MutableLiveData<ResponseGetApplicationDetailByIdCandidate> = MutableLiveData()
    var errorString: MutableLiveData<String> = MutableLiveData()

    fun getApplicationsByCandidate() {
        val token: String = profileRepository.getToken()
        val applicantID: String = profileRepository.getUserId()
        if (token.equals("")) {
            errorString.postValue("Invalid Token. Please log in again.")
            return
        }
        if (applicantID.equals("")) {
            errorString.postValue("Invalid User ID. Please log in again.")
            return
        }
        viewModelScope.launch {
            try {
                responseGetApplicationByCandidate.postValue(candidateRepository.getApplicationByCandidate(token, applicantID))
            } catch (e: Exception) {

                //Change the Mutable LiveData so that change can be detected in Fragment/Activity. One extra Observer per ViewModel per Activity
                errorString.postValue("Haha! You got an error!!" + e.localizedMessage)

                //Show Toast with Application Context. No extra Observers.
                (app as MyApplication).showToast(e.localizedMessage)
            }
        }
    }

    fun getApplicationDetailByIdCandidate(applicationID: String) {
        val token = profileRepository.getToken()
        if (token.equals("")) {
            errorString.postValue("Invalid Token. Please log in again.")
            return
        }
        viewModelScope.launch {
            try {
                responseGetApplicationDetailByIdCandidate.postValue(candidateRepository.getApplicationDetailByIdCandidate(token, applicationID))
            } catch (e: Exception) {

                //Change the Mutable LiveData so that change can be detected in Fragment/Activity. One extra Observer per ViewModel per Activity
                errorString.postValue("Haha! You got an error!!" + e.localizedMessage)

                //Show Toast with Application Context. No extra Observers.
                (app as MyApplication).showToast(e.localizedMessage)
            }
        }
    }


    fun updateApplication(applicationID: String, bodyUpdateApplication: BodyUpdateApplication) {
        val token = profileRepository.getToken()
        if (token.equals("")) {
            errorString.postValue("Invalid Token. Please log in again.")
            return
        }
        viewModelScope.launch {
            try {
                responseUpdateApplication.postValue(candidateRepository.updateApplication(token, applicationID, bodyUpdateApplication))
            } catch (e: Exception) {

                //Change the Mutable LiveData so that change can be detected in Fragment/Activity. One extra Observer per ViewModel per Activity
                errorString.postValue("Haha! You got an error!!" + e.localizedMessage)

                //Show Toast with Application Context. No extra Observers.
                (app as MyApplication).showToast(e.localizedMessage)
            }
        }
    }


    fun deleteApplication(applicationID: String) {
        val token = profileRepository.getToken()
        if (token.equals("")) {
            errorString.postValue("Invalid Token. Please log in again.")
            return
        }
        viewModelScope.launch {
            try {
                responseDeleteApplication.postValue(candidateRepository.deleteApplication(applicationID, token))
            } catch (e: Exception) {

                //Change the Mutable LiveData so that change can be detected in Fragment/Activity. One extra Observer per ViewModel per Activity
                errorString.postValue("Haha! You got an error!!" + e.localizedMessage)

                //Show Toast with Application Context. No extra Observers.
                (app as MyApplication).showToast(e.localizedMessage)
            }
        }
    }

}