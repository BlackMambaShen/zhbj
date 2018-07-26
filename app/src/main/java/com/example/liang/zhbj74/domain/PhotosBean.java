package com.example.liang.zhbj74.domain;

import java.util.ArrayList;

/*
组图对象
 */
public class PhotosBean {
    public PhotoData data;

    public class PhotoData{
        public ArrayList<PhotoNews>news;
    }

    public class PhotoNews{
        public int id;
        public String listimage;
        public String title;
    }
}
