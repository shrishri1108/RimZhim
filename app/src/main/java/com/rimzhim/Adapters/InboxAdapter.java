package com.rimzhim.Adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rimzhim.ModelClasses.GetChats.DataItem;
import com.rimzhim.ModelClasses.LoginResponse;
import com.rimzhim.R;
import com.rimzhim.SharedPres.SharedPrefManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*public class InboxAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    private List<DataItem> mPostItems;

    public InboxAdapter(List<DataItem> postItems) {
        this.mPostItems = postItems;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_text_layout, parent, false));
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
        @BindView(R.id.chatText)
        TextView chatText;
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.card)
        CardView card;
        @BindView(R.id.chatLinearLayout)
        LinearLayoutCompat chatLinearLayout;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);
            DataItem temp = mPostItems.get(position);
            LoginResponse.User user = SharedPrefManager.getInstance(chatText.getContext()).getUser();
            String userId = String.valueOf(user.getId());

            if(temp.getSender_id().equals(userId.trim())){
                chatLinearLayout.setGravity(Gravity.END);
                chatText.setBackgroundResource(R.drawable.chat_text_background);
                chatText.setText(temp.getText());
                if(temp.getFile_type() != null){
                    chatText.setVisibility(View.GONE);
                    card.setVisibility(View.VISIBLE);
                    Glide.with(chatText.getContext()).load(temp.getText().trim()).into(img);
                }
            }else {
                chatLinearLayout.setGravity(Gravity.START);
                chatText.setBackgroundResource(R.drawable.sender_chat_background);
                chatText.setText(temp.getText());
                if(temp.getFile_type() != null){
                    chatText.setVisibility(View.GONE);
                    card.setVisibility(View.VISIBLE);
                    Glide.with(chatText.getContext()).load(temp.getText().trim()).into(img);
                }
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
}*/

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.viewHolder>{
    private List<DataItem> data;
    Context context;

    public InboxAdapter(List<DataItem> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_text_layout, parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        DataItem temp = data.get(position);
        LoginResponse.User user = SharedPrefManager.getInstance(holder.chatText.getContext()).getUser();
        String userId = String.valueOf(user.getId());

        if(temp.getSender_id().equals(userId.trim())){
            holder.chatLinearLayout.setGravity(Gravity.END);
            holder.chatText.setBackgroundResource(R.drawable.chat_text_background);
            holder.chatText.setText(temp.getText());

        }else {
            holder.chatLinearLayout.setGravity(Gravity.START);
            holder.chatText.setBackgroundResource(R.drawable.sender_chat_background);
            holder.chatText.setText(temp.getText());

        }



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView chatText;
        ImageView img;
        CardView card;
        LinearLayoutCompat chatLinearLayout;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            chatLinearLayout = itemView.findViewById(R.id.chatLinearLayout);
            card = itemView.findViewById(R.id.card);
            img = itemView.findViewById(R.id.img);
            chatText = itemView.findViewById(R.id.chatText);
        }
    }
}
