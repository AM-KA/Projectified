package com.princeakash.projectified.recruiter.myOffers.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.R
import kotlinx.android.synthetic.main.card_my_offers_candidate.view.*

class MyOfferApplicantsAdapter(var applicantList: List<ApplicantCardModel>?, val listener: MyOfferApplicantListener) : RecyclerView.Adapter<MyOfferApplicantsAdapter.MyOfferApplicantViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOfferApplicantViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_my_offers_candidate, parent, false)
        return MyOfferApplicantViewHolder(v, listener)
    }

    override fun onBindViewHolder(holder: MyOfferApplicantViewHolder, position: Int) {
        holder.textViewCollege.text = applicantList!!.get(position).college_name
        holder.textViewDate.text = applicantList!!.get(position).date
        if(applicantList!!.get(position).is_Seen)
            holder.imageViewSeen.setImageResource(R.drawable.ic_baseline_favorite_24)
        if(applicantList!!.get(position).is_Selected)
            holder.imageViewSelected.setImageResource(R.drawable.ic_baseline_done_24)
    }

    override fun getItemCount() = applicantList!!.size

    class MyOfferApplicantViewHolder(itemView: View, listener: MyOfferApplicantListener) : RecyclerView.ViewHolder(itemView){
        val textViewCollege = itemView.textViewCollege
        val textViewDate = itemView.textViewDate
        val imageViewSeen = itemView.imageViewSeen
        val imageViewSelected = itemView.imageViewSelected
        val button = itemView.button
        val myListener = listener
        init {
            imageViewSeen.setOnClickListener {
                myListener.onSeenClick(adapterPosition)
            }

            imageViewSelected.setOnClickListener{
                myListener.onSelectedClick(adapterPosition)
            }

            button.setOnClickListener{
                myListener.onViewDetailsClick(adapterPosition)
            }
        }
    }
    interface MyOfferApplicantListener{
        fun onViewDetailsClick(itemPosition: Int)
        fun onSeenClick(itemPosition: Int)
        fun onSelectedClick(itemPosition: Int)
    }
}