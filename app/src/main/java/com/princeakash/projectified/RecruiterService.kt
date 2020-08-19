package com.princeakash.projectified

import kotlinx.coroutines.Deferred
import retrofit2.http.*


interface RecruiterService {

    @POST("offer")
    fun addOffer(@Header("Authorization") token: String, @Body bodyAddOffer:BodyAddOffer) : Deferred<ResponseAddOffer>

    @GET("byRecruiter/{recruiterID}")
    fun getOffersByRecruiter(@Header("Authorization") token: String, @Path("recruiterID") recruiterID:String) : Deferred<ResponseGetOffersByRecruiter>

    @GET("byIdRecruiter/{offerID}")
    fun getOfferByIdRecruiter(@Header("Authorization") token: String, @Path("offerID") offerID:String) : Deferred<ResponseGetOfferByIdRecruiter>

    @GET("{offerID}/getApplicants")
    fun getOfferApplicants(@Header("Authorization") token: String, @Path("offerID") offerID:String) : Deferred<ResponseGetOfferApplicants>

    @PATCH("{offerID}")
    fun updateOffer(@Header("Authorization") token: String, @Path("offerID") offerID: String, @Body bodyUpdateOffer: BodyUpdateOffer): Deferred<ResponseUpdateOffer>

    @POST("toggle/{offerID}")
    fun toggleVisibility(@Header("Authorization") token: String, @Path("offerID") offerID: String, @Body bodyToggleVisibility: BodyToggleVisibility) : Deferred<ResponseToggleVisibility>

    @DELETE("{offerID}")
    fun deleteOffer(@Header("Authorization") token: String, @Path("offerID") offerID:String): Deferred<ResponseDeleteOffer>

    @GET("byIdRecruiter/{applicationID}")
    fun getApplicationById(@Header("Authorization") token: String, @Path("applicationID") applicationID:String): Deferred<ResponseGetApplicationDetailById>

    @PATCH("markSeen/{applicationID}")
    fun markSeen(@Header("Authorization") token: String, @Path("applicationID") applicationID: String, @Body bodyMarkAsSeen: BodyMarkAsSeen): Deferred<ResponseMarkAsSeen>

    @PATCH("markSelected/{applicationID}")
    fun markSelected(@Header("Authorization") token: String, @Path("applicationID") applicationID: String, @Body bodyMarkAsSelected: BodyMarkAsSelected): Deferred<ResponseMarkAsSelected>

}