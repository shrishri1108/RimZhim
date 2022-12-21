package com.rimzhim.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rimzhim.ModelClasses.ContestResponse.DataItem;
import com.rimzhim.R;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.dialogs.contestBookedDialog;

import java.util.List;

import butterknife.ButterKnife;


/*
public class liveContestAdapter extends RecyclerView.Adapter<liveContestAdapter.ViewHolder> {
    Context context;
    List<DataItem> data;

    public liveContestAdapter(Context context, List<DataItem> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_contests_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataItem temp = data.get(position);

      //  holder.winngPriceAmountTv.setText(data.get(position).getPrice());
        holder.winngPriceAmountTv.setText(temp.getWinning_amount_in_text());
       // holder.totalContestantsCount.setText(temp.);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView winngPriceAmountTv, room_id,
                totalContestantsCount, timeLeft, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            winngPriceAmountTv =itemView.findViewById(R.id.winngPriceAmountTv);
            room_id =itemView.findViewById(R.id.room_id);
            time =itemView.findViewById(R.id.time);
            timeLeft =itemView.findViewById(R.id.timeLeft);
            totalContestantsCount =itemView.findViewById(R.id.totalContestantsCount);
        }
    }
}
*/


public class liveContestAdapter extends RecyclerView.Adapter<BaseViewHolder> {
private static final int VIEW_TYPE_LOADING = 0;
private static final int VIEW_TYPE_NORMAL = 1;
private boolean isLoaderVisible = false;
private  List<DataItem> mPostItems;
        Context context;

public liveContestAdapter(List<DataItem> mPostItems, Context context) {
        this.mPostItems = mPostItems;
        this.context = context;
        }

@NonNull
@Override
public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
        case VIEW_TYPE_NORMAL:
        return new ViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_contests_view, parent, false));
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
    TextView winngPriceAmountTv, room_id,
            totalContestantsCount, timeLeft, time;


    ViewHolder(View itemView) {
        super(itemView);
        winngPriceAmountTv =itemView.findViewById(R.id.winngPriceAmountTv);
        room_id =itemView.findViewById(R.id.room_id);
        time =itemView.findViewById(R.id.time);
        timeLeft =itemView.findViewById(R.id.timeLeft);
        totalContestantsCount =itemView.findViewById(R.id.totalContestantsCount);
    }



    protected void clear() {
    }
    public void onBind(int position) {
        super.onBind(position);
        DataItem temp = mPostItems.get(position);
        //  holder.winngPriceAmountTv.setText(data.get(position).getPrice());
        winngPriceAmountTv.setText(temp.getWinning_amount_in_text());
        // holder.totalContestantsCount.setText(temp.);

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



