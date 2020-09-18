package com.princeakash.projectified.user

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.princeakash.projectified.MyApplication
import com.squareup.moshi.JsonAdapter
import retrofit2.Retrofit
import kotlin.Exception

//TODO:Show exception via toast.
class ProfileRepository(retrofit: Retrofit, app: MyApplication) {

    var profileService: ProfileService = retrofit.create(ProfileService::class.java)
    var application: MyApplication = app
    var sharedPref: SharedPreferences
    var editor: SharedPreferences.Editor

    init{
        sharedPref = application.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        editor = sharedPref.edit()
    }

    suspend fun signUp(bodySignUp: BodySignUp) = profileService.signUp(bodySignUp)

    suspend fun logIn(bodyLogin: LoginBody) = profileService.logIn(bodyLogin)

    suspend fun createProfile(token: String,bodyCreateProfile: BodyCreateProfile)  = profileService.createProfile(token, bodyCreateProfile)

    suspend fun updateProfile(token: String,bodyUpdateProfile: BodyUpdateProfile){
        val profileID = getUserId()
        profileService.updateProfile(token,bodyUpdateProfile, profileID)
    }

    fun getLoginStatus(): Boolean{
        try{
            val loginStatus = sharedPref.getBoolean(LOGIN_STATUS, false)
            return loginStatus
        } catch (e: Exception){
            e.printStackTrace()
            return false
        }
    }

    fun setLoginStatus(loginStatus: Boolean) {
        try{
            editor.putBoolean(LOGIN_STATUS, loginStatus)
            editor.commit()
        } catch(e : Exception){
            e.printStackTrace()
        }
    }

    fun getToken() : String {
        try{
            return sharedPref.getString(USER_TOKEN, "")!!
        } catch (e: Exception){
            e.printStackTrace()
            return ""
        }
    }

    fun setToken(token : String){
        try{
            if(token.equals(""))
                throw NullPointerException()
            editor.putString(USER_TOKEN, token)
            editor.commit()
        } catch(e: Exception){
            e.printStackTrace()
            //TODO:Show "Error in token" Toast
        }
    }

    fun getLocalProfile() : ProfileModel?{
        try{
            val json = sharedPref.getString(USER_PROFILE, null)!!
            val jsonAdapter:JsonAdapter<ProfileModel> = (application).moshi.adapter(ProfileModel::class.java)
            return jsonAdapter.fromJson(json)!!
        } catch(e: Exception){
            e.printStackTrace()
            return null
            //TODO:Show "Error in token" Toast
        }
    }

    fun setLocalProfile(bodyProfile: ProfileModel){
        try{
            val jsonAdapter = (application).moshi.adapter(ProfileModel::class.java)
            val json = jsonAdapter.toJson(bodyProfile)
            editor.putString(USER_PROFILE, json)
            editor.commit()
        } catch (e: Exception){
            e.printStackTrace()
            //TODO:Show "Error in storing profile locally" Toast
        }
    }

    fun getUserId(): String{
        try{
            return sharedPref.getString(USER_ID, null)!!
        } catch(e: Exception){
            e.printStackTrace()
            return ""
            //TODO:Show "Error in token" Toast
        }
    }

    fun setUserId(id: String){
        try{
            if(id.equals(""))
                throw NullPointerException()
            editor.putString(USER_ID, id)
            editor.commit()
        } catch(e: Exception){
            e.printStackTrace()
            //TODO:Show "Error in token" Toast
        }
    }

    companion object{
        val SHARED_PREFS = "SharedPreferences"
        val LOGIN_STATUS = "LoginStatus"
        val USER_ID = "UserId"
        val USER_PROFILE = "UserProfile"
        val USER_TOKEN = "UserToken"
    }
}


