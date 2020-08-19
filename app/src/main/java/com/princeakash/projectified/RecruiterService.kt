package com.princeakash.projectified

import kotlinx.coroutines.Deferred
import retrofit2.http.*


interface RecruiterService {

    @POST("offer")
    suspend fun addOffer(@Header("Authorization") token: String, @Body bodyAddOffer:BodyAddOffer) : ResponseAddOffer

    @GET("offer/recruiter/{recruiterID}")
    suspend fun getOffersByRecruiter(@Header("Authorization") token: String, @Path("recruiterID") recruiterID:String) : ResponseGetOffersByRecruiter

    @GET("offer/{offerID}/recruiter")
    suspend fun getOfferByIdRecruiter(@Header("Authorization") token: String, @Path("offerID") offerID:String) : ResponseGetOfferByIdRecruiter

    @GET("offer/{offerID}/applicants")
    suspend fun getOfferApplicants(@Header("Authorization") token: String, @Path("offerID") offerID:String) : ResponseGetOfferApplicants

    @PATCH("offer/{offerID}")
    suspend fun updateOffer(@Header("Authorization") token: String, @Path("offerID") offerID: String, @Body bodyUpdateOffer: BodyUpdateOffer): ResponseUpdateOffer

    @POST("offer/{offerID}/toggle")
    suspend fun toggleVisibility(@Header("Authorization") token: String, @Path("offerID") offerID: String, @Body bodyToggleVisibility: BodyToggleVisibility) : ResponseToggleVisibility

    @DELETE("offer/{offerID}")
    suspend fun deleteOffer(@Header("Authorization") token: String, @Path("offerID") offerID:String): ResponseDeleteOffer

    @GET("application/{applicationID}/recruiter")
    suspend fun getApplicationById(@Header("Authorization") token: String, @Path("applicationID") applicationID:String): ResponseGetApplicationByIdRecruiter

    @PATCH("application/{applicationID}/seen")
    suspend fun markSeen(@Header("Authorization") token: String, @Path("applicationID") applicationID: String, @Body bodyMarkAsSeen: BodyMarkAsSeen): ResponseMarkAsSeen

    @PATCH("application/{applicationID}/selected")
    suspend fun markSelected(@Header("Authorization") token: String, @Path("applicationID") applicationID: String, @Body bodyMarkAsSelected: BodyMarkAsSelected): ResponseMarkAsSelected

}