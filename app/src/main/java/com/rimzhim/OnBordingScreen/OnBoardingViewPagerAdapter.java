package com.rimzhim.OnBordingScreen;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rimzhim.Activities.Login_Activity;
import com.rimzhim.R;
import com.rimzhim.SharedPres.onBoardingPref;

import java.util.ArrayList;

public class OnBoardingViewPagerAdapter extends RecyclerView.Adapter<OnBoardingViewPagerAdapter.viewHolder> {
    Context context;
    ArrayList<onBordingScreenModel> data;

    public OnBoardingViewPagerAdapter(Context context, ArrayList<onBordingScreenModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.onbording_image_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        holder.des.setText(data.get(position).getDescription());
        holder.img.setBackgroundColor(data.get(position).getColor());
        Glide.with(holder.des).asGif().load(data.get(position).getName()).into(holder.img);
        
        if(position == data.size()-1){
            holder.skipBtn.setVisibility(View.VISIBLE);
           // onBoardingPref.getInstance(holder.des.getContext()).save(true);
        }

        holder.skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBoardingPref.getInstance(holder.des.getContext()).save(true);
                Intent intent = new Intent(holder.des.getContext(), Login_Activity.class);
                holder.img.getContext().startActivity(intent);
                ((onBordingActivity)holder.des.getContext()).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView des;
        Button skipBtn;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            des = itemView.findViewById(R.id.des);
            skipBtn = itemView.findViewById(R.id.skipBtn);
        }
    }
}
