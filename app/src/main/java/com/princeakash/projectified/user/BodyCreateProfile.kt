package com.princeakash.projectified.user

import java.lang.reflect.Array
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

data class BodyCreateProfile
(
        var collegeName:String,
        var course:String,
        var semester:String,
        var languages:IntArray,
        var interest1:String,
        var interest2:String?,
        var interest3:String?,
        var description:String
)