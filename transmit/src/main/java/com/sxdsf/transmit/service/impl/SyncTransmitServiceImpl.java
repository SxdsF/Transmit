package com.sxdsf.transmit.service.impl;

import com.sxdsf.transmit.Destination;
import com.sxdsf.transmit.Message;
import com.sxdsf.transmit.Topic;
import com.sxdsf.transmit.service.SyncTransmitService;
import com.sxdsf.transmit.service.TransmitServiceMode;
import com.sxdsf.transmit.service.filter.Filter;
import com.sxdsf.transmit.service.producer.MessageProducer;
import com.sxdsf.transmit.service.producer.impl.SyncMessageProducerImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by sunbowen on 2015/12/17.
 */
public class SyncTransmitServiceImpl implements SyncTransmitService {

    private final Map<String, Message> subjectMapper = new ConcurrentHashMap<>();
    private final Map<String, List<ObserverTuple>> subjectsMapper = new ConcurrentHashMap<>();

    @Override
    public TransmitServiceMode getServiceMode() {
        return TransmitServiceMode.SYNC;
    }

    @Override
    public void init() {
        //do nothing
    }

    @Override
    public <T> void post(Destination destination, Message<T> message) {
        if (destination != null) {
            this.subjectMapper.put(destination.getDestinationName(), message);
        }
    }

    @Override
    public <T> T receive(Destination destination) {
        T subject = null;
        if (destination != null) {
            Message<T> content;
            synchronized (this.subjectMapper) {
                content = this.subjectMapper.get(destination.getDestinationName());
                this.subjectMapper.remove(destination.getDestinationName());
            }
            if (content != null) {
                subject = content.getContent();
            }
        }
        return subject;
    }

    @Override
    public <T> Observable<T> register(Topic topic) {
        ObserverTuple<T, T> subject = null;
        if (topic != null) {
            subject = ObserverTuple.toObserverTuple(PublishSubject.create());
            synchronized (this.subjectsMapper) {
                List<ObserverTuple> tuples = this.subjectsMapper.get(topic.getTopicName());
                if (tuples == null) {
                    tuples = new ArrayList<>();
                    this.subjectsMapper.put(topic.getTopicName(), tuples);
                }
                tuples.add(subject);
            }
        }
        return subject;
    }

    @Override
    public <T> Observable<T> register(Topic topic, Filter filter) {
        ObserverTuple<T, T> subject = null;
        if (topic != null) {
            subject = ObserverTuple.toObserverTuple(PublishSubject.create());
            synchronized (this.subjectsMapper) {
                List<ObserverTuple> tuples = this.subjectsMapper.get(topic.getTopicName());
                if (tuples == null) {
                    tuples = new ArrayList<>();
                    this.subjectsMapper.put(topic.getTopicName(), tuples);
                }
                subject.filters = new ArrayList<>();
                subject.filters.add(filter);
                tuples.add(subject);
            }
        }
        return subject;
    }

    @Override
    public <T> Observable<T> register(Topic topic, List<Filter> filterList) {
        ObserverTuple<T, T> subject = null;
        if (topic != null) {
            subject = ObserverTuple.toObserverTuple(PublishSubject.create());
            synchronized (this.subjectsMapper) {
                List<ObserverTuple> tuples = this.subjectsMapper.get(topic.getTopicName());
                if (tuples == null) {
                    tuples = new ArrayList<>();
                    this.subjectsMapper.put(topic.getTopicName(), tuples);
                }
                subject.filters = filterList;
                tuples.add(subject);
            }
        }
        return subject;
    }

    @Override
    public <T> void unRegister(Topic topic, Observable<T> observable) {
        if (topic != null) {
            synchronized (this.subjectsMapper) {
                List<ObserverTuple> tuples = this.subjectsMapper.get(topic.getTopicName());
                if (tuples != null) {
                    tuples.remove((ObserverTuple) observable);
                    if (tuples.isEmpty()) {
                        this.subjectsMapper.remove(topic.getTopicName());
                    }
                }
            }
        }
    }

    @Override
    public MessageProducer createSyncProducer(Topic topic) {
        MessageProducer producer = null;
        if (topic != null) {
            producer = new SyncMessageProducerImpl(this.subjectsMapper.get(topic.getTopicName()));
        }
        return producer;
    }
}
