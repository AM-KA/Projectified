package com.princeakash.projectified.candidate.myApplications.model


data class ResponseGetApplicationsByCandidate(
        var code: Int,
        var message:String,
        var applications:List<ApplicationCardModelCandidate>?
)