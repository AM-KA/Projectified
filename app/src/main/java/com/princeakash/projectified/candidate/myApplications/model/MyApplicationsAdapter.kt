package com.princeakash.projectified.candidate.myApplications.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.R
import kotlinx.android.synthetic.main.card_myapplication.view.*

class MyApplicationsAdapter(var applicationList : List<ApplicationCardModelCandidate>? , val listener: MyApplicationsListener): RecyclerView.Adapter<MyApplicationsAdapter.MyApplicationsViewHolder>()
{


    class MyApplicationsViewHolder(itemView: View, listener: MyApplicationsListener):RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val textViewPost = itemView.textViewPost
        val textViewCollege = itemView.textViewCollege
        val textViewDate = itemView.textViewDate
        val buttonViewDetails = itemView.button
        val myListener = listener

        init{
            buttonViewDetails.setOnClickListener(this)
        }


        override fun onClick(v: View?) {
            myListener.onViewDetailsClick(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyApplicationsViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_myapplication, parent, false)
        return MyApplicationsViewHolder(v,listener)
    }

    override fun onBindViewHolder(holder: MyApplicationsViewHolder, position: Int) {
        holder.textViewPost.text = applicationList?.get(position)?.offer_name
        holder.textViewCollege.text= applicationList?.get(position)?.collegeName
        holder.textViewDate.text = applicationList?.get(position)?.float_date.toString()
    }

    override fun getItemCount(): Int = applicationList!!.size

    interface MyApplicationsListener{
        fun onViewDetailsClick(itemPosition: Int)
    }
}

