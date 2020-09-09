package com.princeakash.projectified.candidate

import com.princeakash.projectified.candidate.addApplication.model.BodyAddApplication
import com.princeakash.projectified.candidate.addApplication.model.ResponseAddApplication
import com.princeakash.projectified.candidate.addApplication.model.ResponseGetOffersByDomain
import com.princeakash.projectified.candidate.myApplications.model.*
import retrofit2.http.*
import com.princeakash.projectified.candidate.addApplication.model.ResponseGetOfferById as ResponseGetOfferById1

interface CandidateService {

    // ADD Application
    @POST("application")
    suspend  fun addApplication(@Header("Authorization") token: String, @Body bodyAddApplication: BodyAddApplication) : ResponseAddApplication


    // Get all Applications by ApplicantID
    @GET("byApplicant/{applicantID}")
    suspend  fun getApplicationsByCandidate(@Header("Authorization") token: String, @Path("applicantID") applicantID:String) : ResponseGetApplicationsByCandidate

    // Get info about specific application by ApplicationID
    @GET("{applicationID}")
    suspend fun getApplicationByIdCandidate(@Header("Authorization") token: String, @Path("applicationID") applicationID:String) : ResponseGetApplicationDetailByIdCandidate


    //Update any offer through ApplicationId
    @PATCH("{applicationID}")
    suspend  fun  updateApplication(@Header("Authorization") token: String, @Path("applicationID") applicationID:String, @Body bodyUpdateApplication: BodyUpdateApplication): ResponseUpdateApplication


    // Delete any Application through ApplicationID
    @DELETE("{applicationID}")
    suspend  fun deleteApplication(@Header("Authorization") token: String, @Path("applicationID") applicationID: String): ResponseDeleteApplication


    // Get info about all offers through domain name
    @GET("byDomain/{domainName}")
    suspend  fun  getOffersByDomain(@Header("Authorization") token: String, @Path("domainName") domainName:String): ResponseGetOffersByDomain


    // Get Info about specific offer through offerID
    @GET("{offerID}")
    suspend  fun getOfferById(@Header("Authorization") token: String, @Path("offerID") offerID:String): ResponseGetOfferById1


}



