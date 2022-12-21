package com.rimzhim.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.rimzhim.Activities.JoinContestActivity;
import com.rimzhim.Activities.VideoGalleryActivity;
import com.rimzhim.Activities.VideoRecording.VideoRecoderA;
import com.rimzhim.Activities.videorecorder.activity.RecorderActivity;
import com.rimzhim.ModelClasses.ContestResponse.DataItem;
import com.rimzhim.R;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.dialogs.contestBookedDialog;
import com.rimzhim.dialogs.createVideoDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class contestAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    private  List<DataItem> mPostItems;
    Context context;

    public contestAdapter(List<DataItem> mPostItems, Context context) {
        this.mPostItems = mPostItems;
        this.context = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_upcoming_view, parent, false));
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
        int totalContestant, leftContestant;
        CardView cardView;
        TextView winngPriceAmountTv, contestantsCount, totalContestantsCount, date, time;
        Button bookContestsBtn, contestsAmount;
        android.widget.ProgressBar ProgressBar;


        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            winngPriceAmountTv = itemView.findViewById(R.id.winngPriceAmountTv);
            bookContestsBtn = itemView.findViewById(R.id.bookContestsBtn);
            contestsAmount = itemView.findViewById(R.id.contestsAmount);
            contestantsCount = itemView.findViewById(R.id.contestantsCount);
            totalContestantsCount = itemView.findViewById(R.id.totalContestantsCount);
            date = itemView.findViewById(R.id.date);
          //  time = itemView.findViewById(R.id.time);
            ProgressBar = itemView.findViewById(R.id.ProgressBar);
        }



        protected void clear() {
        }
        public void onBind(int position) {
            super.onBind(position);
            DataItem temp = mPostItems.get(position);
            winngPriceAmountTv.setText(context.getString(R.string.rupee)+temp.getWinning_amount_in_text());
            contestsAmount.setText(context.getString(R.string.rupee)+String.valueOf(temp.getEntry_amount()));
            Log.d("======>>", String.valueOf(temp.getEntry_amount()));
            date.setText(temp.getStart_date_time());
            totalContestantsCount.setText(temp.getSpots()+" Total Spots");

            bookContestsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //contestBookedDialog.openAccountReadyDialog(date.getContext(),String.valueOf(temp.getId()));
                    Intent intent = new Intent(date.getContext(), JoinContestActivity.class);
                    intent.putExtra("contest_id",String.valueOf(temp.getId()));
                    date.getContext().startActivity(intent);
                }
            });

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ///contestBookedDialog.openAccountReadyDialog(date.getContext(),String.valueOf(temp.getId()));
                    Intent intent = new Intent(date.getContext(), JoinContestActivity.class);
                    intent.putExtra("contest_id",String.valueOf(temp.getId()));
                    date.getContext().startActivity(intent);
                }
            });

            contestsAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogStyle);
                    bottomSheetDialog.setContentView(R.layout.create_video_dialog);
                    bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    LinearLayout createLL = bottomSheetDialog.findViewById(R.id.createLL);
                    LinearLayout uploadLL = bottomSheetDialog.findViewById(R.id.uploadLL);

                    createLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bottomSheetDialog.dismiss();
                            Intent intent = new Intent(context, RecorderActivity.class);
                            context.startActivity(intent);


                        }
                    });

                    uploadLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Intent intent = new Intent(context, uploadPreActivity.class);
                            // context.startActivity(intent);

                            Intent intent = new Intent(context, VideoGalleryActivity.class);
                            intent.putExtra("entryAmount", temp.getEntry_amount());
                            intent.putExtra("contestId",temp.getId());
                            context.startActivity(intent);
                            bottomSheetDialog.dismiss();
                        }
                    });
                    bottomSheetDialog.show();

                   // createVideoDialog.openCreateVideoDialog(date.getContext());
                }
            });


            if(temp.getFilled_spot() != null){
                totalContestant = Integer.parseInt(temp.getSpots());
                leftContestant = Integer.parseInt(temp.getFilled_spot());
                leftContestant = totalContestant -  leftContestant;
                ProgressBar.setProgress(Functions.showProgress(totalContestant,leftContestant));
                contestantsCount.setText(String.valueOf(leftContestant)+" Spots left");
                Log.d("==========", String.valueOf(Functions.showProgress(totalContestant,leftContestant)));

            }else {
                // holder.contestantsCount.setText(String.valueOf(holder.leftContestant)+" Contestants left");
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


