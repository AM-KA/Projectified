package com.princeakash.projectified.Faq

data class ResponseGetFaq(
        val code: Int,
        val message: String,
        val faqList: List<FaqModel>
)