package com.example.apple.photoutil.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

/**
 * 作    者：高学军
 * 时    间：16/3/19
 * 描    述：
 * 修改时间：
 */
public class PhontImageView extends ImageView {


    public PhontImageView(Context context) {
        super(context);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        Log.d("heihtwidth", getHeight() + "==" + getWidth());

    }
}
