package com.sxdsf.transmit.service.producer.impl;

import android.util.Log;

import com.sxdsf.transmit.Message;
import com.sxdsf.transmit.Topic;
import com.sxdsf.transmit.TransmitMessage;
import com.sxdsf.transmit.service.producer.MessageProducer;

import java.util.concurrent.BlockingQueue;

/**
 * Created by sunbowen on 2015/12/18.
 */
public class AsyncMessageProducerImpl implements MessageProducer {

    private final Topic topic;
    private final BlockingQueue<TransmitMessage> messageQueue;

    public AsyncMessageProducerImpl(Topic topic, BlockingQueue<TransmitMessage> messageQueue) {
        this.topic = topic;
        this.messageQueue = messageQueue;
    }

    private static final String TAG = "AsyncTransmitService";

    @Override
    public <T> void send(Message<T> message) {
        if (message != null) {
            try {
                TransmitMessage<T> tm = new TransmitMessage<>(message.getContent());
                tm.setTopic(this.topic);
                this.messageQueue.put(tm);
            } catch (InterruptedException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }
}
