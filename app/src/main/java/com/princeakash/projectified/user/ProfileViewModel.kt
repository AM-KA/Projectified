package com.princeakash.projectified.user

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.princeakash.projectified.Event
import com.princeakash.projectified.MyApplication
import kotlinx.coroutines.launch

class ProfileViewModel(app: Application) : AndroidViewModel(app) {

    //ProfileRepository for all types of Profile-Related Operations
    private val profileRepository = (app as MyApplication).profileRepository

    //MutableLiveData for the data to be exposed
    private var errorString: MutableLiveData<Event<String>> = MutableLiveData()
    private var responseSignUp: MutableLiveData<Event<ResponseSignUp>> = MutableLiveData()
    private var responseLogin: MutableLiveData<Event<ResponseLogin>> = MutableLiveData()
    private var responseCreateProfile: MutableLiveData<Event<ResponseCreateProfile>> = MutableLiveData()
    private var responseUpdateProfile: MutableLiveData<Event<ResponseUpdateProfile>> = MutableLiveData()
    private var responsecheckSignUp: MutableLiveData<Event<ResponseSignUp>> = MutableLiveData()

    //LiveData for all exposed data
    fun errorString(): LiveData<Event<String>> = errorString
    fun responseSignUp(): LiveData<Event<ResponseSignUp>> = responseSignUp
    fun responseLogin(): LiveData<Event<ResponseLogin>> = responseLogin
    fun responseCreateProfile(): LiveData<Event<ResponseCreateProfile>> = responseCreateProfile
    fun responseUpdateProfile(): LiveData<Event<ResponseUpdateProfile>> = responseUpdateProfile
    fun responsecheckSignUp(): LiveData<Event<ResponseSignUp>> = responsecheckSignUp

    //Functions based on *Local Data*
    fun getLoginStatus() = profileRepository.getLoginStatus()
    fun setLoginStatus(loginStatus: Boolean) = profileRepository.setLoginStatus(loginStatus)
    fun getLocalProfile() = profileRepository.getLocalProfile()
    fun setLocalProfile(bodyModel: ProfileModel) = profileRepository.setLocalProfile(bodyModel)
    fun getToken() = profileRepository.getToken()
    fun setToken(token: String) = profileRepository.setToken(token)
    private fun setUserId(id: String) = profileRepository.setUserId(id)


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
                handleError(e)
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
                handleError(e)
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
                    setUserId(response.userID!!)
                    Log.d(TAG, "logIn: " + response.token)
                    //TODO: Fetch Profile and save to SharedPreference using setLocalProfile()
                    if (response.profileCompleted!!) {
                        val bodyProfile = response.profile
                        setLocalProfile(bodyProfile!!)
                    }
                }
                responseLogin.postValue(Event(response))
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun createProfile(bodyCreateProfile: BodyCreateProfile) {
        viewModelScope.launch {
            try {
                val token = profileRepository.getToken()
                val userID = profileRepository.getUserId()
                bodyCreateProfile.userID = userID
                Log.d(TAG, "createProfile: $token")
                responseCreateProfile.postValue(Event(profileRepository.createProfile("Bearer $token", bodyCreateProfile)))
                setLocalProfile(ProfileModel(bodyCreateProfile))
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun updateProfile(bodyUpdateProfile: BodyUpdateProfile) {
        viewModelScope.launch {
            try {
                val token = profileRepository.getToken()
                val userID = profileRepository.getUserId()
                bodyUpdateProfile.userID = userID
                responseUpdateProfile.postValue(Event(profileRepository.updateProfile("Bearer $token", bodyUpdateProfile)))
                setLocalProfile(ProfileModel(bodyUpdateProfile))
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun handleError(e: Exception) {
        e.printStackTrace()
        errorString.postValue(Event(e.localizedMessage))
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }
}
