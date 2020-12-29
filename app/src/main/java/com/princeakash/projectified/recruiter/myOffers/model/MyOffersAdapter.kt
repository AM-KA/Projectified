package com.princeakash.projectified.recruiter.myOffers.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.R
import kotlinx.android.synthetic.main.card_my_offers.view.*
import java.text.SimpleDateFormat

class MyOffersAdapter(var offerList: List<OfferCardModelRecruiter>?, val listener:MyOffersListener): RecyclerView.Adapter<MyOffersAdapter.MyOffersViewHolder>() {

    class MyOffersViewHolder(itemView: View, listener: MyOffersListener):RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val textViewPost = itemView.textViewPost
        val textViewDate = itemView.textViewDate
        val textViewApplicants = itemView.textViewCollege
        //val buttonViewDetails = itemView.button
        val myListener = listener

        init{
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            myListener.onViewDetailsClick(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOffersViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_my_offers, parent, false)
        return MyOffersViewHolder(v, listener)
    }

    override fun onBindViewHolder(holder: MyOffersViewHolder, position: Int) {
        holder.textViewPost.text = offerList?.get(position)?.offer_name
        holder.textViewDate.text = (SimpleDateFormat("dd MMMM yyyy HH:mm:ss z")).format(offerList?.get(position)?.float_date)
        holder.textViewApplicants.text =  "${offerList?.get(position)?.no_of_applicants} Applicants"
    }

    override fun getItemCount(): Int = offerList!!.size

    interface MyOffersListener{
        fun onViewDetailsClick(itemPosition: Int)
    }
}