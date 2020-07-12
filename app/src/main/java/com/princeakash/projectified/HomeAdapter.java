package com.princeakash.projectified;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private ArrayList<HomeItem> list;
    private Context context;
    private HomeListener homeListener;

    public HomeAdapter(ArrayList<HomeItem> list, Context context, HomeListener homeListener) {
        this.list = list;
        this.context = context;
        this.homeListener = homeListener;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_home_job, parent, false);
        return new HomeViewHolder(view, homeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        holder.textView.setText(list.get(position).itemName);
        holder.imageView.setImageResource(list.get(position).itemDrawable);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        HomeListener homeListener;

        public HomeViewHolder(@NonNull View itemView, HomeListener homeListener) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.image);
            this.textView = itemView.findViewById(R.id.description);
            this.homeListener = homeListener;
        }
    }

    public static class HomeItem{
        public String itemName;
        public int itemDrawable;

        public HomeItem(String itemName, int itemDrawable) {
            this.itemName = itemName;
            this.itemDrawable = itemDrawable;
        }
    }

    public interface HomeListener{

    }
}
