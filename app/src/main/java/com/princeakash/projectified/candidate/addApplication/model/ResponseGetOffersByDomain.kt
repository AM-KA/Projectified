package com.princeakash.projectified.candidate.addApplication.model

import com.princeakash.projectified.candidate.myApplications.model.OfferCardModelCandidate

data class ResponseGetOffersByDomain(val message: String, val offers: ArrayList<OfferCardModelCandidate>)