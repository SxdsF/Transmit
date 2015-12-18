package com.sxdsf.transmit.service.impl;

import android.util.Log;

import com.sxdsf.transmit.Destination;
import com.sxdsf.transmit.Message;
import com.sxdsf.transmit.Topic;
import com.sxdsf.transmit.TransmitMessage;
import com.sxdsf.transmit.service.CompositeTransmitService;
import com.sxdsf.transmit.service.TransmitServiceMode;
import com.sxdsf.transmit.service.filter.Filter;
import com.sxdsf.transmit.service.filter.impl.ClassFilter;
import com.sxdsf.transmit.service.producer.MessageProducer;
import com.sxdsf.transmit.service.producer.impl.AsyncMessageProducerImpl;
import com.sxdsf.transmit.service.producer.impl.SyncMessageProducerImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by sunbowen on 2015/12/18.
 */
class InnerTransmitService implements CompositeTransmitService {
    private final Map<String, Message> subjectMapper = new ConcurrentHashMap<>();
    private final ObserableTuple tuples = new ObserableTuple();
    private final BlockingQueue<TransmitMessage> messageQueue = new PriorityBlockingQueue<>();
    private final Thread pollingThread = new Thread(new Task());

    private static final String TAG = "InnerTransmitService";

    @Override
    public TransmitServiceMode getServiceMode() {
        return null;
    }

    @Override
    public void init() {
        this.pollingThread.start();
    }

    @Override
    public <T> void post(Destination destination, Message<T> message) {
        if (destination != null) {
            this.subjectMapper.put(destination.getDestinationName(), message);
        }
    }

    @Override
    public <T> T receive(Destination destination, Class<T> cls) {
        T subject = null;
        if (destination != null) {
            Message<T> content;
            synchronized (this.subjectMapper) {
                content = this.subjectMapper.get(destination.getDestinationName());
                this.subjectMapper.remove(destination.getDestinationName());
            }
            if (content != null) {
                Filter filter = new ClassFilter(cls);
                if (filter.filter(content)) {
                    subject = content.getContent();
                }
            }
        }
        return subject;
    }

    @Override
    public <T> Observable<T> register(Topic topic, Class<T> cls) {
        return this.register(topic, cls, new ClassFilter(cls));
    }

    @Override
    public <T> Observable<T> register(Topic topic, Class<T> cls, Filter filter) {
        Subject<T, T> subject = null;
        if (topic != null) {
            subject = PublishSubject.create();
            synchronized (this.tuples) {
                List<Subject> subjects = this.tuples.subjectsMapper.get(topic.getTopicName());
                if (subjects == null) {
                    subjects = new ArrayList<>();
                    this.tuples.subjectsMapper.put(topic.getTopicName(), subjects);
                }
                List<Filter> filters = new ArrayList<>();
                filters.add(new ClassFilter(cls));
                filters.add(filter);
                this.tuples.filtersMapper.put(subject, filters);
                subjects.add(subject);
            }
        }
        return subject;
    }

    @Override
    public <T> Observable<T> register(Topic topic, Class<T> cls, List<Filter> filterList) {
        Subject<T, T> subject = null;
        if (topic != null) {
            subject = PublishSubject.create();
            synchronized (this.tuples) {
                List<Subject> subjects = this.tuples.subjectsMapper.get(topic.getTopicName());
                if (subjects == null) {
                    subjects = new ArrayList<>();
                    this.tuples.subjectsMapper.put(topic.getTopicName(), subjects);
                }
                List<Filter> filters = new ArrayList<>();
                filters.add(new ClassFilter(cls));
                filters.addAll(filterList);
                this.tuples.filtersMapper.put(subject, filters);
                subjects.add(subject);
            }
        }
        return subject;
    }

    @Override
    public <T> void unRegister(Topic topic, Observable<T> observable) {
        if (topic != null) {
            synchronized (this.tuples) {
                List<Subject> subjects = this.tuples.subjectsMapper.get(topic.getTopicName());
                if (subjects != null) {
                    subjects.remove((Subject) observable);
                    if (subjects.isEmpty()) {
                        this.tuples.subjectsMapper.remove(topic.getTopicName());
                    }
                }
                this.tuples.filtersMapper.remove((Subject) observable);
            }
        }
    }

    @Override
    public MessageProducer createAsyncProducer(Topic topic) {
        MessageProducer producer = null;
        if (topic != null) {
            producer = new AsyncMessageProducerImpl(topic, this.messageQueue);
        }
        return producer;
    }

    @Override
    public MessageProducer createSyncProducer(Topic topic) {
        MessageProducer producer = null;
        if (topic != null) {
            producer = new SyncMessageProducerImpl(topic, this.tuples);
        }
        return producer;
    }


    private class Task implements Runnable {
        @Override
        public void run() {
            try {
                TransmitMessage message = messageQueue.take();
                if (message != null && message.getTopic() != null) {
                    Topic topic = message.getTopic();
                    synchronized (tuples) {
                        List<Subject> subjects = tuples.subjectsMapper.get(topic.getTopicName());
                        if (subjects != null && !subjects.isEmpty()) {
                            for (Subject subject : subjects) {
                                if (subject != null) {
                                    List<Filter> filters = tuples.filtersMapper.get(subject);
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
            } catch (InterruptedException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }
}
