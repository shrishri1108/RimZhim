package com.rimzhim.Sticker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rimzhim.Interfaces.StickerOnClick;
import com.rimzhim.R;

import java.util.ArrayList;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {
    Context context;
    ArrayList<StickersModel> data;
    private StickerOnClick stickerOnClick;

    public StickerAdapter(Context context, ArrayList<StickersModel> data, StickerOnClick stickerOnClick) {
        this.context = context;
        this.data = data;
        this.stickerOnClick = stickerOnClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sticker,parent,false);
        return new ViewHolder(view, stickerOnClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.image).asGif().load(data.get(position).getSticker()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        public ViewHolder(@NonNull View itemView, StickerOnClick stickerOnClick) {
            super(itemView);
            image =(ImageView) itemView.findViewById(R.id.img);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stickerOnClick.onClick(getAdapterPosition());
                }
            });
        }
    }
}
