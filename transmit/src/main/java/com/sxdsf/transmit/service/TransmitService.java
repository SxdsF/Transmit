package com.sxdsf.transmit.service;

import com.sxdsf.transmit.Message;
import com.sxdsf.transmit.Topic;

import rx.Observable;

/**
 * Created by sunbowen on 2015/12/17.
 */
public interface TransmitService {
    TransmitServiceMode getServiceMode();

    void init();

    <T> void post(Topic topic, Message<T> message);

    <T> T receive(Topic topic);

    <T> Observable<T> register(Topic topic);

    <T> void unRegister(Topic topic, Observable<T> observable);
}
