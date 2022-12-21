package com.rimzhim.Chats;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import com.rimzhim.Adapters.BaseViewHolder;
import com.rimzhim.Chats.ViewImageActivity;
import com.rimzhim.ModelClasses.FollowUserResponseModel;


import com.rimzhim.ModelClasses.GetChats.DataItem;
import com.rimzhim.ModelClasses.LoginResponse;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SharedPres.SharedPrefManager;
import com.rimzhim.SharedPres.loginResponsePref;
import com.rimzhim.SimpleClasses.Functions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatingAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int MSG_TYPR_RIGHT = 1;
    private static final int MSG_TYPE_LEFT = 2;

    private boolean isLoaderVisible = false;
    Context context;
    List<DataItem> mPostItems;
    String imageurl;

    public ChatingAdapter(Context context, List<DataItem> mPostItems, String imageurl) {
        this.context = context;
        this.mPostItems = mPostItems;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case MSG_TYPR_RIGHT:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat_right, parent, false));
            case MSG_TYPE_LEFT:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat_left, parent, false));
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
            if (position == mPostItems.size() - 1) {
                return VIEW_TYPE_LOADING;
            } else {
                LoginResponse.User user = SharedPrefManager.getInstance(context).getUser();
                String userId =    String.valueOf(user.getId()).trim();
                if (mPostItems.get(position).getSender_id().equals(userId)) {
                    return MSG_TYPR_RIGHT;
                } else {
                    return MSG_TYPE_LEFT;
                }
            }

        } else {
            LoginResponse.User user = SharedPrefManager.getInstance(context).getUser();
            String userId =    String.valueOf(user.getId()).trim();
            if (mPostItems.get(position).getSender_id().equals(userId)) {
                return MSG_TYPR_RIGHT;
            } else {
                return MSG_TYPE_LEFT;
            }
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

    public void addAtBottom(DataItem postItems) {
        mPostItems.add(0,postItems);
        notifyDataSetChanged();
    }

    public void AddComingMassages(List<DataItem> postItems){
        LoginResponse.User user = SharedPrefManager.getInstance(context).getUser();
        String userId =    String.valueOf(user.getId()).trim();
        mPostItems = postItems;
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
        CircleImageView image;
        ImageView mimage;
        TextView message, time, isSee;
        LinearLayout msglayput;




        ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.profilec);
            message = itemView.findViewById(R.id.msgc);
            time = itemView.findViewById(R.id.timetv);
            isSee = itemView.findViewById(R.id.isSeen);
            msglayput = itemView.findViewById(R.id.msglayout);
            mimage = itemView.findViewById(R.id.images);



        }


        protected void clear() {


        }

        public void onBind(int position) {
            super.onBind(position);
            String messagee = mPostItems.get(position).getText();
            String timeStamp = mPostItems.get(position).getTime();
            String type = mPostItems.get(position).getFile_type();
            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
            //  calendar.setTimeInMillis(Long.parseLong(timeStamp));
            // String timedate = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
            message.setText(messagee);
            time.setText(timeStamp);

            isSee.setText(mPostItems.get(position).getStatus());



            try {
                Glide.with(context).load(imageurl).placeholder(R.drawable.ic_user_icon).into(image);
            } catch (Exception e) {

            }
            if (type.equals("text")) {
                message.setVisibility(View.VISIBLE);
                mimage.setVisibility(View.GONE);
                message.setText(messagee);

                Log.d("pos======", messagee+ "  " + String.valueOf(position));
            } else {
                message.setVisibility(View.GONE);
                mimage.setVisibility(View.VISIBLE);
                Glide.with(context).load(messagee).placeholder(R.drawable.chat_image_place_holder).into(mimage);
            }

            mimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(time.getContext(),ViewImageActivity.class);
                    intent.putExtra("img",messagee);
                    time.getContext().startActivity(intent);
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



