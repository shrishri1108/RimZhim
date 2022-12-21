package com.rimzhim.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;

import com.rimzhim.Activities.JoinContestActivity;
import com.rimzhim.R;


public class contestBookedDialog {
    public static void OpenContestBookedDialog(Context context, String contest_id){
        Dialog mdialog;
        mdialog = new Dialog(context);


        mdialog.setContentView(R.layout.contest_booked_dialog);
        mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mdialog.setCancelable(false);
        Button doneBTN = mdialog.findViewById(R.id.doneBTN);
        doneBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdialog.dismiss();
                Intent intent = new Intent(context, JoinContestActivity.class);
                intent.putExtra("contest_id",contest_id);
                context.startActivity(intent);

            }
        });
        mdialog.show();


    }
}
