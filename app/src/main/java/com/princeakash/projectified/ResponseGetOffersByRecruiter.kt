package com.princeakash.projectified

data class ResponseGetOffersByRecruiter(
        var message:String,
        var offers:List<OfferCardModelRecruiter>
)