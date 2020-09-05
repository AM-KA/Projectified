package com.princeakash.projectified.candidate.myApplications.model


data class ResponseGetApplicationsByCandidate(
        var message:String,
        var Application:List<ApplicationCardModelCandidate>
)