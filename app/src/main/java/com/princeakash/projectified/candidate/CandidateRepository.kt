
package com.princeakash.projectified.candidate

import com.princeakash.projectified.recruiter.BodyAddOffer
import retrofit2.Retrofit

//TODO:Show exception via toast
class CandidateRepository(retrofit: Retrofit) {
    var candidateService: CandidateService = retrofit.create(CandidateService::class.java)

    suspend fun addApplication(token: String, bodyAddOffer: BodyAddOffer) = candidateService.addApplication(token, bodyAddOffer)

    suspend fun getApplicationByCandidate(token: String, applicantID: String) = candidateService.getApplicationsByCandidate(token, applicantID)

    suspend fun getApplicationByIdCandidate(token: String, applicationID: String) = candidateService.getApplicationByIdCandidate(token, applicationID)

    suspend fun updateApplication(token: String, applicationID: String, bodyUpdateApplication: BodyUpdateApplication) = candidateService.updateApplication(token, applicationID, bodyUpdateApplication)

    suspend fun deleteApplication(token: String, applicationID: String) = candidateService.deleteApplication(token, applicationID)

    suspend fun getOffersByDomain(token: String, domainName: String) = candidateService.getOffersByDomain(token, domainName)

    suspend fun getOfferById(token: String, offerId: String) = candidateService.getOfferById(token, offerId)


}