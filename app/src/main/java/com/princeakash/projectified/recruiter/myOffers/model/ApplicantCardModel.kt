package com.princeakash.projectified.recruiter.myOffers.model

import java.util.*

data class ApplicantCardModel (
        var application_id:String,
        var collegeName:String,
        var is_Seen:Boolean,
        var is_Selected:Boolean,
        var date:Date,
        var applicant_id:String
)