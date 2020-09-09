package com.princeakash.projectified.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.princeakash.projectified.MyApplication
import kotlinx.coroutines.launch

class ProfileViewModel(app: Application): AndroidViewModel(app){

    //ProfileRepository for all types of Profile-Related Operations
    val profileRepository = (app as MyApplication).profileRepository

    //MutableLiveData for all exposable data
    var errorString: MutableLiveData<String> = MutableLiveData()
    var responseSignUp: MutableLiveData<ResponseSignUp> = MutableLiveData()
    var responseLogin: MutableLiveData<ResponseLogin> = MutableLiveData()

    //Functions based on
    //Local Data
    fun getLoginStatus() = profileRepository.getLoginStatus()
    fun setLoginStatus(loginStatus: Boolean) = profileRepository.setLoginStatus(loginStatus)
    fun getLocalProfile() = profileRepository.getStoredProfile()
    fun setLocalProfile(response: ResponseGetProfileById) = profileRepository.setStoredProfile(response)
    fun getToken() = profileRepository.getToken()
    fun setToken(token: String) = profileRepository.setToken(token)
    fun getUserId() = profileRepository.getUserId()
    fun setUserId(id: String) = profileRepository.setUserId(id)

    //Functions based on
    //Server Data
    fun signUp(bodySignUp: BodySignUp){
        viewModelScope.launch {
            try{
                responseSignUp.postValue(profileRepository.signUp(bodySignUp))
            } catch (e: Exception){
                e.printStackTrace()
                errorString.postValue(e.localizedMessage)
            }
        }
    }

    fun logIn(bodyLogin: LoginBody){
        viewModelScope.launch {
            try{
                val response = profileRepository.logIn(bodyLogin)
                setLoginStatus(true)
                setToken(response.token)
                setUserId(response.userID)
                //TODO: Fetch Profile and save to SharedPreference using setLocalProfile()
                responseLogin.postValue(response)
            } catch(e: Exception){

            }
        }
    }
    fun createProfile(bodyUpdateProfile: BodyUpdateProfile)
       viewModelScope.launch{

    }

}