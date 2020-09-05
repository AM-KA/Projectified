package com.princeakash.projectified.recruiter.myOffers.model

data class ResponseGetOfferApplicants (
        var message:String,
        var applicants:List<ApplicantCardModel>
)