package com.example.aced;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Equipment implements Serializable {
    private String _equipmentName;
    private String _equipmentDescription;
    private int _equipmentId;
    private int _equipmentType;
    private int _equipmentPrice;
    private String _equipmentSkill;
    private byte[] _equipmentImage;


    public Equipment(){
        _equipmentImage = null;
        _equipmentSkill = "";
        _equipmentDescription="";
        _equipmentName="";
        _equipmentPrice=0;
        _equipmentId = 0;
        _equipmentType = 0;

    }
    public Equipment(String _equipmentName, String _equipmentDescription, byte[] _equipmentImage, int _equipmentId,
            int _equipmentType,int _equipmentPrice, String _equipmentSkill){
        this._equipmentName = _equipmentName;
        this._equipmentDescription = _equipmentDescription;
        this._equipmentImage = _equipmentImage;
        this._equipmentId = _equipmentId;
        this._equipmentType = _equipmentType;
        this._equipmentPrice = _equipmentPrice;
        this._equipmentSkill = _equipmentSkill;
    }

    public void setEuipmentSkill(String _equipmentSkill) {
        this._equipmentSkill = _equipmentSkill;
    }

    public void setEquipmentId(int _equipmentId) {
        this._equipmentId = _equipmentId;
    }

    public void setEquipmentType(int _equipmentType) {
        this._equipmentType = _equipmentType;
    }

    public void setEquipmentImage(byte[] _equipmentImage) {
        this._equipmentImage = _equipmentImage;
    }

    public void setEquipmentDescription(String _equipmentDescription) {
        this._equipmentDescription = _equipmentDescription;
    }

    public void setEquipmentName(String _equipmentName) {
        this._equipmentName = _equipmentName;
    }

    public void setEquipmentPrice(int _equipmentPrice) {
        this._equipmentPrice = _equipmentPrice;
    }

    public String getEquipmentSkill() {
        return _equipmentSkill;
    }

    public int getEquipmentId() {
        return _equipmentId;
    }

    public int getEquipmentType() {
        return _equipmentType;
    }

    public byte[] getEquipmentImage() {
        return _equipmentImage;
    }

    public int getEquipmentPrice() {
        return _equipmentPrice;
    }

    public String getEquipmentDescription() {
        return _equipmentDescription;
    }

    public String getEquipmentName() {
        return _equipmentName;
    }

}
