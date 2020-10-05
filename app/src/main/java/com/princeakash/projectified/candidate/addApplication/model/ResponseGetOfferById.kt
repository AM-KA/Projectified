package com.princeakash.projectified.candidate.addApplication.model

import com.princeakash.projectified.candidate.myApplications.model.OfferCandidate

data class ResponseGetOfferById(
        val code: Int,
        val message: String,
        val offer: OfferCandidate
)