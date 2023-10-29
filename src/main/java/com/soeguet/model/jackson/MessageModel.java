package com.soeguet.model.jackson;

public non-sealed class MessageModel extends BaseModel {

    private String quotedMessageSender;
    private String quotedMessageTime;
    private String quotedMessageText;

    public String getQuotedMessageSender() {

        return quotedMessageSender;
    }

    public void setQuotedMessageSender(final String quotedMessageSender) {

        this.quotedMessageSender = quotedMessageSender;
    }

    public String getQuotedMessageTime() {

        return quotedMessageTime;
    }

    public void setQuotedMessageTime(final String quotedMessageTime) {

        this.quotedMessageTime = quotedMessageTime;
    }

    public String getQuotedMessageText() {

        return quotedMessageText;
    }

    public void setQuotedMessageText(final String quotedMessageText) {

        this.quotedMessageText = quotedMessageText;
    }

    @Override
    public String toString() {

        return "MessageModel{" + "quotedMessageSender='" + quotedMessageSender + '\'' + ", quotedMessageTime='" + quotedMessageTime + '\'' + ", " + "quotedMessageText='" + quotedMessageText + '\'' + ", id=" + id + ", userInteractions=" + userInteractions + ", localIp='" + localIp + '\'' + ", sender='" + sender + '\'' + ", time='" + time + '\'' + ", message='" + message + '\'' + '}';
    }
}