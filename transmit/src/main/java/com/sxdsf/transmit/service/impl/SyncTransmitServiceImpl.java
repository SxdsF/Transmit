package com.sxdsf.transmit.service.impl;

import android.support.annotation.NonNull;

import com.sxdsf.transmit.Destination;
import com.sxdsf.transmit.Message;
import com.sxdsf.transmit.Topic;
import com.sxdsf.transmit.service.SyncTransmitService;
import com.sxdsf.transmit.service.TransmitServiceMode;
import com.sxdsf.transmit.service.filter.Filter;
import com.sxdsf.transmit.service.producer.MessageProducer;

import java.util.List;

import rx.Observable;

/**
 * Created by sunbowen on 2015/12/17.
 */
public class SyncTransmitServiceImpl implements SyncTransmitService {

    private final InnerTransmitService service = new InnerTransmitService();

    @Override
    public TransmitServiceMode getServiceMode() {
        return TransmitServiceMode.SYNC;
    }

    @Override
    public void initialize() {
        //do nothing
    }

    @Override
    public boolean isInitialized() {
        return true;
    }

    @Override
    public <T> void post(@NonNull Destination destination, @NonNull Message<T> message) {
        this.service.post(destination, message);
    }

    @Override
    public <T> T receive(@NonNull Destination destination, @NonNull Class<T> cls) {
        return this.service.receive(destination, cls);
    }

    @Override
    public <T> Observable<T> register(@NonNull Topic topic, @NonNull Class<T> cls) {
        return this.service.register(topic, cls);
    }

    @Override
    public <T> Observable<T> register(@NonNull Topic topic, @NonNull Class<T> cls, Filter filter) {
        return this.service.register(topic, cls, filter);
    }

    @Override
    public <T> Observable<T> register(@NonNull Topic topic, @NonNull Class<T> cls, List<Filter> filterList) {
        return this.service.register(topic, cls, filterList);
    }

    @Override
    public <T> void unRegister(@NonNull Topic topic, @NonNull Observable<T> observable) {
        this.service.unRegister(topic, observable);
    }

    @Override
    public MessageProducer createSyncProducer(@NonNull Topic topic) {
        return this.service.createSyncProducer(topic);
    }
}
