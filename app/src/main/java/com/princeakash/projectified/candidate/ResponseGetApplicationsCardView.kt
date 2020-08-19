package com.princeakash.projectified.candidate

import com.princeakash.projectified.candidate.ApplicationCardModelCandidate


data class ResponseGetApplicationsCardView(
        var message:String,
        var Application:List<ApplicationCardModelCandidate>
)