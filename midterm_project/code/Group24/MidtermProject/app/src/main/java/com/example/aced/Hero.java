package com.example.aced;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Hero implements Serializable {

    private int _heroId;
    private byte[] _icon;
    private String _name;
    private String _occupation;
    private String _positon;
    private String _type;
    private String _backgroundStory;
    private String _equipmentsTip;
    private List<Skill> _skills;
    private List<Equipment>_equipments;
    private List<Inscription>_inscriptions;
    private String _inscriptionsTip;
    private List <Integer> _abilities;


    public Hero(){
        _heroId=0;
        _name="";
        _occupation="";
        _positon="";
        _backgroundStory="";
        _skills = new ArrayList<Skill>();
        _equipments = new ArrayList<Equipment>();
        _inscriptions = new ArrayList<Inscription>();
        _type = "";
        _inscriptionsTip="";
        _equipmentsTip ="";
        _abilities=new ArrayList<>();
    }

    public Hero(Integer _heroId, String _name, String _occupation, String _positon, String _backgroundStory,
                List _skills, List _equipments, List _inscriptions, String _type, List _abilities, byte[] _icon){
        this._heroId = _heroId;
        this._name = _name;
        this._occupation = _occupation;
        this._positon = _positon;
        this._backgroundStory = _backgroundStory;
        this._skills = _skills;
        this._equipments = _equipments;
        this._inscriptions = _inscriptions;
        this._type = _type;
        this._abilities = _abilities;
        this._icon = _icon;
    }


    public void setId(int _id) {
        this._heroId = _id;
    }

    public void setIcon(byte[] _icon) {
        this._icon = _icon;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public void setOccupation(String _occupation) {
        this._occupation = _occupation;
    }

    public void setPositon(String _positon) {
        this._positon = _positon;
    }

    public void set_type(String _type) { this._type = _type; }

    public void setBackgroundStory(String _backgroundStory) {
        this._backgroundStory = _backgroundStory;
    }

    public void setSkills(List<Skill> _skills) {
        this._skills = _skills;
    }

    public void setEquipment(List<Equipment> _equipments) {
        this._equipments = _equipments;
    }

    public void setInscription(List<Inscription> _inscriptions) {
        this._inscriptions = _inscriptions;
    }

    public void setAbilities(List<Integer> _abilitys) {
        this._abilities = _abilitys;
    }

    public void setEquipmentsTip(String _equipmentsTip) {
        this._equipmentsTip = _equipmentsTip;
    }

    public void setInscriptionsTip(String _inscriptionTip) {
        this._inscriptionsTip = _inscriptionTip;
    }

    public int getId() {
        return _heroId;
    }

    public String getName() {
        return _name;
    }

    public byte[] getIcon() {
        return _icon;
    }

    public String getOccupation() {
        return _occupation;
    }

    public String getPositon() {
        return _positon;
    }

    public String get_type() { return _type; }

    public String getBackgroundStory() {
        return _backgroundStory;
    }

    public List<Skill> getSkills() {
        return _skills;
    }

    public List<Equipment> getEquipments() {
        return _equipments;
    }

    public List<Inscription> getInscriptions() {
        return _inscriptions;
    }

    public List<Integer> getAbilities() {
        return _abilities;
    }

    public String getEquipmentsTip() {
        return _equipmentsTip;
    }

    public String getInscriptionsTip() {
        return _inscriptionsTip;
    }

    /*
            public static class Equipment implements Serializable {
                private String _equipmentName;
                private String _equipmentDescription;
                private String _equipmentImage;
                private int _equipmentPrice;


                public Equipment(){
                    _equipmentImage = null;
                    _equipmentDescription="";
                    _equipmentName="";
                    _equipmentPrice=0;

                }
                public Equipment(String _equipmentName, String _equipmentDescription, String _equipmentImage, int _equipmentPrice){
                    this._equipmentName = _equipmentName;
                    this._equipmentDescription = _equipmentDescription;
                    this._equipmentImage = _equipmentImage;
                    this._equipmentPrice = _equipmentPrice;
                }
                public void setEquipmentImage(String _equipmentImage) {
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


                public String getEquipmentImage() {
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
        */
//    public static class Inscription implements Serializable {
//        private String _inscriptionName;
//        private int _inscriptionLevel;
//        private String _inscriptionDescription;
//
//        public int getInscriptionLevel() {
//            return _inscriptionLevel;
//        }
//
//        public String getInscriptionDescription() {
//            return _inscriptionDescription;
//        }
//
//        public String getInscriptionName() {
//            return _inscriptionName;
//        }
//
//        public void setInscriptionDescription(String _inscriptionDescription) {
//            this._inscriptionDescription = _inscriptionDescription;
//        }
//
//        public void setInscriptionLevel(int _inscriptionLevel) {
//            this._inscriptionLevel = _inscriptionLevel;
//        }
//
//        public void setInscriptionName(String _inscriptionName) {
//            this._inscriptionName = _inscriptionName;
//        }
//
//
//    }
//
    public static class Skill implements Serializable{
        private Integer _skillid;
        private String _skillName;
        private String _skillPosition;
        private String _skillDescription;
        private Integer _skillcool;
        private Integer _skillwaste;
        private String _skilltips;
        private byte[] _skillimage;

        public Skill(Integer _skillid, String _skillName, String _skillPosition, String _skillDescription,
                     Integer _skillcool, Integer _skillwaste, String _skilltips, byte[] _skillimage){
            this._skillid = _skillid;
            this._skillName = _skillName;
            this._skillPosition = _skillPosition;
            this._skillDescription = _skillDescription;
            this._skillcool = _skillcool;
            this._skilltips = _skilltips;
            this._skillwaste = _skillwaste;
            this._skillimage = _skillimage;
        }

        public void setSkillDescription(String _skillDescription) {
            this._skillDescription = _skillDescription;
        }

        public void setSkillName(String _skillName) {
            this._skillName = _skillName;
        }

        public void setSkillPosition(String _skillPosition) {
            this._skillPosition = _skillPosition;
        }


        public String getSkillDescription() {
            return _skillDescription;
        }


        public String getSkillPosition() {
            return _skillPosition;
        }

        public Integer getSkillId() { return _skillid; }

        public Integer getSkillCool() { return _skillcool; }

        public Integer getSkillWaste() { return _skillwaste; }


        public String getSkillName() { return _skillName; }


        public String getSkillTips() { return _skilltips; }

        public byte[] get_skillimage() { return _skillimage; }
    }
}

