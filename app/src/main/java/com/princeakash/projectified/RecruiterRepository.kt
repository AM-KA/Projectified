package com.princeakash.projectified

import kotlinx.coroutines.CoroutineScope
import retrofit2.Retrofit
import retrofit2.create

//TODO:Show exception via toast.
class RecruiterRepository(retrofit: Retrofit) {
    var recruiterService:RecruiterService = retrofit.create(RecruiterService::class.java)

    suspend fun addOffer(bodyAddOffer: BodyAddOffer, token:String) : ResponseAddOffer?{
        val deferredResponse = recruiterService.addOffer(token, bodyAddOffer)
        try {
            val responseAddOffer = deferredResponse.await()
            return responseAddOffer
        } catch (e: Exception){
            e.printStackTrace()
        }
        return null
    }

    suspend fun getOffersByRecruiter(token:String, recruiterID:String) : ResponseGetOffersByRecruiter?{
        val deferredResponse = recruiterService.getOffersByRecruiter(token, recruiterID)
        try{
            val responseGetOffersByRecruiter = deferredResponse.await()
            return responseGetOffersByRecruiter
        }catch(e:Exception){
            e.printStackTrace()
        }
        return null
    }

    suspend fun getOfferByIdRecruiter(token:String, offerID:String) : ResponseGetOfferByIdRecruiter?{
        val deferredResponse = recruiterService.getOfferByIdRecruiter(token, offerID)
        try{
            val responseGetOfferByIdRecruiter = deferredResponse.await()
            return responseGetOfferByIdRecruiter
        } catch (e : Exception){
            e.printStackTrace()
        }
        return null
    }

    suspend fun getOfferApplicants(token:String, offerID:String) : ResponseGetOfferApplicants?{
        val deferredResponse = recruiterService.getOfferApplicants(token, offerID)
        try{
            val responseGetOfferApplicants = deferredResponse.await()
            return responseGetOfferApplicants
        } catch (e : Exception){
            e.printStackTrace()
        }
        return null
    }

    suspend fun updateOffer(token:String, offerID:String, bodyUpdateOffer:BodyUpdateOffer) : ResponseUpdateOffer?{
        val deferredResponse = recruiterService.updateOffer(token, offerID, bodyUpdateOffer)
        try{
            val responseUpdateOffer = deferredResponse.await()
            return responseUpdateOffer
        } catch (e : Exception){
            e.printStackTrace()
        }
        return null
    }

    suspend fun toggleVisibility(token:String, offerID:String, bodyToggleVisibility: BodyToggleVisibility) : ResponseToggleVisibility?{
        val deferredResponse = recruiterService.toggleVisibility(token, offerID, bodyToggleVisibility)
        try{
            val responseToggleVisibility = deferredResponse.await()
            return responseToggleVisibility
        } catch (e : Exception){
            e.printStackTrace()
        }
        return null
    }

    suspend fun deleteOffer(token:String, offerID:String) : ResponseDeleteOffer?{
        val deferredResponse = recruiterService.deleteOffer(token, offerID)
        try{
            val responseDeleteOffer = deferredResponse.await()
            return responseDeleteOffer
        } catch (e : Exception){
            e.printStackTrace()
        }
        return null
    }

    suspend fun getApplicationById(token: String, applicationID:String) : ResponseGetApplicationDetailById?{
        val deferredResponse = recruiterService.getApplicationById(token, applicationID)
        try{
            val responseGetApplicationDetailById = deferredResponse.await()
            return responseGetApplicationDetailById
        } catch (e : Exception){
            e.printStackTrace()
        }
        return null
    }

    suspend fun markSeen(token: String, applicationID:String, bodyMarkAsSeen: BodyMarkAsSeen) : ResponseMarkAsSeen?{
        val deferredResponse = recruiterService.markSeen(token, applicationID, bodyMarkAsSeen)
        try{
            val responseMarkAsSeen = deferredResponse.await()
            return responseMarkAsSeen
        } catch (e : Exception){
            e.printStackTrace()
        }
        return null
    }

    suspend fun markSelected(token: String, applicationID:String, bodyMarkAsSelected: BodyMarkAsSelected) : ResponseMarkAsSelected?{
        val deferredResponse = recruiterService.markSelected(token, applicationID, bodyMarkAsSelected)
        try{
            val responseMarkAsSelected = deferredResponse.await()
            return responseMarkAsSelected
        } catch (e : Exception){
            e.printStackTrace()
        }
        return null
    }
}