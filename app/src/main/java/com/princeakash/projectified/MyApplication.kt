package com.princeakash.projectified

import android.app.Application
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.lifecycle.Observer
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

    fun showToast(message: String?){
        Toast.makeText(this, message, LENGTH_SHORT).show()
    }

    companion object{
        val BASE_URL = "http://am-ka.com"
    }
}