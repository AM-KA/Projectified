package com.princeakash.projectified

import android.app.Application
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.google.firebase.FirebaseApp
import com.princeakash.projectified.Faq.FaqRepository
import com.princeakash.projectified.candidate.CandidateRepository
import com.princeakash.projectified.recruiter.RecruiterRepository
import com.princeakash.projectified.user.ProfileRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*


class MyApplication: Application(){

    //Moshi: GSON of Kotlin
    var moshi = Moshi.Builder()
                //.add(Date::class.java, Rfc3339DateJsonAdapter())
                //.add(CustomDateAdapter())
                //.add(KotlinJsonAdapterFactory())
                .build()

    var retrofit = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl(BASE_URL)
                .build()

    lateinit var recruiterRepository:RecruiterRepository
    lateinit var profileRepository: ProfileRepository
    lateinit var candidateRepository:CandidateRepository
    lateinit var faqRepository:FaqRepository

    //Show Toast by using Application Context
    fun showToast(message: String?){
        Toast.makeText(this, message, LENGTH_SHORT).show()
    }

    //Storing static values
    companion object{
        val BASE_URL = "https://am-ka-projectified-test.herokuapp.com/"
    }

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this);
        moshi = Moshi.Builder()
                .add(CustomDateAdapter())
                .add(KotlinJsonAdapterFactory())
                .build()
        retrofit = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl(BASE_URL)
                .build()

        recruiterRepository = RecruiterRepository(retrofit)
        profileRepository = ProfileRepository(retrofit, this)
        candidateRepository = CandidateRepository(retrofit)
        faqRepository = FaqRepository(retrofit)




        }
}