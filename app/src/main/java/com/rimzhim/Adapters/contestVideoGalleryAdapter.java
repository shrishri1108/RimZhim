package com.rimzhim.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rimzhim.Interfaces.videoItemClickInterface;
import com.rimzhim.ModelClasses.LoginResponse;
import com.rimzhim.ModelClasses.model.DataItem;
import com.rimzhim.R;
import com.rimzhim.SharedPres.SharedPrefManager;
import com.rimzhim.SimpleClasses.Functions;
import java.util.List;


public class contestVideoGalleryAdapter extends RecyclerView.Adapter<contestVideoGalleryAdapter.ViewHolder> {
    Context context;
    List<DataItem> data;
    private static com.rimzhim.Interfaces.videoItemClickInterface videoItemClickInterface;
    public contestVideoGalleryAdapter(Context context, List<DataItem> data, com.rimzhim.Interfaces.videoItemClickInterface videoItemClickInterface) {
        this.context = context;
        this.data = data;
        this.videoItemClickInterface = videoItemClickInterface;
    }

    @NonNull
    @Override
    public contestVideoGalleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contest_gallery_video, parent, false);

        return new contestVideoGalleryAdapter.ViewHolder(view, videoItemClickInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull contestVideoGalleryAdapter.ViewHolder holder, int position) {
        final DataItem temp = data.get(position);


        LoginResponse.User user = SharedPrefManager.getInstance(holder.likes.getContext()).getUser();
        String userId = String.valueOf(user.getId());

        holder.likes.setText(Functions.getSuffix(String.valueOf(temp.getTotal_likes())));

        long thumb = holder.getLayoutPosition()*1000;
        RequestOptions options = new RequestOptions().frame(thumb);
        Glide.with(holder.itemView.getContext()).load(temp.getVideo()).placeholder(R.drawable.ic_baseline_error_outline_24).apply(options).into(holder.videoCover);

        if(temp.isSelected()){
            holder.selectBtn.setVisibility(View.VISIBLE);

        }else {
            holder.selectBtn.setVisibility(View.GONE);
        }

        holder.videoCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(temp.isSelected()){
                   temp.setSelected(false);

                }else {
                    temp.setSelected(true);
                    holder.selectBtn.setVisibility(View.GONE);
                }
                notifyItemChanged(position);
            }
        });

    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView videoCover ;
        TextView likes;
        Bitmap bmThumbnail;
        String draftFile, duetVideoId, duetVideoUsername, duetOrientation;
        LinearLayout selectBtn;


        public ViewHolder(@NonNull View itemView,videoItemClickInterface videoItemClickInterface) {
            super(itemView);
            videoCover =itemView.findViewById(R.id.videoCover);
            likes =itemView.findViewById(R.id.likeCount);
            selectBtn =itemView.findViewById(R.id.playBtn);




        }

    }
}

