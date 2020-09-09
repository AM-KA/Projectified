package com.princeakash.projectified.candidate.addApplication.model

import com.princeakash.projectified.candidate.myApplications.model.OfferCandidate

data class ResponseGetOfferById (val message: String, val offer: OfferCandidate)