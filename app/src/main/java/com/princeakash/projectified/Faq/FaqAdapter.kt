package com.princeakash.projectified.Faq

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.R
import kotlinx.android.synthetic.main.card_faq.view.*

private const val TAG = "FaqAdapter"
class FaqAdapter(val faqList: List<FaqModel>): RecyclerView.Adapter<FaqAdapter.FaqViewHolder>() {
    class FaqViewHolder(val itemView: View): RecyclerView.ViewHolder(itemView){
        val textViewQuestion: TextView = itemView.findViewById(R.id.textViewQuestion)
        val textViewAnswer: TextView = itemView.findViewById(R.id.textViewAnswer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_faq, parent, false);
        return FaqViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        holder.textViewQuestion.text = faqList[position].question
        holder.textViewAnswer.text = faqList[position].answer
    }

    override fun getItemCount() = faqList.size
}