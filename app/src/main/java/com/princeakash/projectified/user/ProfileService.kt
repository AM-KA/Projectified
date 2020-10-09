package com.princeakash.projectified.user

import retrofit2.http.*

interface ProfileService {

    @POST("user/signup")
    suspend fun signUp(@Body bodySignUp: BodySignUp): ResponseSignUp

    @POST("user/login")
    suspend fun logIn(@Body bodyLogin: LoginBody): ResponseLogin

    @POST("profile")
    suspend fun createProfile(@Header("Authorization") token: String, @Body bodyCreateProfile: BodyCreateProfile) :ResponseCreateProfile

    @PATCH("profile/{profileID}")
    suspend fun updateProfile(@Header("Authorization") token: String, @Body bodyUpdateProfile: BodyUpdateProfile, @Path("profileID") profileID:String) :  ResponseUpdateProfile

    @POST("user/checksignup")
    suspend fun checksignup(@Body bodySignUp: BodySignUp) : ResponseSignUp
}