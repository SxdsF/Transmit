package com.sxdsf.transmit.service.producer.impl;

import android.support.annotation.NonNull;

import com.sxdsf.transmit.Message;
import com.sxdsf.transmit.Topic;
import com.sxdsf.transmit.service.filter.Filter;
import com.sxdsf.transmit.service.impl.ObservableTuple;
import com.sxdsf.transmit.service.producer.MessageProducer;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.Subject;

/**
 * Created by sunbowen on 2015/12/18.
 */
public class SyncMessageProducerImpl implements MessageProducer {

    private final Topic topic;
    private final ObservableTuple tuple;

    public SyncMessageProducerImpl(Topic topic, ObservableTuple tuple) {
        this.topic = topic;
        this.tuple = tuple;
    }

    @Override
    public <T> void send(@NonNull final Message<T> message) {
        if (this.tuple != null) {
            synchronized (this.tuple) {
                final List<Subject> subjects = this.tuple.subjectsMapper.get(this.topic.getUniqueId());
                Observable.from(subjects).
                        filter(new Func1<Subject, Boolean>() {
                            @Override
                            public Boolean call(Subject subject) {
                                return subject != null;
                            }
                        }).
                        filter(new Func1<Subject, Boolean>() {
                            @Override
                            public Boolean call(Subject subject) {
                                boolean flag = true;
                                List<Filter> filters = tuple.filtersMapper.get(subject);
                                if (!message.isEmptyMessage()) {
                                    if (filters != null) {
                                        for (Filter filter : filters) {
                                            if (filter != null) {
                                                flag = filter.filter(message);
                                            }
                                        }
                                    }
                                }
                                return flag;
                            }
                        }).
                        observeOn(AndroidSchedulers.mainThread()).
                        subscribe(new Action1<Subject>() {
                            @Override
                            public void call(Subject subject) {
                                subject.onNext(message.getContent());
                            }
                        });
            }
        }
    }
}
