package com.princeakash.projectified.recruiter.myOffers.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.databinding.CardMyOffersBinding
import java.text.SimpleDateFormat

class MyOffersAdapter(var offerList: List<OfferCardModelRecruiter>?, val listener:MyOffersListener): RecyclerView.Adapter<MyOffersAdapter.MyOffersViewHolder>() {

    class MyOffersViewHolder(val binding: CardMyOffersBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOffersViewHolder {
        val binding = CardMyOffersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyOffersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyOffersViewHolder, position: Int) {
        holder.binding.apply {
            textViewPost.text = offerList?.get(position)?.offer_name
            textViewDate.text = (SimpleDateFormat("dd MMMM yyyy HH:mm:ss z")).format(offerList?.get(position)?.float_date)
            textViewCollege.text =  "${offerList?.get(position)?.no_of_applicants} Applicants"
            root.setOnClickListener { listener.onViewDetailsClick(position) }
        }
    }

    override fun getItemCount(): Int = offerList!!.size

    interface MyOffersListener{
        fun onViewDetailsClick(itemPosition: Int)
    }
}