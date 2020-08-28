package com.princeakash.projectified.candidate

import java.util.*

data class BodyAddApplication (
        var apply_date: Date,
        var resume: String,
        var previousWork: String,
        var applicant_id:String,
        var offer_id:String,
        var recruiter_id: String
)