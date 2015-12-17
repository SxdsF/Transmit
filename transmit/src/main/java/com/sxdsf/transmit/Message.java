package com.sxdsf.transmit;

/**
 * Created by sunbowen on 2015/12/17.
 */
public class Message<T> {
    private T content;

    protected Message(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public static <T> Message<T> create(T content) {
        return new Message<>(content);
    }
}
