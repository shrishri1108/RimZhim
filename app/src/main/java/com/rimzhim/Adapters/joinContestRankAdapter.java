package com.rimzhim.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rimzhim.ModelClasses.ContestResponse.DistributionItem;
import com.rimzhim.R;

import java.util.List;


public class joinContestRankAdapter extends RecyclerView.Adapter<joinContestRankAdapter.viewHolder> {
    Context context;
    List<DistributionItem> data;

    public joinContestRankAdapter(Context context, List<DistributionItem> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rank_join_contest,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        final  DistributionItem temp = data.get(position);
        holder.rank.setText("#"+String.valueOf(temp.getLower_rank()));
        holder.priceAmount.setText(context.getString(R.string.rupee)+String.valueOf(temp.getAmount()));


    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder {
        TextView rank, priceAmount;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.rank);
            priceAmount = itemView.findViewById(R.id.priceAmount);
        }
    }
}
