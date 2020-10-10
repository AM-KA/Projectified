package com.princeakash.projectified.Faq

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.princeakash.projectified.Event
import com.princeakash.projectified.MyApplication
import kotlinx.coroutines.launch
import kotlin.Exception

class FaqViewModel(val app: Application): AndroidViewModel(app) {

    //FaqRepository
    val faqRepository = (app as MyApplication).faqRepository

    //MutableLiveData
    private var responseGetFaq: MutableLiveData<ResponseGetFaq> = MutableLiveData()
    private var responseAddQuestion: MutableLiveData<Event<ResponseAddQuestion>> = MutableLiveData()
    private var errorString: MutableLiveData<Event<String>> = MutableLiveData()

    //LiveData
    fun responseGetFaq(): LiveData<ResponseGetFaq> = responseGetFaq
    fun responseAddQuestion():LiveData<Event<ResponseAddQuestion>> = responseAddQuestion
    fun errorString(): LiveData<Event<String>> = errorString

    fun getAllFaq(){
        viewModelScope.launch {
            try{
                responseGetFaq.postValue(faqRepository.getAllFaq());
            } catch(e: Exception){
                errorString.postValue(Event(e.localizedMessage))
            }
        }
    }

    fun addQuestion(question: String){
        viewModelScope.launch {
            try{
                val bodyAddQuestion = BodyAddQuestion(question)
                responseAddQuestion.postValue(Event(faqRepository.addQuestion(bodyAddQuestion)))
            }catch (e: Exception){
                errorString.postValue(Event(e.localizedMessage))
            }
        }
    }
}