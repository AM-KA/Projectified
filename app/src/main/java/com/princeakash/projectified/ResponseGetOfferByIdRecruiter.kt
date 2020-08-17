package com.princeakash.projectified

data class ResponseGetOfferByIdRecruiter(
        var message:String,
        var offer: Offer
)
{
    data class Offer(
            var offer_id:String,
            var requirements:String,
            var skills:String,
            var expectation: String,
            var is_visible:Boolean
    )
}