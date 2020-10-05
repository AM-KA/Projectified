package com.princeakash.projectified.candidate.addApplication.model

import com.princeakash.projectified.candidate.myApplications.model.OfferCardModelCandidate

data class ResponseGetOffersByDomain(
        val code: String,
        val message: String,
        val offers: List<OfferCardModelCandidate>?
)