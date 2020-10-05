package com.princeakash.projectified.Faq

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.princeakash.projectified.Event
import com.princeakash.projectified.MyApplication
import kotlinx.coroutines.launch
import kotlin.Exception

class FaqViewModel(val app: Application): AndroidViewModel(app) {

    //
    val faqRepository = (app as MyApplication).faqRepository

    //LiveData
    var responseGetFaq: MutableLiveData<Event<ResponseGetFaq>> = MutableLiveData()
    var responseAddQuestion: MutableLiveData<Event<ResponseAddQuestion>> = MutableLiveData()
    var errorString: MutableLiveData<String> = MutableLiveData()

    fun getAllFaq(){
        viewModelScope.launch {
            try{
                responseGetFaq.postValue(Event(faqRepository.getAllFaq()));
            } catch(e: Exception){
                errorString.postValue(e.localizedMessage)
            }
        }
    }

    fun addQuestion(question: String){
        viewModelScope.launch {
            try{
                val bodyAddQuestion = BodyAddQuestion(question)
                responseAddQuestion.postValue(Event(faqRepository.addQuestion(bodyAddQuestion)))
            }catch (e: Exception){
                errorString.postValue(e.localizedMessage)
            }
        }
    }
}