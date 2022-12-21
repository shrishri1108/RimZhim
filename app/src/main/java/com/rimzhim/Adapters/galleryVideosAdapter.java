package com.rimzhim.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.ListenableWorker;

import com.beak.gifmakerlib.GifMaker;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.drawee.view.SimpleDraweeView;
import com.rimzhim.Interfaces.videoItemClickInterface;


import com.rimzhim.ModelClasses.LoginResponse;
import com.rimzhim.ModelClasses.model.DataItem;
import com.rimzhim.R;
import com.rimzhim.SharedPres.SharedPrefManager;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.dialogs.contestBookedDialog;

import java.util.List;

import butterknife.ButterKnife;

public class galleryVideosAdapter extends RecyclerView.Adapter<galleryVideosAdapter.ViewHolder> {
    Context context;
    List<DataItem> data;
   private static videoItemClickInterface videoItemClickInterface;

    public galleryVideosAdapter(Context context, List<DataItem> data, com.rimzhim.Interfaces.videoItemClickInterface videoItemClickInterface) {
        this.context = context;
        this.data = data;
        this.videoItemClickInterface = videoItemClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_video, parent, false);

        return new ViewHolder(view, videoItemClickInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DataItem temp = data.get(position);


        LoginResponse.User user = SharedPrefManager.getInstance(holder.likes.getContext()).getUser();
        String userId = String.valueOf(user.getId());
        if(!temp.getUser_id().equals(userId.trim())){
            holder.moreMenus.setVisibility(View.GONE);
        }
        holder.likes.setText(Functions.getSuffix(String.valueOf(temp.getTotal_likes())));

      /*  String preview ="";
        GifMaker gif = new GifMaker(2);
        gif.makeGifFromVideo(temp.getVideo(), 1000, 3000, 250, preview);*/



        long thumb = holder.getLayoutPosition()*1000;
        RequestOptions options = new RequestOptions().frame(thumb);
        Glide.with(holder.itemView.getContext()).load(temp.getVideo()).placeholder(R.drawable.video_place_holder).apply(options).into(holder.videoCover);
     //   Glide.with(holder.itemView.getContext()).asGif().load(preview).placeholder(R.drawable.video_place_holder).apply(options).into(holder.videoCover);
      //  holder.thumbImage.setController(Functions.frescoImageLoad(temp.getVideo(),holder.thumbImage,true));

        if(data.get(position).getIs_winning() == 1){
            Glide.with(holder.itemView.getContext()).asGif().load(R.raw.win_video_gif).into(holder.crownGif);
        }
    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView videoCover, crownGif, moreMenus, playBtn;
        TextView likes;
        Bitmap bmThumbnail;
        String draftFile, duetVideoId, duetVideoUsername, duetOrientation;
        SimpleDraweeView thumbImage;


        public ViewHolder(@NonNull View itemView,videoItemClickInterface videoItemClickInterface) {
            super(itemView);
            videoCover =itemView.findViewById(R.id.videoCover);
            likes =itemView.findViewById(R.id.likeCount);
            crownGif =itemView.findViewById(R.id.crownGif);
            moreMenus =itemView.findViewById(R.id.moreMenus);
            playBtn =itemView.findViewById(R.id.playBtn);
            thumbImage = itemView.findViewById(R.id.thumb_image);

            moreMenus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    videoItemClickInterface.onClick(getAdapterPosition(), moreMenus.getId());
                }
            });
            videoCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    videoItemClickInterface.onClick(getAdapterPosition(), videoCover.getId());
                }
            });


        }





    }

}

