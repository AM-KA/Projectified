package com.princeakash.projectified.user

data class ResponseLogin(

        var message: String,
        var userID: String,
        var token: String,
        var code: Int
)