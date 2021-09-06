package com.princeakash.projectified.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.R
import com.princeakash.projectified.databinding.CardMyapplicationBinding
import com.princeakash.projectified.model.candidate.ApplicationCardModelCandidate
import java.text.SimpleDateFormat

class MyApplicationsAdapter(private var applicationList : List<ApplicationCardModelCandidate>, private val listener: MyApplicationsListener): RecyclerView.Adapter<MyApplicationsAdapter.MyApplicationsViewHolder>() {

    class MyApplicationsViewHolder(val binding: CardMyapplicationBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyApplicationsViewHolder {
        val binding = CardMyapplicationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyApplicationsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyApplicationsViewHolder, position: Int) {
        holder.binding.textViewName.text = applicationList[position].recruiter_name
        holder.binding.textViewPost.text = applicationList[position].offer_name
        holder.binding.textViewCollege.text= applicationList[position].collegeName
        holder.binding.textViewDate.text = (SimpleDateFormat("dd MMMM yyyy HH:mm:ss z")).format(applicationList[position].float_date)
        holder.binding.root.setOnClickListener { listener.onViewDetailsClick(position) }

        if(applicationList[position].is_Seen)
            holder.binding.imageViewSeen.setImageResource(R.drawable.ic_baseline_favorite_24)
        if(applicationList[position].is_Selected)
            holder.binding.imageViewSelected.setImageResource(R.drawable.ic_baseline_done_24)
    }

    override fun getItemCount(): Int = applicationList.size

    interface MyApplicationsListener{
        fun onViewDetailsClick(itemPosition: Int)
    }
}