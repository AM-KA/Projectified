package com.princeakash.projectified.Faq

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.princeakash.projectified.R
import kotlinx.android.synthetic.main.fragment_all_faq.view.*

class FaqActivity : AppCompatActivity(), NewFaqDialogFragment.NewFaqDialogListener {
    //var recyclerViewFaq: RecyclerView? = null
    var adapter: FaqAdapter? = null

    var faqViewModel: FaqViewModel? = null
    //var faqList: List<FaqModel> = ArrayList()
    lateinit var recyclerViewFaq: RecyclerView
    lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_all_faq)
        recyclerViewFaq = findViewById(R.id.recyclerViewFaq)
        fab = findViewById(R.id.fab)
        window?.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        faqViewModel = ViewModelProvider(this).get(FaqViewModel::class.java)
        faqViewModel!!.responseGetFaq().observe(this, {
                //Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                adapter = FaqAdapter(it.faqList)
                recyclerViewFaq.adapter = adapter
        })
        faqViewModel!!.responseAddQuestion().observe(this, {
            it?.getContentIfNotHandled()?.let {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
        })
        faqViewModel!!.errorString().observe(this, {
            it?.getContentIfNotHandled()?.let{
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })

        if(savedInstanceState==null)
            faqViewModel!!.getAllFaq()

        recyclerViewFaq.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
        fab.setOnClickListener{
            //New Dialog Box
            val dialogFragment = NewFaqDialogFragment()
            dialogFragment.show(supportFragmentManager, "NewFaqDialogFragment")
        }
    }

    override fun onDialogPositiveClick(dialogFragment: DialogFragment, question: String) {
        faqViewModel!!.addQuestion(question)
    }

    override fun onDialogNegativeClick(dialogFragment: DialogFragment) {
        dialogFragment.dismiss()
    }
}