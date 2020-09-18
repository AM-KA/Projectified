package com.princeakash.projectified.candidate.addApplication.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.R
import com.princeakash.projectified.candidate.myApplications.model.OfferCardModelCandidate
import kotlinx.android.synthetic.main.card_offer_candidate_version.view.*

class GetOffersByDomainAdapter(val offerList: List<OfferCardModelCandidate>, val listener:GetOffersListener): RecyclerView.Adapter<GetOffersByDomainAdapter.GetOfferViewHolder>() {

    class GetOfferViewHolder(itemView: View, listener: GetOffersListener):RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val textViewPost = itemView.textViewPost
        val textViewDate = itemView.textViewDate
        val textViewCollege = itemView.textViewCollege
        val textViewSkills = itemView.textViewSkills
        val buttonViewDetails = itemView.button
        val myListener = listener

        init{
            buttonViewDetails.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            myListener.onViewDetailsClick(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GetOfferViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_offer_candidate_version, parent, false)
        return GetOfferViewHolder(v, listener)
    }

    override fun onBindViewHolder(holder: GetOfferViewHolder, position: Int) {
        holder.textViewPost.text = offerList?.get(position)?.offer_name
        holder.textViewDate.text = offerList?.get(position)?.float_date
        holder.textViewCollege.text = offerList?.get(position)?.collegeName
        holder.textViewSkills.text = offerList?.get(position)?.skills
    }

    override fun getItemCount(): Int = offerList!!.size

    interface GetOffersListener{
        fun onViewDetailsClick(itemPosition: Int)
    }
}