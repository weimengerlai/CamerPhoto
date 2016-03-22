package com.example.apple.photoutil.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    public static Toast mToast;
    public static void show(Context context, String info) {
        if (null != context && null != info && !info.equals("")) {
            Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showT(Context context,String msg){
        if (mToast == null) {
            mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();
    }
    public static void show(Context context, int info) {
            Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
    }
}
