package com.sxdsf.transmit.service.impl;

import com.sxdsf.transmit.Destination;
import com.sxdsf.transmit.Message;
import com.sxdsf.transmit.Topic;
import com.sxdsf.transmit.service.AsyncTransmitService;
import com.sxdsf.transmit.service.TransmitServiceMode;
import com.sxdsf.transmit.service.filter.Filter;
import com.sxdsf.transmit.service.producer.MessageProducer;

import java.util.List;

import rx.Observable;

/**
 * Created by sunbowen on 2015/12/17.
 */
public class AsyncTransmitServiceImpl implements AsyncTransmitService {

    private final InnerTransmitService service = new InnerTransmitService();

    @Override
    public TransmitServiceMode getServiceMode() {
        return TransmitServiceMode.ASYNC;
    }

    @Override
    public void init() {
        this.service.init();
    }

    @Override
    public <T> void post(Destination destination, Message<T> message) {
        this.service.post(destination, message);
    }

    @Override
    public <T> T receive(Destination destination) {
        return this.service.receive(destination);
    }

    @Override
    public <T> Observable<T> register(Topic topic) {
        return this.service.register(topic);
    }

    @Override
    public <T> Observable<T> register(Topic topic, Filter filter) {
        return this.service.register(topic, filter);
    }

    @Override
    public <T> Observable<T> register(Topic topic, List<Filter> filterList) {
        return this.service.register(topic, filterList);
    }

    @Override
    public <T> void unRegister(Topic topic, Observable<T> observable) {
        this.service.unRegister(topic, observable);
    }

    @Override
    public MessageProducer createAsyncProducer(Topic topic) {
        return this.service.createAsyncProducer(topic);
    }
}
