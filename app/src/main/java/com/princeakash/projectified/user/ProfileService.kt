package com.princeakash.projectified.user

import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ProfileService {

    @POST("profile")
    suspend fun createProfile(@Header("Authorization") token: String, @Body bodyCreateProfile: BodyCreateProfile) :ResponseCreateProfile


    @PATCH("{profileID}")
    suspend fun updateProfile(@Header("Authorization") token: String, @Body bodyUpdateProfile: BodyUpdateProfile) :  ResponseUpdateProfile



}