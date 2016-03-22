package com.example.apple.photoutil.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 作    者：高学军
 * 时    间：16/3/19
 * 描    述：
 * 修改时间：
 */
public class ImageUtil {

    /**
     * 获取bitmap尺寸缩放解决OOM问题
     *
     * @throws IOException
     */
    public static Bitmap convertBitmap(String file, float maxSize) {
        Bitmap bitmap = null;
        try {
            //获得bitmap工厂编辑器的实例
            BitmapFactory.Options o = new BitmapFactory.Options();
            //设置进获取边界尺寸，不获取图片的大小
            o.inJustDecodeBounds = true;
            //从流中获取bitmap
            BitmapFactory.decodeStream(new FileInputStream(file), null, o);
            //获取bitmap的宽度和高度
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            //设置初始缩放率为整型值1；
            int scale = 2;
            //判断需要按那种比率进行等比例的缩放
            if (width_tmp > maxSize || height_tmp > maxSize) {
                if (width_tmp >= height_tmp)
                    scale = NumberUtil.convertFloatToInt(width_tmp / maxSize);
                else
                    scale = NumberUtil.convertFloatToInt(height_tmp / maxSize);
            }
            BitmapFactory.Options op = new BitmapFactory.Options();
            op.inSampleSize = scale;
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis, null, op);
            fis.close();

        } catch (FileNotFoundException e) {
            // e.printStackTrace();
        } catch (IOException e) {
            // e.printStackTrace();
        }

        return bitmap;
    }


    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return degree;
    }


    //将图片进行旋转
    public static Bitmap adjustPhotoRotation(Bitmap bm,
                                             final int orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2,
                (float) bm.getHeight() / 2);
        float targetX, targetY;
        if (orientationDegree == 90) {
            targetX = bm.getHeight();
            targetY = 0;
        } else {
            targetX = bm.getHeight();
            targetY = bm.getWidth();
        }
        final float[] values = new float[9];
        m.getValues(values);

        float x1 = values[Matrix.MTRANS_X];
        float y1 = values[Matrix.MTRANS_Y];

        m.postTranslate(targetX - x1, targetY - y1);

        Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(),
                Bitmap.Config.ARGB_8888);

        Paint paint = new Paint();
        Canvas canvas = new Canvas(bm1);
        canvas.drawBitmap(bm, m, paint);

        return bm1;
    }


    //将角度设置为0度
    public static void setPictureDegreeZero(String path) {
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            // 修正图片的旋转角度，设置其不旋转。这里也可以设置其旋转的角度，可以传值过去，
            // 例如旋转90度，传值ExifInterface.ORIENTATION_ROTATE_90，需要将这个值转换为String类型的
            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "no");
            exifInterface.saveAttributes();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
