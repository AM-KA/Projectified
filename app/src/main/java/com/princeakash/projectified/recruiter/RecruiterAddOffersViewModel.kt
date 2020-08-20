package com.princeakash.projectified.recruiter

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.princeakash.projectified.MyApplication
import kotlinx.coroutines.launch
import java.lang.Exception

class RecruiterAddOffersViewModel(val app: Application): AndroidViewModel(app) {

    //RecruiterRepository instance, guaranteed to be singular because of being
    //picked up from instance of MyApplication.
    val recruiterRepository: RecruiterRepository = (app as MyApplication).recruiterRepository
    var errorString: MutableLiveData<String> = MutableLiveData()

    //MutableLiveData variables of responses for all kinds of requests handled by RecruiterViewModel
    //which can be put to observation in Activities/Fragments
    var responseAddOffer : MutableLiveData<ResponseAddOffer> = MutableLiveData()

    fun addOffer(token:String, bodyAddOffer: BodyAddOffer){
        viewModelScope.launch {
            try {
                responseAddOffer.postValue(recruiterRepository.addOffer(bodyAddOffer, token))
            } catch(e: Exception){

                //Change the Mutable LiveData so that change can be detected in Fragment/Activity. One extra Observer per ViewModel per Activity
                errorString.postValue("Haha! You got an error!!" + e.localizedMessage)

                //Show Toast with Application Context. No extra Observers.
                (app as MyApplication).showToast(e.localizedMessage)
            }
        }
    }
}