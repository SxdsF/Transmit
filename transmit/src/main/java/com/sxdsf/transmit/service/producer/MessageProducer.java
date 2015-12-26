package com.sxdsf.transmit.service.producer;

import android.support.annotation.NonNull;

import com.sxdsf.transmit.Message;

/**
 * Created by sunbowen on 2015/12/17.
 */
public interface MessageProducer {
    <T> void send(@NonNull Message<T> message);
}
