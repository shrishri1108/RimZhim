package com.rimzhim.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;

import com.rimzhim.R;


public class accountReadyDialog {

    public static void openAccountReadyDialog(Context context){
        Dialog mdialog;
        mdialog = new Dialog(context);


        mdialog.setContentView(R.layout.popup);
        mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mdialog.setCancelable(false);
        Button popupBTN = mdialog.findViewById(R.id.popupBTN);
        popupBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdialog.dismiss();

            }
        });
        mdialog.show();


    }

}
