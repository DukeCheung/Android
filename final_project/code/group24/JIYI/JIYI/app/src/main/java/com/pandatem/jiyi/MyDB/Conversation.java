package com.pandatem.jiyi.MyDB;

import java.util.Date;

public class Conversation {
    private Person other;
    private Date lastCon;
    private String lastMessage;

    public Conversation(Person other, Date lastCon, String lastMessage){
        this.other = other;
        this.lastCon = lastCon;
        this.lastMessage = lastMessage;
    }

    public void setOther(Person other) {
        this.other = other;
    }

    public void setLastCon(Date lastCon) {
        this.lastCon = lastCon;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Person getOther() {
        return other;
    }

    public Date getLastCon() {
        return lastCon;
    }

    public String getLastMessage() {
        return lastMessage;
    }
}
