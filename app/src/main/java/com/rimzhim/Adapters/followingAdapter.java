package com.rimzhim.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.rimzhim.ModelClasses.FollowUserResponseModel;
import com.google.android.material.imageview.ShapeableImageView;

import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SharedPres.loginResponsePref;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.dialogs.contestBookedDialog;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import  com.rimzhim.ModelClasses.FollowerFollowingResponseModel.DataItem;

/*
public class followingAdapter extends RecyclerView.Adapter<followingAdapter.viewHolder> {
    Context context;
    List<DataItem> data;

    public followingAdapter(Context context, List<DataItem> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.following_item_row,parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        DataItem temp = data.get(position);
        Glide.with(holder.name.getContext()).load(temp.getImage()).into(holder.img);
        holder.name.setText(temp.getName().trim());
        holder.userName.setText(temp.getUser_name().trim());

        if(temp.getIs_following()==1){
            Drawable buttonDrawable = holder.followingBtn.getBackground();
            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
            DrawableCompat.setTint(buttonDrawable, Color.TRANSPARENT);
            holder.followingBtn.setBackground(buttonDrawable);
            holder.followingBtn.setText("Following");
            holder.followingBtn.setTextColor(Color.BLACK);
        }else {
            Drawable buttonDrawable = holder.followingBtn.getBackground();
            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
            DrawableCompat.setTint(buttonDrawable, Color.parseColor("#ED4D4D"));
            holder.followingBtn.setBackground(buttonDrawable);
            holder.followingBtn.setText("Follow");
            holder.followingBtn.setTextColor(Color.WHITE);
        }


        holder.followingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followUser(holder.followingBtn.getContext(),temp, holder);
            }
        });

        holder.userRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.OpenOtherProfile(holder.followingBtn.getContext(),String.valueOf(temp.getId()));
            }
        });




    }

    private void followUser(Context context, DataItem temp, viewHolder holder) {
        String token = loginResponsePref.getInstance(context).getToken().trim();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token",token);
        hashMap.put("follower_id",String.valueOf(temp.getId()));

        Call<FollowUserResponseModel> call = ApiClient.getUserService().FollowUser(hashMap);
        call.enqueue(new Callback<FollowUserResponseModel>() {
            @Override
            public void onResponse(Call<FollowUserResponseModel> call, Response<FollowUserResponseModel> response) {
                if(response.isSuccessful()){
                    if(response.body().isResult()){
                        Toast.makeText(context, response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        if(response.body().getMessage().trim().equals("Follow Successfully")){

                            Drawable buttonDrawable = holder.followingBtn.getBackground();
                            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                            DrawableCompat.setTint(buttonDrawable, Color.TRANSPARENT);
                            holder.followingBtn.setBackground(buttonDrawable);
                            holder.followingBtn.setText("Following");
                            holder.followingBtn.setTextColor(Color.BLACK);

                        }else {

                            Drawable buttonDrawable = holder.followingBtn.getBackground();
                            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                            DrawableCompat.setTint(buttonDrawable, Color.parseColor("#ED4D4D"));
                            holder.followingBtn.setBackground(buttonDrawable);
                            holder.followingBtn.setText("Follow");
                            holder.followingBtn.setTextColor(Color.WHITE);

                        }

                    }else {
                        Toast.makeText(context, response.body().getMessage(),Toast.LENGTH_SHORT).show();

                    }

                }

            }

            @Override
            public void onFailure(Call<FollowUserResponseModel> call, Throwable t) {
                Toast.makeText(context,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });






    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView img;
        TextView name, userName;
        Button followingBtn;
        ConstraintLayout userRow;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.userImg);
            userName = itemView.findViewById(R.id.userName);
            name = itemView.findViewById(R.id.fullName);
            followingBtn = itemView.findViewById(R.id.followingBtn);
            userRow = itemView.findViewById(R.id.userRow);

        }
    }
}
*/

