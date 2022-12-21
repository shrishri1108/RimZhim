package com.rimzhim.Sticker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.rimzhim.Adapters.galleryVideosAdapter;
import com.rimzhim.BuildConfig;
import com.rimzhim.Fragments.ProfileFragment;
import com.rimzhim.Interfaces.StickerOnClick;
import com.rimzhim.MainMenu.MainMenuFragment;
import com.rimzhim.R;

import java.util.ArrayList;

public class stickerDialog {

    public static  void openStickerDialog(Context context){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogStyle);
        bottomSheetDialog.setContentView(R.layout.sticker_dialog);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        RecyclerView StickerView =bottomSheetDialog.findViewById(R.id.stickersView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,3);
        StickerView.setLayoutManager(gridLayoutManager);
        /*StickerAdapter adapter = new StickerAdapter(context, AddStickers(context), stickerDialog.this);
        StickerView.setAdapter(adapter);*/

        bottomSheetDialog.show();
    }

    private static ArrayList<StickersModel> AddStickers(Context context) {
        ArrayList<StickersModel> list = new ArrayList<>();
        StickersModel stickersModel = new StickersModel();
        stickersModel.setSticker(R.raw.giphy);
        stickersModel.setName("ha");
        list.add(stickersModel);

        StickersModel stickersModel1 = new StickersModel();
        stickersModel1.setSticker(R.raw.giphy2);
        stickersModel1.setName("ha");
        list.add(stickersModel1);

        StickersModel stickersModel2 = new StickersModel();
        stickersModel2.setSticker(R.raw.giphy3);
        stickersModel2.setName("ha");
        list.add(stickersModel2);

        StickersModel stickersModel3 = new StickersModel();
        stickersModel3.setSticker(R.raw.giphy4);
        stickersModel3.setName("ha");
        list.add(stickersModel3);

        StickersModel stickersModel4 = new StickersModel();
        stickersModel4.setSticker(R.raw.love1);
        stickersModel4.setName("ha");
        list.add(stickersModel4);

        StickersModel stickersModel5 = new StickersModel();
        stickersModel5.setSticker(R.raw.love2);
        stickersModel5.setName("ha");
        list.add(stickersModel5);

        StickersModel stickersModel6 = new StickersModel();
        stickersModel6.setSticker(R.raw.h1);
        stickersModel6.setName("ha");
        list.add(stickersModel6);

        StickersModel stickersModel7 = new StickersModel();
        stickersModel7.setSticker(R.raw.h2);
        stickersModel7.setName("ha");
        list.add(stickersModel7);

        StickersModel stickersModel8 = new StickersModel();
        stickersModel8.setSticker(R.raw.h3);
        stickersModel8.setName("ha");
        list.add(stickersModel8);

        StickersModel stickersModel9 = new StickersModel();
        stickersModel9.setSticker(R.raw.d1);
        stickersModel9.setName("ha");
        list.add(stickersModel9);

        StickersModel stickersModel10 = new StickersModel();
        stickersModel10.setSticker(R.raw.d2);
        stickersModel10.setName("ha");
        list.add(stickersModel10);

        StickersModel stickersModel11 = new StickersModel();
        stickersModel11.setSticker(R.raw.d3);
        stickersModel11.setName("ha");
        list.add(stickersModel11);



       return list;

    }


}
