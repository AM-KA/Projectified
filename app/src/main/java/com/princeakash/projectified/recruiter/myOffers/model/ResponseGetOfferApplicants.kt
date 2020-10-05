package com.princeakash.projectified.recruiter.myOffers.model

data class ResponseGetOfferApplicants (
        var code: Int,
        var message: String,
        var applicants: List<ApplicantCardModel>?
)