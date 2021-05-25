package com.eatx.wdj.data;

import android.app.Dialog;
import android.content.Context;
import com.eatx.wdj.R;
public class Util {
    // ...
    public static Dialog getCustomDialog(Context context, int layout) {
        Dialog dialog = new Dialog(context, R.style.FullHeightDialog);
        dialog.setContentView(layout);
        return dialog;
    }
    // ...
}