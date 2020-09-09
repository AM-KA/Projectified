package com.princeakash.projectified.Faq

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.VERTICAL
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.princeakash.projectified.R
import kotlinx.android.synthetic.main.fragment_all_faq.view.*

class AllFaqFragment : Fragment(), NewFaqDialogFragment.NewFaqDialogListener {

    //var recyclerViewFaq: RecyclerView? = null
    var adapter: FaqAdapter? = null

    var faqViewModel: FaqViewModel? = null
    var faqList: ArrayList<FaqModel> = ArrayList()
    var responseAddQuestion: ResponseAddQuestion? = null
    var error: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        faqViewModel = ViewModelProvider(requireActivity()).get(FaqViewModel::class.java)
        faqViewModel!!.responseGetFaq.observe(this, {
            faqList = it.faqList
            adapter?.notifyDataSetChanged()
        })
        faqViewModel!!.responseAddQuestion.observe(this, {
            responseAddQuestion = it
            Toast.makeText(context, responseAddQuestion!!.message, LENGTH_LONG).show()
        })
        faqViewModel!!.errorString.observe(this, {
            error = it;
            Toast.makeText(context, error, LENGTH_LONG).show()
        })
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_faq, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        faqViewModel!!.getAllFaq()
        view.recyclerViewFaq.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
            adapter = FaqAdapter(faqList)
        }
        view.fab.setOnClickListener{
            //New Dialog Box
            val dialogFragment = NewFaqDialogFragment()
            dialogFragment.show(parentFragmentManager, "NewFaqDialogFragment")
        }
    }

    override fun onDialogPositiveClick(dialogFragment: DialogFragment, question: String) {
        faqViewModel!!.addQuestion(question)
    }

    override fun onDialogNegativeClick(dialogFragment: DialogFragment) {
        dialogFragment.dismiss()
    }
}