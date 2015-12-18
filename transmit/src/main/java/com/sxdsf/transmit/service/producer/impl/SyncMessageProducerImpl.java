package com.sxdsf.transmit.service.producer.impl;

import com.sxdsf.transmit.Message;
import com.sxdsf.transmit.Topic;
import com.sxdsf.transmit.service.filter.Filter;
import com.sxdsf.transmit.service.impl.ObserableTuple;
import com.sxdsf.transmit.service.producer.MessageProducer;

import java.util.List;

import rx.subjects.Subject;

/**
 * Created by sunbowen on 2015/12/18.
 */
public class SyncMessageProducerImpl implements MessageProducer {

    private final Topic topic;
    private final ObserableTuple tuples;

    public SyncMessageProducerImpl(Topic topic, ObserableTuple tuples) {
        this.topic = topic;
        this.tuples = tuples;
    }

    @Override
    public <T> void send(Message<T> message) {
        if (message != null) {
            if (this.tuples != null) {
                synchronized (this.tuples) {
                    List<Subject> subjects = this.tuples.subjectsMapper.get(this.topic.getTopicName());
                    if (subjects != null && !subjects.isEmpty()) {
                        for (Subject subject : subjects) {
                            if (subject != null) {
                                List<Filter> filters = this.tuples.filtersMapper.get(subject);
                                boolean flag = true;
                                if (filters != null) {
                                    for (Filter filter : filters) {
                                        if (filter != null) {
                                            flag = filter.filter(message);
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
}
