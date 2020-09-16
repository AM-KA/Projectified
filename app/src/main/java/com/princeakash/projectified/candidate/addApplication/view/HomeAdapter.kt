package com.princeakash.projectified.candidate.addApplication.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.R
import com.princeakash.projectified.candidate.addApplication.view.HomeAdapter.HomeViewHolder
import java.util.*


class HomeAdapter(private val list: ArrayList<HomeItem>, private val context: Context, private val homeListener: HomeListener) : RecyclerView.Adapter<HomeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_home_job, parent, false)
        return HomeViewHolder(view, homeListener)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.textView.text = list[position].itemName
        holder.imageView.setImageResource(list[position].itemDrawable)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class HomeViewHolder(itemView: View, homeListener: HomeListener) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var textView: TextView
        var homeListener: HomeListener

        init {
            imageView = itemView.findViewById(R.id.image)
            textView = itemView.findViewById(R.id.description)
            this.homeListener = homeListener
        }
    }

    class HomeItem(var itemName: String, var itemDrawable: Int)
    interface HomeListener
}