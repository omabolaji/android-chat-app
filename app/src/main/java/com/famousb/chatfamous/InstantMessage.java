package com.famousb.chatfamous;

public class InstantMessage {

    private String message;
    private  String author;
    private boolean isMe;

    public InstantMessage(String message, String author) {
        this.message = message;
        this.author = author;
        this.isMe = isMe;
    }

    public InstantMessage() {
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

}
