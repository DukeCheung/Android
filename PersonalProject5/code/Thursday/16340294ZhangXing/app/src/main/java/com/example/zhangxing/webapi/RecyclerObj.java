package com.example.zhangxing.webapi;

import android.graphics.Bitmap;

public class RecyclerObj {
    private boolean status;
    private Data data;
    private ImageObj imageObj;
    private Bitmap bitmap;
    public static class Data{
        private int aid;
        private int state;
        private String cover;
        private String title;
        private String content;
        private int play;
        private String duration;
        private int video_review;
        private String create;
        private String rec;
        private int count;

        public String getTitle() {
            return title;
        }

        public int getAid() {
            return aid;
        }

        public int getPlay() {
            return play;
        }

        public int getState() {
            return state;
        }

        public int getCount() {
            return count;
        }

        public int getVideo_review() {
            return video_review;
        }

        public String getContent() {
            return content;
        }

        public String getCover() {
            return cover;
        }

        public String getCreate() {
            return create;
        }

        public String getDuration() {
            return duration;
        }

        public String getRec() {
            return rec;
        }

        public void setAid(int aid) {
            this.aid = aid;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public void setCreate(String create) {
            this.create = create;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public void setPlay(int play) {
            this.play = play;
        }

        public void setState(int state) {
            this.state = state;
        }

        public void setRec(String rec) {
            this.rec = rec;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setVideo_review(int video_review) {
            this.video_review = video_review;
        }
    }

    public Data getData() {
        return data;
    }

    public boolean getStatus(){
        return status;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ImageObj getImageObj() {
        return imageObj;
    }

    public void setImageObj(ImageObj imageObj) {
        this.imageObj = imageObj;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
