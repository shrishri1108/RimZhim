package com.rimzhim.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.rimzhim.R;


public class congratualation_dialog {

    public static void openCongratualation_dialog(Context context) {
        Dialog mdialog;
        mdialog = new Dialog(context);
        mdialog.setContentView(R.layout.congratualation_dialog);
        mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mdialog.show();
    }
}
