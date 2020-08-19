package com.princeakash.projectified.candidate

data class ApplicationCandidateVersion(
        var  requirements: String,
        var  skills:String,
        var markAsSeen: Boolean,
        var markAsSelected: String,
        var expectation:String,
        var recruiter_name: String,
        var recruiter_collegeName:String,
        var recruiter_course:String ,
        var recruiter_semester:String,
        var recruiter_phone: String,
        var previousWork: String,
        var resume: String
)