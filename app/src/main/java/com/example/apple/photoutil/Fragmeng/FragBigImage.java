package com.example.apple.photoutil.Fragmeng;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.apple.photoutil.R;
import com.example.apple.photoutil.cache.CacheImageAsyncTask;

/**
 * 作    者：高学军
 * 时    间：16/3/18
 * 描    述：
 * 修改时间：
 */
public class FragBigImage extends BaseFragment {

    //得到加载数据的对象
    public static final String FRAGMENT_IMAEURL = "fragment_imageurl";
    //加载数据的对象
    String imageUrl;
    //看大图
    ImageView iv_fragment_big_image;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_big_image, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        imageUrl = getArguments().getString(FRAGMENT_IMAEURL);
        initWidget();
    }


    @Override
    public void initHeader() {

    }

    @Override
    public void initWidget() {
        iv_fragment_big_image = (ImageView) getView().findViewById(R.id.iv_fragment_big_image);


        //加载图片
//        Ion.with(getActivity())
//                .load(imageUrl)
//                .withBitmap()
//                .placeholder(R.drawable.load_img)
//                .error(R.drawable.load_img)
//                .intoImageView(iv_fragment_big_image);

        new CacheImageAsyncTask(iv_fragment_big_image, getActivity()).execute(imageUrl);
        iv_fragment_big_image.setOnClickListener(new ImageClickListener());
    }

    @Override
    public void setWidgetState() {

    }

    class ImageClickListener implements View.OnClickListener {


        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.iv_fragment_big_image:
                    getActivity().finish();
                    break;
            }
        }
    }
}
