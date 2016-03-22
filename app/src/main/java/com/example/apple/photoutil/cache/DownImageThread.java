package com.example.apple.photoutil.cache;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import com.example.apple.photoutil.PhotoApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 作    者：高学军
 * 时    间：16/3/21
 * 描    述：
 * 修改时间：
 */
public class DownImageThread extends Thread {

    //用来下载的url
    String url;
    //发送到主线程去更新UI的hander
    Handler handler;
    //当前下载的图片是第几张
    int current;

    public DownImageThread(String url, Handler handler, int current) {
        this.url = url;
        this.handler = handler;
        this.current = current;
    }

    @Override
    public void run() {
        super.run();

        getBitmap(url, current);
    }


    //发送消息到主线程
    public void sendMessage(int type, String file) {

        Message msg = handler.obtainMessage();
        msg.what = type;
        msg.obj = file;
        handler.sendMessage(msg);

    }


    //获取bitmap
    public String getBitmap(String url, int i) {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File file = new File(PhotoApplication.getImgDir()
                + PhotoApplication.IMAGE_SENCODE);
        file.mkdirs();
        String fileName = file + "/" + timeStamp + ".png";

        int gred = ImageUtil.readPictureDegree(url);

        return SaveSd((gred == 0 ? ImageUtil.convertBitmap(url, 720) : ImageUtil.adjustPhotoRotation(ImageUtil.convertBitmap(url, 720), gred)), new File(fileName));
    }

    //获取到的图片存储到本地
    public String SaveSd(Bitmap bitmap, File fileName) {

        FileOutputStream b = null;
        if (fileName.exists()) {
            fileName.delete();
        }


        try {
            b = new FileOutputStream(fileName);


            bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            sendMessage(4, fileName.toString());
            return fileName.toString();
        }

        try {
            b.flush();
            b.close();

            sendMessage(3, fileName.toString());
            return fileName.toString();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            sendMessage(4, fileName.toString());
            return fileName.toString();
        }
    }
}
