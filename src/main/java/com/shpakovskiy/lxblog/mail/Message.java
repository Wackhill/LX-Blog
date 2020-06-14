package com.shpakovskiy.lxblog.mail;

public class Message {

    private String receiverAddress;
    private String subject;
    private String text;

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static class Builder {
        private final Message newMessage;

        public Builder() {
            newMessage = new Message();
        }

        public Builder subject(String subject) {
            newMessage.subject = subject;
            return this;
        }

        public Builder text(String text) {
            newMessage.text = text;
            return this;
        }

        public Builder receiverAddress(String receiverAddress) {
            newMessage.receiverAddress = receiverAddress;
            return this;
        }

        public Message build() {
            return newMessage;
        }
    }
}