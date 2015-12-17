package com.sxdsf.transmit.service;

import com.sxdsf.transmit.Message;
import com.sxdsf.transmit.Topic;

/**
 * Created by sunbowen on 2015/12/17.
 */
public interface SyncTransmitService extends TransmitService {

    <T> void syncPublish(Topic topic, Message<T> message);
}
