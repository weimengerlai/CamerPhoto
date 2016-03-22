package com.example.apple.photoutil.cache;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class CacheImageAsyncTask extends AsyncTask<String, Integer, Bitmap>{
    private ImageView ima;
    private ImageFileCache fileCache;
    private ImageMemoryCache memoryCache;
    public CacheImageAsyncTask(ImageView ima,Context context){
        this.ima=ima;
        fileCache=new ImageFileCache();
        memoryCache=new ImageMemoryCache(context);
    }
    public Bitmap getBitmap(String url) {
        ///
        // 从内存缓存中获取图片

        Bitmap result = memoryCache.getBitmapFromCache(url);
        if (result == null) {
            // 文件缓存中获取
            result = fileCache.getImage(url);
            if (result == null) {
                // 从网络获取
                result = ImageGetFromHttp.downloadBitmap(url);
                if (result != null) {
                    fileCache.saveBitmap(result, url);
                    memoryCache.addBitmapToCache(url, result);
                }
            } else {
                // 添加到内存缓存
                memoryCache.addBitmapToCache(url, result);
            }
        }
        return result;
    }

    //在后台执行
    protected Bitmap doInBackground(String... params) {

        return getBitmap(params[0]);
    }
    //主线程执行
    @Override
    protected void onPostExecute(Bitmap result) {
        ima.setImageBitmap(result);
    }
}