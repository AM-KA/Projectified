package com.princeakash.projectified.recruiter.myOffers.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.R
import com.princeakash.projectified.databinding.CardMyOffersCandidateBinding

class MyOfferApplicantsAdapter(private var applicantList: List<ApplicantCardModel>?, private val listener: MyOfferApplicantListener) : RecyclerView.Adapter<MyOfferApplicantsAdapter.MyOfferApplicantViewHolder>(){

    class MyOfferApplicantViewHolder(val binding: CardMyOffersCandidateBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOfferApplicantViewHolder {
        val binding = CardMyOffersCandidateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyOfferApplicantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyOfferApplicantViewHolder, position: Int) {
        holder.binding.apply {
            textViewCollege.text = applicantList!!.get(position).collegeName
            textViewDate.text = applicantList!!.get(position).date.toString()
            if(applicantList!!.get(position).is_Seen)
                imageViewSeen.setImageResource(R.drawable.ic_baseline_favorite_24)
            if(applicantList!!.get(position).is_Selected)
                imageViewSelected.setImageResource(R.drawable.ic_baseline_done_24)

            imageViewSeen.setOnClickListener { listener.onSeenClick(position) }
            imageViewSelected.setOnClickListener { listener.onSelectedClick(position) }
            root.setOnClickListener { listener.onViewDetailsClick(position) }
        }
    }

    override fun getItemCount() = applicantList!!.size

    interface MyOfferApplicantListener{
        fun onViewDetailsClick(itemPosition: Int)
        fun onSeenClick(itemPosition: Int)
        fun onSelectedClick(itemPosition: Int)
    }
}