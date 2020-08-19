package com.princeakash.projectified.recruiter

data class ResponseGetApplicationByIdRecruiter(
        var markAsSeen: Boolean,
        var markAsSelected: Boolean,
        var applicant_name: String,
        var applicant_collegeName: String,
        var applicant_course: String,
        var applicant_semester: String,
        var applicant_phone: String,
        var previousWork: String,
        var resume: String
)