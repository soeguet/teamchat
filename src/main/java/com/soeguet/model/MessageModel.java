package com.soeguet.model;

import com.soeguet.gui.util.MessageTypes;
import lombok.Getter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MessageModel {

    private Long id;
    private byte messageType;
    private List<UserInteraction> userInteractions;
    private String localIp;
    private String sender;
    private String time;
    private String message;
    private String quotedMessageSender;
    private String quotedMessageTime;
    private String quotedMessageText;

    public MessageModel(String sender, String message) {
        this(MessageTypes.NORMAL, sender, message, null, null, null);
    }

    public MessageModel(Long id) {
        this(id, MessageTypes.DELETED, null, null, null, null, "delete by user", null, null, null);
    }

    public MessageModel(byte messageType, String sender, String message) {
        this(messageType, sender, message, null, null, null);
    }

    public MessageModel(
            String sender,
            String message,
            String quotedMessageSender,
            String quotedMessageTime,
            String quotedMessageText) {
        this(
                MessageTypes.NORMAL,
                sender,
                message,
                quotedMessageSender,
                quotedMessageTime,
                quotedMessageText);
    }

    public MessageModel(
            byte messageType,
            String sender,
            String message,
            String quotedMessageSender,
            String quotedMessageTime,
            String quotedMessageText) {
        this(
                null,
                messageType,
                null,
                null,
                sender,
                LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
                message,
                quotedMessageSender,
                quotedMessageTime,
                quotedMessageText);
    }

    // for updating messages
    public MessageModel(
            Long id,
            byte messageType,
            List<UserInteraction> userInteraction,
            String localIp,
            String sender,
            String time,
            String message,
            String quotedMessageSender,
            String quotedMessageTime,
            String quotedMessageText) {
        this.id = id;
        this.messageType = messageType;

        this.userInteractions = userInteraction;
        this.localIp = localIp;
        this.sender = sender;
        this.time = time;
        this.message = message;
        this.quotedMessageSender = quotedMessageSender;
        this.quotedMessageTime = quotedMessageTime;
        this.quotedMessageText = quotedMessageText;
    }

    public MessageModel() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setQuotedMessageSender(String quotedMessageSender) {
        this.quotedMessageSender = quotedMessageSender;
    }

    public void setQuotedMessageTime(String quotedMessageTime) {
        this.quotedMessageTime = quotedMessageTime;
    }

    public void setQuotedMessageText(String quotedMessageText) {
        this.quotedMessageText = quotedMessageText;
    }

    public void setMessageType(byte messageType) {
        this.messageType = messageType;
    }

    public void setUserInteractions(List<UserInteraction> userInteractions) {
        this.userInteractions = userInteractions;
    }

    public List<UserInteraction> addUserInteractions(String name, String emoji) {

        if (this.userInteractions == null) {
            this.userInteractions = new ArrayList<>();
        }
        this.userInteractions.add(new UserInteraction(name, emoji));
        return this.userInteractions;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "id=" + id +
                ", messageType=" + messageType +
                ", userInteractions=" + userInteractions +
                ", localIp='" + localIp + '\'' +
                ", sender='" + sender + '\'' +
                ", time='" + time + '\'' +
                ", message='" + message + '\'' +
                ", quotedMessageSender='" + quotedMessageSender + '\'' +
                ", quotedMessageTime='" + quotedMessageTime + '\'' +
                ", quotedMessageText='" + quotedMessageText + '\'' +
                '}';
    }
}
