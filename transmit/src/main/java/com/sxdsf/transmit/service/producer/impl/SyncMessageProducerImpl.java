package com.sxdsf.transmit.service.producer.impl;

import android.support.annotation.NonNull;

import com.sxdsf.transmit.Message;
import com.sxdsf.transmit.Topic;
import com.sxdsf.transmit.service.filter.Filter;
import com.sxdsf.transmit.service.impl.ObservableTuple;
import com.sxdsf.transmit.service.producer.MessageProducer;

import java.util.List;

import rx.subjects.Subject;

/**
 * Created by sunbowen on 2015/12/18.
 */
public class SyncMessageProducerImpl implements MessageProducer {

    private final Topic topic;
    private final ObservableTuple tuples;

    public SyncMessageProducerImpl(Topic topic, ObservableTuple tuples) {
        this.topic = topic;
        this.tuples = tuples;
    }

    @Override
    public <T> void send(@NonNull Message<T> message) {
        if (this.tuples != null) {
            synchronized (this.tuples) {
                List<Subject> subjects = this.tuples.subjectsMapper.get(this.topic.getTopicName());
                if (subjects != null && !subjects.isEmpty()) {
                    for (Subject subject : subjects) {
                        if (subject != null) {
                            List<Filter> filters = this.tuples.filtersMapper.get(subject);
                            boolean flag = true;
                            if (!message.isEmptyMessage()) {
                                if (filters != null) {
                                    for (Filter filter : filters) {
                                        if (filter != null) {
                                            flag = filter.filter(message);
                                        }
                                    }
                                }
                            }
                            if (flag) {
                                subject.onNext(message.getContent());
                            }
                        }
                    }
                }
            }
        }
    }
}
