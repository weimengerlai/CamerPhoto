package com.example.apple.photoutil;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.apple.photoutil.adapter.AdapterGridview;
import com.example.apple.photoutil.base.BaseActivity;
import com.example.apple.photoutil.cache.DownImageThread;
import com.example.apple.photoutil.entity.ImageUrl;
import com.example.apple.photoutil.util.ToastUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//相册类，用来获取系统的相册图片
public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    //加载图片的数据list
    List<ImageUrl> imageUrlList = new ArrayList<ImageUrl>();
    //放相册数量的gridview
    GridView grid_image_list;
    //加载数据的adapter
    AdapterGridview adapterGridview;
    //发送图片的按钮
    TextView tv_lookbigimage_send;
    //发送给界面的list
    List<ImageUrl> sendlist = new ArrayList<ImageUrl>();
    //下载完成的图片的个数
    int downNumber = 0;
    //下载完成的list
    List<ImageUrl> success = new ArrayList<ImageUrl>();


    //主线程更新UI
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1://更新主线程的数据
                    adapterGridview.addrest(imageUrlList);
                    break;
                case 2://更新主线程选择个数
                    tv_lookbigimage_send.setText("发送(" + sendlist.size() + ")");
                    break;
                case 3://下载完成

                    if (downNumber == sendlist.size() - 1) {

                        closeProgressDialog();
                        ImageUrl imageUrl = new ImageUrl();
                        imageUrl.setImageurl(msg.obj.toString());
                        success.add(imageUrl);


                        Intent intent = new Intent();
                        intent.putExtra("data", (Serializable) success);
                        setResult(1, intent);
                        finish();

                    } else {
                        ImageUrl imageUrl = new ImageUrl();
                        imageUrl.setImageurl(msg.obj.toString());
                        success.add(imageUrl);
                        downNumber++;
                    }

                    break;
                case 4://下载失败

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidget();
        setWidgetState();
        SearchPhotoDb();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void initHeader() {

    }

    //初始化控件的方法
    @Override
    public void initWidget() {
        grid_image_list = (GridView) findViewById(R.id.grid_image_list);
        tv_lookbigimage_send = (TextView) findViewById(R.id.tv_lookbigimage_send);
        adapterGridview = new AdapterGridview(MainActivity.this);
        grid_image_list.setAdapter(adapterGridview);

        //删除用过的图片。
        File file = new File(PhotoApplication.getImgDir()
                + PhotoApplication.IMAGE_SENCODE);
        file.delete();
    }

    //设置监听的方法
    @Override
    public void setWidgetState() {
        tv_lookbigimage_send.setOnClickListener(this);
    }


    //查找数据库的方法
    public void SearchPhotoDb() {

        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Images.Media.DATE_MODIFIED + " DESC");
        //初始化游标的位置
        cursor.moveToFirst();
        //获取没一条数据的链接
        while (cursor.moveToNext()) {

            //获取图片的显示名
            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            //获取图片的详细描述
            String desc = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DESCRIPTION));
            //获取图片的路径
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));


            //判断是不是图片 .png 或者.jpg
            if (path.substring(path.length() - 4, path.length()).equals(".png")
                    || path.substring(path.length() - 4, path.length()).equals(".jpg")) {

                ImageUrl imageUrl = new ImageUrl();
                imageUrl.setImageurl(path);
                imageUrl.setIsselect(0);
                imageUrl.setDesc(desc);
                imageUrl.setName(name);
                imageUrlList.add(imageUrl);

            }
        }

        sendMessage(1);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {

            case R.id.grid_image_list:

                if (imageUrlList.get(position).getIsselect() == 0) {
                    imageUrlList.get(position).setIsselect(1);
                } else {
                    imageUrlList.get(position).setIsselect(0);
                }

                sendMessage(1);

                break;
        }
    }

    //改变图片相册的选中状态
    public void setStaute(List<ImageUrl> imageUrlList, int position) {
        this.imageUrlList = imageUrlList;
        if (imageUrlList.get(position).getIsselect() == 0) {
            sendlist.remove(imageUrlList.get(position));
        } else {
            sendlist.add(imageUrlList.get(position));
        }
        sendMessage(2);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_lookbigimage_send://发送按钮


                if (sendlist.size() > 0) {

                    showProgressDialog();
                    ExecutorService executorService = Executors.newFixedThreadPool(6);
                    for (int i = 0; i < sendlist.size(); i++) {

                        executorService.submit(new DownImageThread(sendlist.get(i).getImageurl(), handler, i));
                    }

                } else {
                    ToastUtils.show(getApplicationContext(), "请选择图片");
                }
                break;
        }
    }


    //发送消息到主线程
    public void sendMessage(int type) {

        Message msg = handler.obtainMessage();
        msg.what = type;
        handler.sendMessage(msg);

    }


}
