package com.pandatem.jiyi;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private String sender;
    private String receiver;
    private String message;
    private Date time;
    public Message(){
        sender = null;
        receiver = null;
        message = null;
        time = null;
    }
    public Message(String sender, String receiver, String message, Date time){
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.time = time;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public void setTime(Date time) {
        this.time = time;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public String getReceiver() {
        return receiver;
    }

    public Date getTime() {
        return time;
    }

    public String getSender() {
        return sender;
    }
}
