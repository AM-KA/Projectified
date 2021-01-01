package com.princeakash.projectified.user.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.princeakash.projectified.Event
import com.princeakash.projectified.MyApplication
import com.princeakash.projectified.MyApplication.Companion.handleError
import com.princeakash.projectified.user.model.*
import kotlinx.coroutines.launch

class ProfileViewModel(app: Application) : AndroidViewModel(app) {

    //ProfileRepository for all types of Profile-Related Operations
    private val profileRepository = (app as MyApplication).profileRepository

    //MutableLiveData for the data to be exposed
    private var errorString: MutableLiveData<Event<String>> = MutableLiveData()
    private var responseSignUp: MutableLiveData<Event<ResponseSignUp>> = MutableLiveData()
    private var responseLogin: MutableLiveData<Event<ResponseLogin>> = MutableLiveData()
    //private var responseCreateProfile: MutableLiveData<Event<ResponseCreateProfile>> = MutableLiveData()
    private var responseUpdateProfile: MutableLiveData<Event<ResponseUpdateProfile>> = MutableLiveData()
    private var responsecheckSignUp: MutableLiveData<Event<ResponseSignUp>> = MutableLiveData()
    private var responseGenerateOtp: MutableLiveData<Event<ResponseGenerateOtp>> = MutableLiveData()
    private var responseVerifyOtp: MutableLiveData<Event<ResponseVerifyOtp>> = MutableLiveData()
    private var responseUpdatePassword: MutableLiveData<Event<ResponseUpdatePassword>> = MutableLiveData()

    //LiveData for all exposed data
    fun errorString(): LiveData<Event<String>> = errorString
    fun responseSignUp(): LiveData<Event<ResponseSignUp>> = responseSignUp
    fun responseLogin(): LiveData<Event<ResponseLogin>> = responseLogin
    //fun responseCreateProfile(): LiveData<Event<ResponseCreateProfile>> = responseCreateProfile
    fun responseUpdateProfile(): LiveData<Event<ResponseUpdateProfile>> = responseUpdateProfile
    fun responsecheckSignUp(): LiveData<Event<ResponseSignUp>> = responsecheckSignUp
    fun responseGenerateOtp(): LiveData<Event<ResponseGenerateOtp>> = responseGenerateOtp
    fun responseVerifyOtp(): LiveData<Event<ResponseVerifyOtp>> = responseVerifyOtp
    fun responseUpdatePassword(): LiveData<Event<ResponseUpdatePassword>> = responseUpdatePassword

    //Functions based on *Local Data*
    fun getLoginStatus() = profileRepository.getLoginStatus()
    fun setLoginStatus(loginStatus: Boolean) = profileRepository.setLoginStatus(loginStatus)
    fun getProfileStatus() = profileRepository.getProfileStatus()
    fun setProfileStatus(profileStatus: Boolean) = profileRepository.setProfileStatus(profileStatus)
    fun getLocalProfile() = profileRepository.getLocalProfile()
    fun setLocalProfile(bodyModel: ProfileModel) = profileRepository.setLocalProfile(bodyModel)
    fun getToken() = profileRepository.getToken()
    fun setToken(token: String) = profileRepository.setToken(token)
    private fun setUserId(id: String) = profileRepository.setUserId(id)
    fun getResetEmail() = profileRepository.getResetEmail()
    fun setResetEmail(email: String) = profileRepository.setResetEmail(email)

    //Functions based on *Server Data*
    fun signUp(bodySignUp: BodySignUp) {
        viewModelScope.launch {
            try {
                val response = profileRepository.signUp(bodySignUp)
                if (response.code == 200)
                    responseSignUp.postValue(Event(response))
                else
                    errorString.postValue(Event(response.message))
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }

    fun checksignup(bodySignUp: BodySignUp) {
        viewModelScope.launch {
            try {
                val response = profileRepository.checksignUp(bodySignUp)
                if (response.code == 200)
                    responsecheckSignUp.postValue((Event(response)))
                else if (response.code == 300)
                    responsecheckSignUp.postValue((Event(response)))
                else
                    errorString.postValue(Event(response.message))
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }

    fun logIn(bodyLogin: LoginBody) {
        viewModelScope.launch {
            try {
                val response = profileRepository.logIn(bodyLogin)
                setLoginStatus(true)
                if (response.code == 200) {
                    setToken(response.token!!)
                    setUserId(response._id!!)
                    Log.d(TAG, "logIn: " + response.token)
                    //TODO: Fetch Profile and save to SharedPreference using setLocalProfile()
                    if (response.profileCompleted!!) {
                        //val bodyProfile = response.profile
                        val bodyProfile = ProfileModel(response.name!!,
                                response.collegeName!!, response.course!!,
                                response.semester!!, response.languages!!,
                                response.interest1!!, response.interest2!!,
                                response.interest3!!, response.description!!,
                                response.hobbies!!)
                        setLocalProfile(bodyProfile!!)
                    }
                }
                responseLogin.postValue(Event(response))
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }

    /*fun createProfile(bodyCreateProfile: BodyCreateProfile) {
        viewModelScope.launch {
            try {
                val token = profileRepository.getToken()
                val userID = profileRepository.getUserId()
                bodyCreateProfile.userID = userID
                Log.d(TAG, "createProfile: $token")
                responseCreateProfile.postValue(Event(profileRepository.createProfile("Bearer $token", bodyCreateProfile)))
                setLocalProfile(ProfileModel(bodyCreateProfile))
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }*/

    fun updateProfile(bodyUpdateProfile: BodyUpdateProfile) {
        viewModelScope.launch {
            try {
                val token = profileRepository.getToken()
                val userID = profileRepository.getUserId()
                bodyUpdateProfile.userID = userID
                responseUpdateProfile.postValue(Event(profileRepository.updateProfile("Bearer $token", bodyUpdateProfile)))
                setLocalProfile(ProfileModel(bodyUpdateProfile))
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }

    fun generateOtp(email: String){
        viewModelScope.launch {
            try {
                setResetEmail(email)
                val bodyGenerateOtp = BodyGenerateOtp(email)
                responseGenerateOtp.postValue(Event(profileRepository.generateOtp(bodyGenerateOtp)))
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }

    fun verifyOtp(otp: String){
        viewModelScope.launch {
            try {
                val resetEmail = getResetEmail()
                val bodyVerifyOtp = BodyVerifyOtp(resetEmail, otp)
                responseVerifyOtp.postValue(Event(profileRepository.verifyOtp(bodyVerifyOtp)))
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }

    fun updatePassword(newPassword: String){
        viewModelScope.launch {
            try {
                val email = getResetEmail()
                val bodyUpdatePassword = BodyUpdatePassword(email, newPassword)
                responseUpdatePassword.postValue(Event(profileRepository.updatePassword(bodyUpdatePassword)))
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }
}