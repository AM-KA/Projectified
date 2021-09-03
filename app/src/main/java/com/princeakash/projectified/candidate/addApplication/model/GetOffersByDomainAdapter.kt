package com.princeakash.projectified.candidate.addApplication.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.candidate.myApplications.model.OfferCardModelCandidate
import com.princeakash.projectified.databinding.CardOfferCandidateVersionBinding
import java.text.SimpleDateFormat

class GetOffersByDomainAdapter(private val offerList: List<OfferCardModelCandidate>, private val listener:GetOffersListener): RecyclerView.Adapter<GetOffersByDomainAdapter.GetOfferViewHolder>() {

    class GetOfferViewHolder(val binding: CardOfferCandidateVersionBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GetOfferViewHolder {
        val binding = CardOfferCandidateVersionBinding.inflate(LayoutInflater.from(parent.context))
        return GetOfferViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GetOfferViewHolder, position: Int) {
        holder.binding.apply {
            textViewPost.text = offerList[position].offer_name
            textViewDate.text = (SimpleDateFormat("dd MMMM yyyy HH:mm:ss z")).format(offerList[position].float_date)
            textViewCollege.text = offerList[position].collegeName
            textViewSkills.text = offerList[position].skills
            root.setOnClickListener { listener.onViewDetailsClick(position) }
        }
    }

    override fun getItemCount(): Int = offerList.size

    interface GetOffersListener{
        fun onViewDetailsClick(itemPosition: Int)
    }
}