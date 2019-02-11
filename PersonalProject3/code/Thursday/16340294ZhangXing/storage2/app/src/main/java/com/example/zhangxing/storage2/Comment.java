package com.example.zhangxing.storage2;

public class Comment {
    private Integer id;
    private String username;
    private String time;
    private String comment;
    private Integer count;
    private Integer like;
    public Comment(){
        id = 0;
        username = null;
        time = null;
        comment = null;
        count = 0;
        like = 0;
    }
    public Comment(String username, String time, String comment, Integer count, Integer like){
        this.username = username;
        this.time = time;
        this.comment = comment;
        this.count = count;
        this.like = like;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getTime() {
        return time;
    }

    public String getComment() {
        return comment;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getLike() {
        return like;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setLike(Integer like) {
        this.like = like;
    }
}
