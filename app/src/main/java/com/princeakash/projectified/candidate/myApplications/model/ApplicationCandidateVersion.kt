package com.princeakash.projectified.candidate.myApplications.model

data class ApplicationCandidateVersion(
        var application_id:String,
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
        var is_seen:Boolean,
        var is_selected:Boolean,
        var resume: String
)