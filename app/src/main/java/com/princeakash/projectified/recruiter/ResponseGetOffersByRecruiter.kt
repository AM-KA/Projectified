package com.princeakash.projectified.recruiter

data class ResponseGetOffersByRecruiter(
        var message:String,
        var offers:List<OfferCardModelRecruiter>
)