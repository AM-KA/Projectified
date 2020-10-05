package com.princeakash.projectified.recruiter.myOffers.model

data class ResponseGetApplicationByIdRecruiter(
        var code: Int,
        var message: String,
        var application: ApplicationModel?
)

data class ApplicationModel(
        var is_Seen: Boolean,
        var is_Selected: Boolean,
        var applicant_name: String,
        var applicant_collegeName: String,
        var applicant_course: String,
        var applicant_semester: String,
        var applicant_phone: String,
        var previousWork: String,
        var resume: String
)