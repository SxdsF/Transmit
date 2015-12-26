package com.sxdsf.transmit.service;

import android.support.annotation.NonNull;

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

    void initialize();

    boolean isInitialized();

    <T> void post(@NonNull Destination destination, @NonNull Message<T> message);

    <T> T receive(@NonNull Destination destination, @NonNull Class<T> cls);

    <T> Observable<T> register(@NonNull Topic topic, @NonNull Class<T> cls);

    <T> Observable<T> register(@NonNull Topic topic, @NonNull Class<T> cls, Filter filter);

    <T> Observable<T> register(@NonNull Topic topic, @NonNull Class<T> cls, List<Filter> filterList);

    <T> void unRegister(@NonNull Topic topic, @NonNull Observable<T> observable);
}
