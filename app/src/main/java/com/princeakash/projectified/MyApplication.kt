package com.princeakash.projectified

import android.app.Application
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.princeakash.projectified.recruiter.RecruiterRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MyApplication(): Application(){

    //Moshi: GSON of Kotlin
    val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

    val retrofit = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl(BASE_URL)
                .build()

    var recruiterRepository = RecruiterRepository(retrofit)

    //Show Toast by using Application Context
    fun showToast(message: String?){
        Toast.makeText(this, message, LENGTH_SHORT).show()
    }

    //Storing static values
    companion object{
        val BASE_URL = "http://am-ka.com"
    }
}