package com.soeguet.model.jackson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.soeguet.model.UserInteraction;

import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "messageType")
@JsonSubTypes({@JsonSubTypes.Type(value = MessageModel.class, name = "text"), @JsonSubTypes.Type(value = PictureModel.class, name = "image")})
public abstract sealed class BaseModel permits MessageModel, PictureModel {

    Long id;
    List<UserInteraction> userInteractions;
    String localIp;
    String sender;
    String time;
    String message;
    private byte messageType;

    public Long getId() {

        return id;
    }

    public void setId(final Long id) {

        this.id = id;
    }

    public byte getMessageType() {

        return messageType;
    }

    public void setMessageType(final byte messageType) {

        this.messageType = messageType;
    }

    public List<UserInteraction> getUserInteractions() {

        //lazy initialization
        if (userInteractions == null) {

            userInteractions = new ArrayList<>();
        }

        return userInteractions;
    }

    public void setUserInteractions(final List<UserInteraction> userInteractions) {

        this.userInteractions = userInteractions;
    }

    public String getLocalIp() {

        return localIp;
    }

    public void setLocalIp(final String localIp) {

        this.localIp = localIp;
    }

    public String getSender() {

        return sender;
    }

    public void setSender(final String sender) {

        this.sender = sender;
    }

    public String getTime() {

        return time;
    }

    public void setTime(final String time) {

        this.time = time;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(final String message) {

        this.message = message;
    }

    @Override
    public String toString() {

        return "BaseModel{" + "id=" + id + ", messageType=" + messageType + ", userInteractions=" + userInteractions + ", localIp='" + localIp + '\'' + ", sender='" + sender + '\'' + ", time='" + time + '\'' + ", message='" + message + '\'' + '}';
    }
}