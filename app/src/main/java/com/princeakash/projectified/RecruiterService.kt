package com.princeakash.projectified

import kotlinx.coroutines.Deferred
import retrofit2.http.*


interface RecruiterService {

    // ADD OFFER
    @POST("offer")
    fun addOffer(@Header("Authorization") token: String, @Body bodyAddOffer:BodyAddOffer) : Deferred<ResponseAddOffer>


    // All Offer of a specific Recruiter through Recruiter Id
    @GET("byRecruiter/{recruiterID}")
    fun getOffersByRecruiter(@Header("Authorization") token: String, @Path("recruiterID") recruiterID:String) : Deferred<ResponseGetOffersByRecruiter>


    // Get all details of specific Offer by offerId
    @GET("byIdRecruiter/{offerID}")
    fun getOfferByIdRecruiter(@Header("Authorization") token: String, @Path("offerID") offerID:String) : Deferred<ResponseGetOfferByIdRecruiter>


    //  Get all applicant of A specific offer through offerId
    @GET("{offerID}/getApplicants")
    fun getOfferApplicants(@Header("Authorization") token: String, @Path("offerID") offerID:String) : Deferred<ResponseGetOfferApplicants>


    //Update any offer through offer Id
    @PATCH("{offerID}")
    fun updateOffer(@Header("Authorization") token: String, @Path("offerID") offerID: String, @Body bodyUpdateOffer: BodyUpdateOffer): Deferred<ResponseUpdateOffer>

    @POST("toggle/{offerID}")
    fun toggleVisibility(@Header("Authorization") token: String, @Path("offerID") offerID: String, @Body bodyToggleVisibility: BodyToggleVisibility) : Deferred<ResponseToggleVisibility>


    // Delete any offer through offerid
    @DELETE("{offerID}")
    fun deleteOffer(@Header("Authorization") token: String, @Path("offerID") offerID:String): Deferred<ResponseDeleteOffer>


    // Get application details By application ID
    @GET("byIdRecruiter/{applicationID}")
    fun getApplicationById(@Header("Authorization") token: String, @Path("applicationID") applicationID:String): Deferred<ResponseGetApplicationDetailById>


    // Mark seen through application ID
    @PATCH("markSeen/{applicationID}")
    fun markSeen(@Header("Authorization") token: String, @Path("applicationID") applicationID: String, @Body bodyMarkAsSeen: BodyMarkAsSeen): Deferred<ResponseMarkAsSeen>


    // Mark selected through application ID
    @PATCH("markSelected/{applicationID}")
    fun markSelected(@Header("Authorization") token: String, @Path("applicationID") applicationID: String, @Body bodyMarkAsSelected: BodyMarkAsSelected): Deferred<ResponseMarkAsSelected>

}