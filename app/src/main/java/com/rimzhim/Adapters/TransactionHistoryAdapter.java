package com.rimzhim.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rimzhim.R;
import  com.rimzhim.ModelClasses.TransactionHistory.DataItem;

import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    Context context;
    List<DataItem> mPostItems;

    public TransactionHistoryAdapter(Context context, List<DataItem> mPostItems) {
        this.context = context;
        this.mPostItems = mPostItems;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new TransactionHistoryAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tranaction_history, parent, false));
            case VIEW_TYPE_LOADING:
                return new TransactionHistoryAdapter.ProgressHolder(
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
        TextView texNo, Date, Types, Amount, Details;


        ViewHolder(View itemView) {
            super(itemView);
            texNo =itemView.findViewById(R.id.texNo);
            Date =itemView.findViewById(R.id.Date);
            Types =itemView.findViewById(R.id.types);
            Amount =itemView.findViewById(R.id.amount);
            Details =itemView.findViewById(R.id.details);




        }


        protected void clear() {


        }

        public void onBind(int position) {
            super.onBind(position);
            DataItem temp = mPostItems.get(position);

            texNo.setText(temp.getTxn_no());
            Types.setText(temp.getType());
          //  Date.setText(temp.get);
            Amount.setText(String.valueOf(temp.getAmount()));
           // Details.setText(temp.get);





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




