package com.princeakash.projectified

import android.app.Application
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MyApplication(): Application(){
    val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
    val retrofit = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl(BASE_URL)
                .build()
        //Job() should be included
    var recruiterRepository = RecruiterRepository(retrofit)

    companion object{
        val BASE_URL = "http://am-ka.com"
    }
}