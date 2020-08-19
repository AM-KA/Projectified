package com.princeakash.projectified

data class ResponseLogin(

        var message: String,
        var userID: String,
        var token: String,
        var code: Int
)