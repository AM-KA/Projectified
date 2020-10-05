package com.princeakash.projectified.candidate.myApplications.model

data class ResponseGetApplicationDetailByIdCandidate (
        var code: Int,
        var message : String,
        var application: ApplicationCandidateVersion?
)

