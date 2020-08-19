package com.princeakash.projectified.user

data class BodyCreateProfile (
        var name:String,
        var collegeName:String,
        var semester:String,
        var languages:String,
        var interest1:String,
        var interest2:String?,
        var interest3:String?,
        var description:String
)