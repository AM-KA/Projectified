package com.princeakash.projectified.recruiter

import com.princeakash.projectified.recruiter.addOffer.model.BodyAddOffer
import com.princeakash.projectified.recruiter.myOffers.model.BodyMarkAsSeen
import com.princeakash.projectified.recruiter.myOffers.model.BodyMarkAsSelected
import com.princeakash.projectified.recruiter.myOffers.model.BodyToggleVisibility
import com.princeakash.projectified.recruiter.myOffers.model.BodyUpdateOffer
import retrofit2.Retrofit

//TODO:Show exception via toast.
class RecruiterRepository(retrofit: Retrofit) {

    //RecruiterService class for using Retrofit
    var recruiterService: RecruiterService = retrofit.create(RecruiterService::class.java)

    suspend fun addOffer(bodyAddOffer: BodyAddOffer, token:String) = recruiterService.addOffer(token, bodyAddOffer)

    suspend fun getOffersByRecruiter(token:String, recruiterID:String) = recruiterService.getOffersByRecruiter(token, recruiterID)

    suspend fun getOfferByIdRecruiter(token:String, offerID:String) = recruiterService.getOfferByIdRecruiter(token, offerID)

    suspend fun getOfferApplicants(token:String, offerID:String) = recruiterService.getOfferApplicants(token, offerID)

    suspend fun updateOffer(token:String, offerID:String, bodyUpdateOffer: BodyUpdateOffer) = recruiterService.updateOffer(token, offerID, bodyUpdateOffer)

    suspend fun toggleVisibility(token:String, offerID:String, bodyToggleVisibility: BodyToggleVisibility) = recruiterService.toggleVisibility(token, offerID, bodyToggleVisibility)

    suspend fun deleteOffer(token:String, offerID:String) = recruiterService.deleteOffer(token, offerID)

    suspend fun getApplicationById(token: String, applicationID:String) = recruiterService.getApplicationById(token, applicationID)

    suspend fun markSeen(token: String, applicationID:String, bodyMarkAsSeen: BodyMarkAsSeen) = recruiterService.markSeen(token, applicationID, bodyMarkAsSeen)

    suspend fun markSelected(token: String, applicationID:String, bodyMarkAsSelected: BodyMarkAsSelected) = recruiterService.markSelected(token, applicationID, bodyMarkAsSelected)
}