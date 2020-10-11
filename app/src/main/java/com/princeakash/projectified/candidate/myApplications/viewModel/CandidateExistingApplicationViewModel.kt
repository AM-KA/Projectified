package com.princeakash.projectified.candidate.myApplications.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.princeakash.projectified.Event
import com.princeakash.projectified.MyApplication
import com.princeakash.projectified.MyApplication.Companion.handleError
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
    private var responseUpdateApplication: MutableLiveData<Event<ResponseUpdateApplication>> = MutableLiveData()
    private var responseDeleteApplication: MutableLiveData<Event<ResponseDeleteApplication>> = MutableLiveData()
    private var responseGetApplicationByCandidate: MutableLiveData<ResponseGetApplicationsByCandidate> = MutableLiveData()
    private var responseGetApplicationDetailByIdCandidate: MutableLiveData<ResponseGetApplicationDetailByIdCandidate> = MutableLiveData()
    private var errorString: MutableLiveData<Event<String>> = MutableLiveData()

    //MutableLiveData variables of responses for all kinds of requests h
    //picked up from instance of MyApplication handled by RecruiterViewModel
    //which can be put to observation in Activities/Fragments
    fun responseUpdateApplication(): LiveData<Event<ResponseUpdateApplication>> = responseUpdateApplication
    fun responseDeleteApplication(): LiveData<Event<ResponseDeleteApplication>> = responseDeleteApplication
    fun responseGetApplicationByCandidate(): LiveData<ResponseGetApplicationsByCandidate> = responseGetApplicationByCandidate
    fun responseGetApplicationDetailByIdCandidate(): LiveData<ResponseGetApplicationDetailByIdCandidate> = responseGetApplicationDetailByIdCandidate
    fun errorString(): LiveData<Event<String>> = errorString

    fun getApplicationsByCandidate() {
        profileRepository.getUserId()
        val token: String = profileRepository.getToken()
        val applicantID: String = profileRepository.getUserId()
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
                responseGetApplicationByCandidate.postValue(candidateRepository.getApplicationByCandidate("Bearer $token", applicantID))
            } catch (e: Exception) {
                handleError(e, errorString)
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
                responseGetApplicationDetailByIdCandidate.postValue(candidateRepository.getApplicationDetailByIdCandidate("Bearer $token", applicationID))
            } catch (e: Exception) {
                handleError(e, errorString)
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
                responseUpdateApplication.postValue(Event(candidateRepository.updateApplication("Bearer $token", applicationID, bodyUpdateApplication)))
            } catch (e: Exception) {
                handleError(e, errorString)
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
                responseDeleteApplication.postValue(Event(candidateRepository.deleteApplication("Bearer $token", applicationID)))
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }
}