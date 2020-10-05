package com.princeakash.projectified.user

data class ResponseLogin(
        var code: Int,
        var message: String,
        var userID: String?,
        var token: String?,
        var userName: String?,
        var profileCompleted: Boolean?,
        var profile: ProfileModel?
)