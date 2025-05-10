package com.example.movie_ticket_app.common;

public class APIResponse<T> {
    private int code;
    private String message;
    private T metadata;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getMetadata() {
        return metadata;
    }


}
