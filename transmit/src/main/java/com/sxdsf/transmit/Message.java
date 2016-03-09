package com.sxdsf.transmit;

import android.support.annotation.NonNull;

/**
 * Created by sunbowen on 2015/12/17.
 */
public class Message<T> implements Comparable<Message> {

    /**
     * 消息id
     */
    private int messageId;
    /**
     * 时间戳
     */
    private long timestamp;
    /**
     * 优先级
     */
    private int priority = NORM_PRIORITY;
    /**
     * 内容
     */
    private T content;
    /**
     * 是否是空消息
     */
    private boolean isEmptyMessage;
    /**
     * 是否被消费
     */
    private boolean isConsumed;

    public final static int MIN_PRIORITY = 1;
    public final static int NORM_PRIORITY = 5;
    public final static int MAX_PRIORITY = 10;

    protected Message(T content) {
        this.content = content;
        this.timestamp = System.currentTimeMillis();
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

    public boolean isConsumed() {
        return isConsumed;
    }

    public void setIsConsumed(boolean isConsumed) {
        this.isConsumed = isConsumed;
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
