package com.example.apple.photoutil.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.apple.photoutil.MainActivity;
import com.example.apple.photoutil.PhotoApplication;
import com.example.apple.photoutil.R;
import com.example.apple.photoutil.base.BaseActivity;
import com.example.apple.photoutil.cache.DownImageThread;
import com.example.apple.photoutil.entity.ImageUrl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 作    者：高学军
 * 时    间：16/3/20
 * 描    述：
 * 修改时间：
 */
public class TestActivity extends BaseActivity implements View.OnClickListener {

    final int RESULT_CODE = 101;
    final int REQUEST_CODE = 1;
    static final int REQUEST_TAKE_PHOTO = 2;

    Button btn_activity_test;
    TextView tv_activity_test;
    //拍照获取图片
    Button btn_activity_cammer;
    //拍照路径
    String mCurrentPhotoPath;

    //相册和拍照的总图片数量(为二次采样过后的图，可以直接上传服务器)
    ArrayList<ImageUrl> imageUrlArrayList = new ArrayList<ImageUrl>();


    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 3://照片下载成功

                    closeProgressDialog();
                    ImageUrl imageUrl = new ImageUrl();
                    imageUrl.setImageurl(msg.obj.toString());
                    imageUrlArrayList.add(imageUrl);
                    tv_activity_test.setText("图片的数量为" + imageUrlArrayList.size() + "张");

                    Log.d("imageurl", msg.obj.toString());
                    break;
                case 4://照片下载失败

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initHeader();
        initWidget();
        setWidgetState();
    }

    @Override
    public void initHeader() {

    }

    @Override
    public void initWidget() {

        btn_activity_test = (Button) findViewById(R.id.btn_activity_test);
        tv_activity_test = (TextView) findViewById(R.id.tv_activity_test);
        btn_activity_cammer = (Button) findViewById(R.id.btn_activity_cammer);

        //删除以前用过的拍照的图片
        File file = PhotoApplication.getImgDir();
        file.delete();
    }

    @Override
    public void setWidgetState() {

        btn_activity_test.setOnClickListener(this);
        btn_activity_cammer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_activity_test://相册

                Intent intent = new Intent(TestActivity.this, MainActivity.class);
                startActivityForResult(intent, RESULT_CODE);

                break;
            case R.id.btn_activity_cammer://拍照
                try {
                    dispatchTakePictureIntent();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("eeee", e.toString());
                }
                break;
        }
    }


    //返回图片的数量
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RESULT_CODE) {
            if (resultCode == REQUEST_CODE) {//相册获取照片的方法
                ArrayList<ImageUrl> imageUrlArrayList1 = (ArrayList<ImageUrl>) data.getSerializableExtra("data");
                imageUrlArrayList.addAll(imageUrlArrayList1);
                tv_activity_test.setText("图片的数量为" + imageUrlArrayList.size() + "张");
            } else if (resultCode == RESULT_OK) {//拍照获取图片

                showProgressDialog();
                ExecutorService executorService = Executors.newFixedThreadPool(6);
                executorService.submit(new DownImageThread(mCurrentPhotoPath, handler, imageUrlArrayList.size()));

            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    //调用拍照功能
    private void dispatchTakePictureIntent() throws IOException {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调用android自带的照相机
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(createImageFile()));
        startActivityForResult(intent, RESULT_CODE);

    }


    //获取拍照后储存的路径
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File file = PhotoApplication.getImgDir();

        file.mkdirs();

        mCurrentPhotoPath = file + "/" + timeStamp + ".png";
        return new File(mCurrentPhotoPath);
    }


}
