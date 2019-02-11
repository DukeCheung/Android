package com.example.zhangxing.webapi;

import android.graphics.Bitmap;

import java.util.List;

public class ImageObj {
    private int code;
    private String message;
    private int ttl;
    private Data data;
    private Bitmap bitmap;
    public class Data {

        private String pvdata;
        private int img_x_len;
        private int img_y_len;
        private int img_x_size;
        private int img_y_size;
        private List<String> image;
        private List<Integer> index;

        public void setPvdata(String pvdata) {
            this.pvdata = pvdata;
        }
        public String getPvdata() {
            return pvdata;
        }

        public void setImg_x_len(int img_x_len) {
            this.img_x_len = img_x_len;
        }
        public int getImg_x_len() {
            return img_x_len;
        }

        public void setImg_y_len(int img_y_len) {
            this.img_y_len = img_y_len;
        }
        public int getImg_y_len() {
            return img_y_len;
        }

        public void setImg_x_size(int img_x_size) {
            this.img_x_size = img_x_size;
        }
        public int getImg_x_size() {
            return img_x_size;
        }

        public void setImg_y_size(int img_y_size) {
            this.img_y_size = img_y_size;
        }
        public int getImg_y_size() {
            return img_y_size;
        }

        public void setImage(List<String> image) {
            this.image = image;
        }
        public List<String> getImage() {
            return image;
        }

        public void setIndex(List<Integer> index) {
            this.index = index;
        }
        public List<Integer> getIndex() {
            return index;
        }

    }

    public Data getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public int getTtl() {
        return ttl;
    }

    public String getMessage() {
        return message;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
