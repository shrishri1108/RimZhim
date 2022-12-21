package com.rimzhim.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.rimzhim.R;
import com.rimzhim.SimpleClasses.Constants;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.SimpleClasses.Variables;

import java.util.ArrayList;

/**
 * Created by qboxus on 3/20/2018.
 */

public class GifAdapter extends RecyclerView.Adapter<GifAdapter.CustomViewHolder> {
    public Context context;
    ArrayList<String> gifList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String item);
    }

    public GifAdapter(Context context, ArrayList<String> datalist, OnItemClickListener listener) {
        this.context = context;
        this.gifList = datalist;
        this.listener = listener;

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gif_layout, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return gifList.size();
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView gifImage;

        public CustomViewHolder(View view) {
            super(view);
            gifImage = view.findViewById(R.id.gif_image);
        }

        public void bind(final String item, final OnItemClickListener listener) {

            itemView.setOnClickListener(v -> {
                    listener.onItemClick(item);

            });


        }

    }


    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int i) {
        holder.bind(gifList.get(i), listener);


        // show the gif images by fresco
         String url= Variables.GIF_FIRSTPART + gifList.get(i) + Variables.GIF_SECONDPART;
        holder.gifImage.setController(Functions.frescoImageLoad(url,holder.gifImage,true));

        Functions.printLog(Constants.tag, Variables.GIF_FIRSTPART + gifList.get(i) + Variables.GIF_SECONDPART);
    }


}