package com.princeakash.projectified.recruiter

data class ResponseGetOfferApplicants (
        var message:String,
        var applicants:List<ApplicantCardModel>)