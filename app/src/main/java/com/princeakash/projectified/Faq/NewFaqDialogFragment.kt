package com.princeakash.projectified.Faq

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.princeakash.projectified.R
import kotlinx.android.synthetic.main.dialog_new_faq.view.*

class NewFaqDialogFragment: DialogFragment() {
    var editText: EditText? = null
    var listener: NewFaqDialogListener? = null
    val TAG = "NewFaqDialogFragment"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            listener = context as NewFaqDialogListener
        }catch (e: ClassCastException){
            Log.d(TAG, "onAttach: NewFaqDialogListener must be implemented")
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_new_faq, null)
        editText = view.findViewById(R.id.editTextNewFaq)
        builder
                .setTitle("Submit New Question")
                .setView(view)
                .setPositiveButton("Submit", DialogInterface.OnClickListener { dialog, which ->
                    if(editText!!.text.isNullOrEmpty()){
                        editText!!.error = "Enter a valid question"
                        return@OnClickListener
                    }
                    listener!!.onDialogPositiveClick(this, question = editText!!.text.toString())
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> listener!!.onDialogNegativeClick(this) })
        return builder.create()
    }
    interface NewFaqDialogListener{
        fun onDialogPositiveClick(dialogFragment: DialogFragment, question: String)
        fun onDialogNegativeClick(dialogFragment: DialogFragment)
    }
}