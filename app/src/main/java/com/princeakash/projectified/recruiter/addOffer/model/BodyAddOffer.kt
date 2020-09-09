package com.princeakash.projectified.recruiter.addOffer.model

import java.util.*

data class BodyAddOffer(
       var   offer_name: String,
       var  domain_name: String,
       var  requirements: String,
       var  skills: String,
       var  expectation: String,
       var  recruiter_id: String
)