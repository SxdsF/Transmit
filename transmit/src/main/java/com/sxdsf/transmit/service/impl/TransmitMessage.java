package com.sxdsf.transmit.service.impl;

import com.sxdsf.transmit.Message;
import com.sxdsf.transmit.Topic;

/**
 * Created by sunbowen on 2015/12/17.
 */
class TransmitMessage<T> extends Message<T> {

    private Topic topic;

    protected TransmitMessage(T content) {
        super(content);
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}
