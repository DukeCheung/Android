package com.example.zhangxing.smarthealth;

import java.io.Serializable;

public class Food implements Serializable{
    private String name;
    private String title;
    private String type;
    private String nutrition;
    private String color;

    public Food(){
        this.name = null;
        this.title = null;
        this.type = null;
        this.nutrition = null;
        this.color = null;
    }
    public Food(String name, String title, String type, String nutrition, String color){
        this.name = name;
        this.title = title;
        this.type = type;
        this.nutrition = nutrition;
        this.color = color;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setNutrition(String nutrition) {
        this.nutrition = nutrition;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getNutrition() {
        return nutrition;
    }

    public String getColor() {
        return color;
    }
}
