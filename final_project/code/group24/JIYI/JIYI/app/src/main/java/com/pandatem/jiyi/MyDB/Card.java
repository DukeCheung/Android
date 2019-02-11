package com.pandatem.jiyi.MyDB;


import android.graphics.Bitmap;

import com.amap.api.maps.model.LatLng;

import java.io.Serializable;

public class Card implements Serializable {
    private Integer id;
    private Boolean isPrivate;
    private String cover;
    private byte[] coverBitmapBytes;
    private String title;
    private String content;
    private Person person;
    private double latLng_x;
    private double latLng_y;
    private String position;

    public Card(){
        id=0;
        cover="Testcover";
        title="TestTitle";
        content="一段迹忆，一段向往一段迹忆，一段向往一段迹忆，一段向往一段迹忆，" +
                "一段向往一段迹忆，一段向往一段迹忆，一段向往一段迹忆，一段向往" +
                "一段迹忆，一段向往一段迹忆，一段向往一段迹忆，一段向往一段迹忆，" +
                "一段向往一段迹忆，一段向往一段迹忆，一段向往一段迹忆，一段向往一" +
                "段迹忆，一段向往一段迹忆，一段向往一段迹忆，一段向往一段迹忆，一" +
                "段向往一段迹忆，一段向往一段迹忆，一段向往一段迹忆，一段向往一段迹忆，" +
                "一段向往一段迹忆，一段向往一段迹忆，一段向往一段迹忆，一段向往一段迹忆，" +
                "一段向往一段迹忆，一段向往一段迹忆，一段向往一段迹忆，一段向往一段迹忆" +
                "，一段向往一段迹忆，一段向往一段迹忆，一段向往一段迹忆，一段向往一段迹忆，" +
                "一段向往一段迹忆，一段向往一段迹忆，一段向往一段迹忆，一段向往一段迹忆，" +
                "一段向往一段迹忆";
        person = new Person();
        position="广州 中山大学";
        isPrivate = false;
    }

    public Card(Integer id, String cover, String title, String content, Person person,
                double latLng_x, double latLng_y ,String position, boolean isPrivate,
                byte[] coverBitmapBytes){
        this.id = id;
        this.cover = cover;
        this.title = title;
        this.content = content;
        this.person = person;
        this.latLng_x = latLng_x;
        this.latLng_y = latLng_y;
        this.position = position;
        this.isPrivate = isPrivate;
        this.coverBitmapBytes = coverBitmapBytes;
    }


    public Integer getId() {
        return id;
    }

    public String getCover() {
        return cover;
    }

    public String getContent() {
        return content;
    }

    public double getLatLng_x() {
        return latLng_x;
    }

    public double getLatLng_y() {
        return latLng_y;
    }


    //    public Bitmap getCoverBitmap() {
//        return coverBitmap;
//    }

//    public LatLng getLatLng() {
//        return latLng;
//    }


    public byte[] getCoverBitmapBytes() {
        return coverBitmapBytes;
    }

    public Person getPerson() {
        return person;
    }

    public String getTitle() {
        return title;
    }

    public String getPosition() {
        return position;
    }


    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

//    public void setCoverBitmap(Bitmap coverBitmap) {
//        this.coverBitmap = coverBitmap;
//    }

//    public void setLatLng(LatLng latLng) {
//        this.latLng = latLng;
//    }


    public void setCoverBitmapBytes(byte[] coverBitmapBytes) {
        this.coverBitmapBytes = coverBitmapBytes;
    }

    public void setLatLng_x(double latLng_x) {
        this.latLng_x = latLng_x;
    }

    public void setLatLng_y(double latLng_y) {
        this.latLng_y = latLng_y;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }
}

