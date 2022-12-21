package com.rimzhim.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.rimzhim.Interfaces.onClickOnTagAdapter;
import com.rimzhim.ModelClasses.model.TagsItem;
import com.rimzhim.R;

import java.util.ArrayList;
import java.util.List;

public class tagAdapter extends RecyclerView.Adapter<tagAdapter.ViewHolder> {
    Context applicationContext;
    List<TagsItem> data;
    onClickOnTagAdapter onClickOnTagAdapter;

    public tagAdapter(Context applicationContext, List<TagsItem> data, com.rimzhim.Interfaces.onClickOnTagAdapter onClickOnTagAdapter) {
        this.applicationContext = applicationContext;
        this.data = data;
        this.onClickOnTagAdapter = onClickOnTagAdapter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_tab,parent,false);
        return new ViewHolder(view,onClickOnTagAdapter);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tag.setText(data.get(position).getName().trim());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        TextView tag;
        public ViewHolder(@NonNull View itemView, com.rimzhim.Interfaces.onClickOnTagAdapter onClickOnTagAdapter) {
            super(itemView);
            tag = itemView.findViewById(R.id.tag);

            tag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   onClickOnTagAdapter.onTagClick(getAdapterPosition());
                }
            });


        }
    }
}
