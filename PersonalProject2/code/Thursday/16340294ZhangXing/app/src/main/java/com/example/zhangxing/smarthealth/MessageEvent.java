package com.example.zhangxing.smarthealth;

public class MessageEvent{
    Food food;
    public MessageEvent(Food f){food = f;}
    public Food getFood(){
        return food;
    }
}