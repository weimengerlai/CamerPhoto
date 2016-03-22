package com.example.apple.photoutil;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

/**
 * 作    者：高学军
 * 时    间：16/3/20
 * 描    述：
 * 修改时间：
 */
public class PhotoApplication extends Application {

    //下载路径
    public static File fBaseDir;

    //上下文
    public static Context context;

    //下载二次采样图片的文件目录
    public static final String IMAGE_SENCODE = "image_sencod";

    //

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        getBaseDir();
    }


    //获取SD卡的路径
    public static void getBaseDir() {


        // 下载到SD卡
        if (Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            fBaseDir = context
                    .getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        } else {
            fBaseDir = new File(PhotoApplication.context.getFilesDir(),
                    Environment.DIRECTORY_DOWNLOADS);
        }
        if (fBaseDir != null) {
            if (!fBaseDir.exists() && !fBaseDir.mkdir()) {
                Toast.makeText(context, "缓存目录不能写", Toast.LENGTH_SHORT).show();
            }

            if (!fBaseDir.isDirectory()) {
                Toast.makeText(context, "缓存目录名已存在但不是目录", Toast.LENGTH_SHORT)
                        .show();
            }
        }


    }

    public static File getImgDir() {
        File dir = new File(fBaseDir, "img");
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir;
    }
}
