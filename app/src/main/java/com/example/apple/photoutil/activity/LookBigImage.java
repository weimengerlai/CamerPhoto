package com.example.apple.photoutil.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.apple.photoutil.Fragmeng.FragBigImage;
import com.example.apple.photoutil.R;
import com.example.apple.photoutil.base.BaseActivity;
import com.example.apple.photoutil.entity.ImageUrl;

import java.util.ArrayList;
import java.util.List;


/**
 * 作    者：高学军
 * 时    间：16/3/18
 * 描    述：
 * 修改时间：
 */
public class LookBigImage extends BaseActivity implements View.OnClickListener {

    //加载图片的list
    public static final String LOOK_BIG_IMAGE_LIST = "look_big_image_list";
    //当前是第几张图片
    public static final String LOOK_BIG_IMAGE_POSITION = "look_big_image_position";
    //数据
    List<ImageUrl> imageUrlList = new ArrayList<ImageUrl>();

    //看大图的viewpager
    ViewPager vp_look_big_image;
    //适配器用来加载图片
    TabPageIndicatorAdapter adapter;
    //当前是第几张图片
    private int position;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏

        setContentView(R.layout.activity_lookbigimage);
        imageUrlList = (List<ImageUrl>) getIntent().getSerializableExtra(LOOK_BIG_IMAGE_LIST);
        position = getIntent().getIntExtra(LOOK_BIG_IMAGE_POSITION, -1);

        initHeader();
        initWidget();
        setWidgetState();
    }

    @Override
    public void initHeader() {

    }

    @Override
    public void initWidget() {

        vp_look_big_image = (ViewPager) findViewById(R.id.vp_look_big_image);
        adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
        vp_look_big_image.setAdapter(adapter);
        adapter.addrst(imageUrlList);
        if (position != -1) {
            vp_look_big_image.setCurrentItem(position);
        }

    }

    @Override
    public void setWidgetState() {


    }

    @Override
    public void onClick(View v) {


    }


    //适配器用来加载需要加载的图片

    class TabPageIndicatorAdapter extends FragmentStatePagerAdapter {

        FragmentManager fm;
        List<ImageUrl> mImage = new ArrayList<ImageUrl>();

        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
        }

        public void addrst(List<ImageUrl> mImage) {
            this.mImage.clear();
            this.mImage.addAll(mImage);
            this.notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {

            FragBigImage mFragBigImage = new FragBigImage();
            Bundle bundle = new Bundle();
            bundle.putString(FragBigImage.FRAGMENT_IMAEURL, mImage.get(position).getImageurl());
            mFragBigImage.setArguments(bundle);

            return mFragBigImage;

        }

        @Override
        public int getCount() {
            return mImage.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            getItem(position);

            return super.instantiateItem(container, position);


        }

//        @Override
//        public int getItemPosition(Object object) {
//            if (object.getClass().getName().equals(Cottoms_Fragment.class.getName())
//                    || object.getClass().getName().equals(Cottoms_Fragment.class.getName())) {
//                return POSITION_NONE;
//            }
//            return super.getItemPosition(object);
//        }

    }
}
