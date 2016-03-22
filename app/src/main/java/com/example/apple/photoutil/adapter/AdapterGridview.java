package com.example.apple.photoutil.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.apple.photoutil.MainActivity;
import com.example.apple.photoutil.R;
import com.example.apple.photoutil.activity.LookBigImage;
import com.example.apple.photoutil.entity.ImageUrl;
import com.koushikdutta.ion.Ion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作    者：高学军
 * 时    间：16/3/18
 * 描    述：
 * 修改时间：
 */
public class AdapterGridview extends BaseAdapter {

    //上下文
    MainActivity context;
    //加载图片的数据list
    List<ImageUrl> imageUrlList = new ArrayList<ImageUrl>();

    public AdapterGridview(MainActivity context) {
        this.context = context;
    }

    //更新数据的方法 先清除数据，再把数据加载到缓存数据里面
    public void addrest(List<ImageUrl> imageUrlList) {
        this.imageUrlList.clear();
        this.imageUrlList.addAll(imageUrlList);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imageUrlList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageUrlList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_gridview, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.iv_adapter_imagelist_photo = (ImageView) convertView.findViewById(R.id.iv_adapter_imagelist_photo);
            viewHolder.iv_adapter_imagelist_select = (ImageView) convertView.findViewById(R.id.iv_adapter_imagelist_select);
            viewHolder.iv_adapter_imagelist_select_big = (ImageView) convertView.findViewById(R.id.iv_adapter_imagelist_select_big);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        //加载图片
        Ion.with(context)
                .load(imageUrlList.get(position).getImageurl())
                .withBitmap()
                .placeholder(R.drawable.load_img)
                .error(R.drawable.load_img)
                .intoImageView(viewHolder.iv_adapter_imagelist_photo);

        switch (imageUrlList.get(position).getIsselect()) {
            case 0://没有被选中
                viewHolder.iv_adapter_imagelist_select.setImageResource(R.mipmap.patient_icon_unselect);
                break;
            case 1://被选中
                viewHolder.iv_adapter_imagelist_select.setImageResource(R.mipmap.patient_icon_select);
                break;
        }



        //设置选中或者取消图片的时候用
        viewHolder.iv_adapter_imagelist_select_big.setOnClickListener(new ImageClicklistener(viewHolder, position));
        //点击看大图
        viewHolder.iv_adapter_imagelist_photo.setOnClickListener(new ImageClicklistener(viewHolder, position));

        return convertView;
    }

    //viewHodler
    class ViewHolder {
        ImageView iv_adapter_imagelist_photo;
        ImageView iv_adapter_imagelist_select;
        ImageView iv_adapter_imagelist_select_big;
    }

    //设置监听，实现监听的方法
    class ImageClicklistener implements View.OnClickListener {

        ViewHolder viewHolder;
        int position;

        public ImageClicklistener(ViewHolder viewHolder, int position) {
            this.viewHolder = viewHolder;
            this.position = position;
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.iv_adapter_imagelist_select_big://设置选中状态，以及更新数据
                    if (imageUrlList.get(position).getIsselect() == 0) {
                        viewHolder.iv_adapter_imagelist_select.setImageResource(R.mipmap.patient_icon_select);
                        imageUrlList.get(position).setIsselect(1);
                        context.setStaute(imageUrlList,position);
                    } else {
                        viewHolder.iv_adapter_imagelist_select.setImageResource(R.mipmap.patient_icon_unselect);

                        imageUrlList.get(position).setIsselect(0);
                        context.setStaute(imageUrlList,position);
                    }

                    break;
                case R.id.iv_adapter_imagelist_photo://点击看大图

                    Intent intent = new Intent(context, LookBigImage.class);
                    intent.putExtra(LookBigImage.LOOK_BIG_IMAGE_LIST, (Serializable) imageUrlList);
                    intent.putExtra(LookBigImage.LOOK_BIG_IMAGE_POSITION,position);
                    context.startActivity(intent);

                    break;

            }
        }
    }
}
