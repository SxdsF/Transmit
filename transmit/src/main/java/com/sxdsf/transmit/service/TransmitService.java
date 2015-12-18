package com.sxdsf.transmit.service;

import com.sxdsf.transmit.Destination;
import com.sxdsf.transmit.Message;
import com.sxdsf.transmit.Topic;
import com.sxdsf.transmit.service.filter.Filter;

import java.util.List;

import rx.Observable;

/**
 * Created by sunbowen on 2015/12/17.
 */
public interface TransmitService {
    TransmitServiceMode getServiceMode();

    void init();

    <T> void post(Destination destination, Message<T> message);

    <T> T receive(Destination destination, Class<T> cls);

    <T> Observable<T> register(Topic topic, Class<T> cls);

    <T> Observable<T> register(Topic topic, Class<T> cls, Filter filter);

    <T> Observable<T> register(Topic topic, Class<T> cls, List<Filter> filterList);

    <T> void unRegister(Topic topic, Observable<T> observable);
}
