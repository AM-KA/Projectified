package com.princeakash.projectified.recruiter.myOffers.model

data class ResponseGetOffersByRecruiter(
        var message:String,
        var offers:List<OfferCardModelRecruiter>
)