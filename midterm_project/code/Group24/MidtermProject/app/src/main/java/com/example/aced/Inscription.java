package com.example.aced;

import java.io.Serializable;

public  class Inscription implements Serializable {
    private Integer _inscriptionId;
    private String _inscriptionType;
    private Integer _inscriptionGrade;
    private String _inscriptionName;
    private String _inscriptionDescription;
    private byte[] _inscriptionImage;

    public Inscription(){

    }

    public Inscription(Integer _inscriptionId, String _inscriptionType, Integer _inscriptionGrade,
                       String _inscriptionName, String _inscriptionDescription, byte[] _inscriptionImage){
        this._inscriptionId = _inscriptionId;
        this._inscriptionType = _inscriptionType;
        this._inscriptionGrade = _inscriptionGrade;
        this._inscriptionName = _inscriptionName;
        this._inscriptionDescription = _inscriptionDescription;
        this._inscriptionImage = _inscriptionImage;
    }

    public Integer getInscriptionGrade() {
        return _inscriptionGrade;
    }

    public String getInscriptionDescription() {
        return _inscriptionDescription;
    }

    public String getInscriptionName() {
        return _inscriptionName;
    }

    public String getinscriptionType() { return _inscriptionType; }

    public Integer getinscriptionId() { return _inscriptionId; }


    public void setInscriptionDescription(String _inscriptionDescription) {
        this._inscriptionDescription = _inscriptionDescription;
    }

    public void setinscriptionGrade(int _inscriptionLevel) {
        this._inscriptionGrade = _inscriptionLevel;
    }

    public void setInscriptionName(String _inscriptionName) {
        this._inscriptionName = _inscriptionName;
    }

    public byte[] getInscriptionImage() { return _inscriptionImage; }
}
