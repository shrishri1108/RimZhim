package com.rimzhim.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.google.android.material.imageview.ShapeableImageView;
import com.rimzhim.Activities.SingleVideoActivity;
import com.rimzhim.ModelClasses.ContestResponse.DataItem;
import com.rimzhim.ModelClasses.LeaderBordModel.LeaderboardItem;
import com.rimzhim.R;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.dialogs.contestBookedDialog;

import java.util.List;

import butterknife.ButterKnife;

/*public class leaderBordConstestantsAdapter extends RecyclerView.Adapter<leaderBordConstestantsAdapter.viewHolder> {
    Context context;
   List<LeaderboardItem>  data;

    public leaderBordConstestantsAdapter(Context context, List<LeaderboardItem>  data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contestants_row,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        final  LeaderboardItem temp = data.get(position);
        Glide.with(holder.name.getContext()).load(temp.getUser_image()).placeholder(R.drawable.ic_user_icon).into(holder.img);
        holder.name.setText(temp.getUser_name());
        holder.vote.setText(temp.getVotes());
        holder.rank.setText(String.valueOf("#"+temp.getRank()));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView img;
        TextView name, vote, rank;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            img =itemView.findViewById(R.id.userImg);
            name =itemView.findViewById(R.id.contestantName);
            vote =itemView.findViewById(R.id.vote);
            rank =itemView.findViewById(R.id.rank);
        }
    }
}*/

public class leaderBordConstestantsAdapter extends RecyclerView.Adapter<BaseViewHolder> {
private static final int VIEW_TYPE_LOADING = 0;
private static final int VIEW_TYPE_NORMAL = 1;
private boolean isLoaderVisible = false;
    Context context;
    List<LeaderboardItem>  mPostItems;

    public leaderBordConstestantsAdapter(Context context, List<LeaderboardItem> mPostItems) {
        this.context = context;
        this.mPostItems = mPostItems;
    }

    @NonNull
@Override
public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
        case VIEW_TYPE_NORMAL:
        return new ViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contestants_row, parent, false));
        case VIEW_TYPE_LOADING:
        return new ProgressHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
default:
        return null;
        }
        }
@Override
public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
        }
@Override
public int getItemViewType(int position) {
        if (isLoaderVisible) {
        return position == mPostItems.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
        return VIEW_TYPE_NORMAL;
        }
        }
@Override
public int getItemCount() {
        return mPostItems == null ? 0 : mPostItems.size();
        }
public void addItems(List<LeaderboardItem> postItems) {
        mPostItems.addAll(postItems);
        notifyDataSetChanged();
        }
public void addLoading() {
        isLoaderVisible = true;
        mPostItems.add(new LeaderboardItem());
        notifyItemInserted(mPostItems.size() - 1);
        }
public void removeLoading() {
        isLoaderVisible = false;
        int position = mPostItems.size() - 1;
    LeaderboardItem item = getItem(position);
        if (item != null) {
        mPostItems.remove(position);
        notifyItemRemoved(position);
        }
        }
public void clear() {
        mPostItems.clear();
        notifyDataSetChanged();
        }
    LeaderboardItem getItem(int position) {
        return mPostItems.get(position);
        }
public class ViewHolder extends BaseViewHolder {
    ShapeableImageView img;
    TextView name, vote, rank, videoLink, InWinningZon;

    ViewHolder(View itemView) {
        super(itemView);
        img =itemView.findViewById(R.id.userImg);
        name =itemView.findViewById(R.id.contestantName);
        vote =itemView.findViewById(R.id.vote);
        rank =itemView.findViewById(R.id.rank);
        videoLink =itemView.findViewById(R.id.videoLink);
        InWinningZon =itemView.findViewById(R.id.InWinningZon);

    }



    protected void clear() {
    }
    public void onBind(int position) {
        super.onBind(position);
        LeaderboardItem temp = mPostItems.get(position);

        Glide.with(name.getContext()).load(temp.getUser_image()).placeholder(R.drawable.ic_user_icon).into(img);
        name.setText(temp.getUser_name());
        vote.setText(temp.getVotes());
        rank.setText(String.valueOf("#"+temp.getRank()));
        videoLink.setText(" @"+"Link");

        videoLink.setOnClickListener(view -> {
            Intent intent = new Intent(name.getContext(), SingleVideoActivity.class);
            intent.putExtra("videoLink",temp.getVideo_link());
            name.getContext().startActivity(intent);
        });

        if(temp.getIs_winning_zone().equals("Y")){
            InWinningZon.setVisibility(View.VISIBLE);

        }else {
            InWinningZon.setVisibility(View.GONE);
        }



    }
}
public class ProgressHolder extends BaseViewHolder {
    ProgressHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
    @Override
    protected void clear() {
    }
}
}



