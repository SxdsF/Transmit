package com.sxdsf.transmit;

import android.support.annotation.NonNull;

/**
 * Created by sunbowen on 2015/12/17.
 */
public class TransmitMessage<T> extends Message<T> {

    private Topic topic;

    public TransmitMessage(T content) {
        super(content);
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public static <T> TransmitMessage<T> copyFromMessage(@NonNull Message<T> message) {
        return (TransmitMessage<T>) message;
    }
}
