package com.sxdsf.transmit.service.producer.impl;

import com.sxdsf.transmit.Message;
import com.sxdsf.transmit.service.filter.Filter;
import com.sxdsf.transmit.service.impl.ObserverTuple;
import com.sxdsf.transmit.service.producer.MessageProducer;

import java.util.List;

/**
 * Created by sunbowen on 2015/12/18.
 */
public class SyncMessageProducerImpl implements MessageProducer {

    private final List<ObserverTuple> tuples;

    public SyncMessageProducerImpl(List<ObserverTuple> tuples) {
        this.tuples = tuples;
    }

    @Override
    public <T> void send(Message<T> message) {
        if (message != null) {
            if (this.tuples != null && !this.tuples.isEmpty()) {
                for (ObserverTuple subject : this.tuples) {
                    if (subject != null) {
                        List<Filter> filters = subject.filters;
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
