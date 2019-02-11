package com.example.zhangxing.storage2;

import android.graphics.Bitmap;

public class Member {
    private String username;
    private String password;
    private String phoneNumber;
    private Bitmap portrait;//头像
    public Member(){
        username = null;
        password = null;
        phoneNumber = null;
        portrait = null;
    }
    public Member(String username, String password, String phoneNumber, Bitmap portrait){
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.portrait = portrait;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPortrait(Bitmap portrait) {
        this.portrait = portrait;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Bitmap getPortrait() {
        return portrait;
    }
}
