package com.rimzhim.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.rimzhim.Chats.ChatActivity;
import com.rimzhim.ModelClasses.ChatModel.DataItem;
import com.rimzhim.R;

import java.util.List;

import butterknife.ButterKnife;
public class chatAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    private  List<DataItem> mPostItems;
    Context context;

    public chatAdapter(List<DataItem> mPostItems, Context context) {
        this.mPostItems = mPostItems;
        this.context = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new chatAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inbox_list, parent, false));
            case VIEW_TYPE_LOADING:
                return new chatAdapter.ProgressHolder(
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
    public void addItems(List<DataItem> postItems) {
        mPostItems.addAll(postItems);
        notifyDataSetChanged();
    }
    public void addLoading() {
        isLoaderVisible = true;
        mPostItems.add(new DataItem());
        notifyItemInserted(mPostItems.size() - 1);
    }
    public void removeLoading() {
        isLoaderVisible = false;
        int position = mPostItems.size() - 1;
        DataItem item = getItem(position);
        if (item != null) {
            mPostItems.remove(position);
            notifyItemRemoved(position);
        }
    }
    public void clear() {
        mPostItems.clear();
        notifyDataSetChanged();
    }
    DataItem getItem(int position) {
        return mPostItems.get(position);
    }
    public class ViewHolder extends BaseViewHolder {
        ShapeableImageView userImg;
        TextView username, message, datetxt, msgCount;
        RelativeLayout inbox;

        ViewHolder(View itemView) {
            super(itemView);
            userImg = itemView.findViewById(R.id.user_image);
            username = itemView.findViewById(R.id.username);
            message = itemView.findViewById(R.id.message);
            datetxt = itemView.findViewById(R.id.datetxt);
            msgCount = itemView.findViewById(R.id.msgCount);
            inbox = itemView.findViewById(R.id.inbox);
        }



        protected void clear() {

        }
        public void onBind(int position) {
            super.onBind(position);
            DataItem temp = mPostItems.get(position);
           username.setText(temp.getName().trim());
           message.setText(temp.getChat_message());
           datetxt.setText(temp.getTime());
            Glide.with(datetxt.getContext()).load(temp.getImage()).placeholder(R.drawable.ic_user_icon).into(userImg);
            inbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(inbox.getContext(), ChatActivity.class);
                    intent.putExtra("userName",temp.getName());
                    intent.putExtra("userId",String.valueOf(temp.getId()));
                    intent.putExtra("userImg",String.valueOf(temp.getImage()));
                    inbox.getContext().startActivity(intent);
                }
            });



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


