package com.sxdsf.transmit.service.impl;

import com.sxdsf.transmit.service.filter.Filter;

import java.util.List;

import rx.subjects.Subject;

/**
 * Created by sunbowen on 2015/12/18.
 */
public abstract class ObserverTuple<T, R> extends Subject<T, R> {

    public ObserverTuple(OnSubscribe onSubscribe) {
        super(onSubscribe);
    }

    public List<Filter> filters;

    public static ObserverTuple toObserverTuple(Subject subject) {
        return (ObserverTuple) subject;
    }
}
