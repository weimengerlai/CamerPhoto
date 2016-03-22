package com.example.apple.photoutil.entity;

import java.io.Serializable;

/**
 * 作    者：高学军
 * 时    间：16/3/18
 * 描    述：
 * 修改时间：
 */
public class ImageUrl implements Serializable{

    //url
    public String imageurl;
    //选中还是没选中(默认没有选中，选中为1)
    public int isselect;
    //图片描述
    public String desc;
    //图片的名字
    public String name;

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public int getIsselect() {
        return isselect;
    }

    public void setIsselect(int isselect) {
        this.isselect = isselect;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ImageUrl{" +
                "imageurl='" + imageurl + '\'' +
                ", isselect=" + isselect +
                ", desc='" + desc + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
