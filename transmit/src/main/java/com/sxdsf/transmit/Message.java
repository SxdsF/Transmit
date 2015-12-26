package com.sxdsf.transmit;

import android.support.annotation.NonNull;

/**
 * Created by sunbowen on 2015/12/17.
 */
public class Message<T> implements Comparable<Message> {

    private int messageId;
    private long timestamp;
    private int priority = NORM_PRIORITY;
    private T content;
    private boolean isEmptyMessage;

    public final static int MIN_PRIORITY = 1;
    public final static int NORM_PRIORITY = 5;
    public final static int MAX_PRIORITY = 10;

    protected Message(T content) {
        this.content = content;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public boolean isEmptyMessage() {
        return isEmptyMessage;
    }

    @Override
    public int compareTo(Message another) {
        int result = 0;
        if (another != null) {
            if (this.priority < another.getPriority()) {
                result = -1;
            } else if (this.priority > another.getPriority()) {
                result = 1;
            }
        }
        return result;
    }

    public static <T> Message<T> create(@NonNull T content) {
        return new Message<>(content);
    }

    public static Message createEmptyMessage() {
        Message message = new Message(null);
        message.isEmptyMessage = true;
        return message;
    }
}
