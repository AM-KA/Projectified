package com.princeakash.projectified.candidate.myApplications.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.princeakash.projectified.Event
import com.princeakash.projectified.MyApplication
import com.princeakash.projectified.candidate.CandidateRepository
import com.princeakash.projectified.candidate.myApplications.model.*
import com.princeakash.projectified.recruiter.myOffers.viewmodel.RecruiterExistingOffersViewModel
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

    var responseUpdateApplication: MutableLiveData<Event<ResponseUpdateApplication>> = MutableLiveData()
    var responseDeleteApplication: MutableLiveData<Event<ResponseDeleteApplication>> = MutableLiveData()
    var responseGetApplicationByCandidate: MutableLiveData<ResponseGetApplicationsByCandidate> = MutableLiveData()
    var responseGetApplicationDetailByIdCandidate: MutableLiveData<ResponseGetApplicationDetailByIdCandidate> = MutableLiveData()
    var errorString: MutableLiveData<Event<String>> = MutableLiveData()

    fun getApplicationsByCandidate() {profileRepository.getUserId()
        val token: String = profileRepository.getToken()
        val applicantID: String =profileRepository.getUserId()
        if (token.equals("")) {
            errorString.postValue(Event(RecruiterExistingOffersViewModel.INVALID_TOKEN))
            return
        }
        if (applicantID.equals("")) {
            errorString.postValue(Event("Invalid User ID. Please log in again."))
            return
        }
        viewModelScope.launch {
            try {
                responseGetApplicationByCandidate.postValue(candidateRepository.getApplicationByCandidate("Bearer " +token, applicantID))
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun getApplicationDetailByIdCandidate(applicationID: String) {
        val token = profileRepository.getToken()
        if (token.equals("")) {
            errorString.postValue(Event(RecruiterExistingOffersViewModel.INVALID_TOKEN))
            return
        }
        viewModelScope.launch {
            try {
                responseGetApplicationDetailByIdCandidate.postValue(candidateRepository.getApplicationDetailByIdCandidate("Bearer " +token, applicationID))
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }


    fun updateApplication(applicationID: String, bodyUpdateApplication: BodyUpdateApplication) {
        val token = profileRepository.getToken()
        if (token.equals("")) {
            errorString.postValue(Event(RecruiterExistingOffersViewModel.INVALID_TOKEN))
            return
        }
        viewModelScope.launch {
            try {
                responseUpdateApplication.postValue(Event(candidateRepository.updateApplication("Bearer " +token, applicationID, bodyUpdateApplication)))
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }


    fun deleteApplication(applicationID: String) {
        val token = profileRepository.getToken()
        if (token.equals("")) {
            errorString.postValue(Event(RecruiterExistingOffersViewModel.INVALID_TOKEN))
            return
        }
        viewModelScope.launch {
            try {
                responseDeleteApplication.postValue(Event(candidateRepository.deleteApplication("Bearer " +token ,applicationID)))
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun handleError(e: Exception){
        e.printStackTrace()

        //Change the Mutable LiveData so that change can be detected in Fragment/Activity. One extra Observer per ViewModel per Activity
        errorString.postValue(Event("Haha! You got an error!!" + e.localizedMessage))
    }
}