public class followingAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    Context context;
    List<DataItem> mPostItems;

    public followingAdapter(Context context, List<DataItem> mPostItems) {
        this.context = context;
        this.mPostItems = mPostItems;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new followingAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.following_item_row, parent, false));
            case VIEW_TYPE_LOADING:
                return new followingAdapter.ProgressHolder(
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
        ShapeableImageView img;
        TextView name, userName;
        Button followingBtn;
        ConstraintLayout userRow;

        ViewHolder(View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.userImg);
            userName = itemView.findViewById(R.id.userName);
            name = itemView.findViewById(R.id.fullName);
            followingBtn = itemView.findViewById(R.id.followingBtn);
            userRow = itemView.findViewById(R.id.userRow);
        }


        protected void clear() {


        }

        public void onBind(int position) {
            super.onBind(position);
            DataItem temp = mPostItems.get(position);
            Glide.with(name.getContext()).load(temp.getImage()).placeholder(R.drawable.ic_user_icon).into(img);
            name.setText(temp.getName().trim());
            userName.setText(temp.getUser_name().trim());
            if (temp.getIs_following() == 1) {
                Drawable buttonDrawable = followingBtn.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                DrawableCompat.setTint(buttonDrawable, Color.TRANSPARENT);
                followingBtn.setBackground(buttonDrawable);
                followingBtn.setText("Following");
                followingBtn.setTextColor(Color.BLACK);
            } else {
                Drawable buttonDrawable = followingBtn.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                DrawableCompat.setTint(buttonDrawable, Color.parseColor("#ED4D4D"));
                followingBtn.setBackground(buttonDrawable);
                followingBtn.setText("Follow");
                followingBtn.setTextColor(Color.WHITE);
            }
            followingBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(followingBtn.getText().equals("Following")){
                        Drawable buttonDrawable = followingBtn.getBackground();
                        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                        DrawableCompat.setTint(buttonDrawable, Color.parseColor("#ED4D4D"));
                        followingBtn.setBackground(buttonDrawable);
                        followingBtn.setText("Follow");
                        followingBtn.setTextColor(Color.WHITE);
                        temp.setIs_following(0);
                        followUser(followingBtn.getContext(), temp);

                    }else {
                        Drawable buttonDrawable = followingBtn.getBackground();
                        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                        DrawableCompat.setTint(buttonDrawable, Color.TRANSPARENT);
                        followingBtn.setBackground(buttonDrawable);
                        followingBtn.setText("Following");
                        followingBtn.setTextColor(Color.BLACK);
                        temp.setIs_following(1);
                        followUser(followingBtn.getContext(), temp);
                    }
                }
            });

            userRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Functions.OpenOtherProfile(followingBtn.getContext(), String.valueOf(temp.getId()));
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

    private void followUser(Context context, DataItem temp) {
        String token = loginResponsePref.getInstance(context).getToken().trim();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("follower_id", String.valueOf(temp.getId()));

        Call<FollowUserResponseModel> call = ApiClient.getUserService().FollowUser(hashMap);
        call.enqueue(new Callback<FollowUserResponseModel>() {
            @Override
            public void onResponse(Call<FollowUserResponseModel> call, Response<FollowUserResponseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().isResult()) {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        if (response.body().getMessage().trim().equals("Follow Successfully")) {
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                           /* Drawable buttonDrawable = followingBtn.getBackground();
                            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                            DrawableCompat.setTint(buttonDrawable, Color.TRANSPARENT);
                            holder.followingBtn.setBackground(buttonDrawable);
                            holder.followingBtn.setText("Following");
                            holder.followingBtn.setTextColor(Color.BLACK);*/

                        } else {

                          /*  Drawable buttonDrawable = holder.followingBtn.getBackground();
                            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                            DrawableCompat.setTint(buttonDrawable, Color.parseColor("#ED4D4D"));
                            holder.followingBtn.setBackground(buttonDrawable);
                            holder.followingBtn.setText("Follow");
                            holder.followingBtn.setTextColor(Color.WHITE);*/

                        }

                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }

            }

            @Override
            public void onFailure(Call<FollowUserResponseModel> call, Throwable t) {
                Toast.makeText(context, "Throwable " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}



