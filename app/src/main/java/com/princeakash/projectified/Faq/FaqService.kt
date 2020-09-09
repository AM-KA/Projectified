package com.princeakash.projectified.Faq

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FaqService {

    //Fetch all FAQs
    @GET("faq")
    suspend fun getAllFaq(): ResponseGetFaq

    //Post a new question
    @POST("faq")
    suspend fun addQuestion(@Body bodyAddQuestion: BodyAddQuestion) : ResponseAddQuestion
}