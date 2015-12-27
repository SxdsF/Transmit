package com.sxdsf.transmit.service.impl;

import android.support.annotation.NonNull;
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
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by sunbowen on 2015/12/18.
 */
class InnerTransmitService implements CompositeTransmitService {
    private final Map<UUID, Message> subjectMapper = new ConcurrentHashMap<>();
    private final ObservableTuple tuple = new ObservableTuple();
    private final BlockingQueue<TransmitMessage> messageQueue = new PriorityBlockingQueue<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final AtomicBoolean isInit = new AtomicBoolean(false);

    private static final String TAG = "InnerTransmitService";

    @Override
    public TransmitServiceMode getServiceMode() {
        return null;
    }

    @Override
    public void initialize() {
        if (this.isInit.compareAndSet(false, true)) {
            this.executorService.execute(new Task());
        }
    }

    @Override
    public boolean isInitialized() {
        return this.isInit.get();
    }

    @Override
    public <T> void post(@NonNull Destination destination, @NonNull Message<T> message) {
        this.subjectMapper.put(destination.getUniqueId(), message);
    }

    @Override
    public <T> T receive(@NonNull Destination destination, @NonNull Class<T> cls) {
        T subject = null;
        Message<?> content;
        synchronized (this.subjectMapper) {
            content = this.subjectMapper.get(destination.getUniqueId());
            this.subjectMapper.remove(destination.getUniqueId());
        }
        if (content != null && !content.isEmptyMessage()) {
            Filter filter = new ClassFilter(cls);
            if (filter.filter(content)) {
                subject = cls.cast(content.getContent());
            }
        }
        return subject;
    }

    @Override
    public <T> Observable<T> register(@NonNull Topic topic, @NonNull Class<T> cls) {
        return this.register(topic, cls, new ClassFilter(cls));
    }

    @Override
    public <T> Observable<T> register(@NonNull Topic topic, @NonNull Class<T> cls, Filter filter) {
        Subject<T, T> subject = PublishSubject.create();
        synchronized (this.tuple) {
            List<Subject> subjects = this.tuple.subjectsMapper.get(topic.getUniqueId());
            if (subjects == null) {
                subjects = new ArrayList<>();
                this.tuple.subjectsMapper.put(topic.getUniqueId(), subjects);
            }
            List<Filter> filters = new ArrayList<>();
            filters.add(new ClassFilter(cls));
            filters.add(filter);
            this.tuple.filtersMapper.put(subject, filters);
            subjects.add(subject);
        }
        return subject;
    }

    @Override
    public <T> Observable<T> register(@NonNull Topic topic, @NonNull Class<T> cls, List<Filter> filterList) {
        Subject<T, T> subject = PublishSubject.create();
        synchronized (this.tuple) {
            List<Subject> subjects = this.tuple.subjectsMapper.get(topic.getUniqueId());
            if (subjects == null) {
                subjects = new ArrayList<>();
                this.tuple.subjectsMapper.put(topic.getUniqueId(), subjects);
            }
            List<Filter> filters = new ArrayList<>();
            filters.add(new ClassFilter(cls));
            filters.addAll(filterList);
            this.tuple.filtersMapper.put(subject, filters);
            subjects.add(subject);
        }
        return subject;
    }

    @Override
    public <T> void unRegister(@NonNull Topic topic, @NonNull Observable<T> observable) {
        synchronized (this.tuple) {
            List<Subject> subjects = this.tuple.subjectsMapper.get(topic.getUniqueId());
            if (subjects != null) {
                subjects.remove((Subject) observable);
                if (subjects.isEmpty()) {
                    this.tuple.subjectsMapper.remove(topic.getUniqueId());
                }
            }
            this.tuple.filtersMapper.remove((Subject) observable);
        }
    }


    @Override
    public MessageProducer createAsyncProducer(@NonNull Topic topic) {
        return new AsyncMessageProducerImpl(topic, this.messageQueue);
    }

    @Override
    public MessageProducer createSyncProducer(@NonNull Topic topic) {
        return new SyncMessageProducerImpl(topic, this.tuple);
    }


    private class Task implements Runnable {
        @Override
        public void run() {
            try {
                final TransmitMessage message = messageQueue.take();
                if (message != null && message.getTopic() != null) {
                    Topic topic = message.getTopic();
                    synchronized (tuple) {
                        List<Subject> subjects = tuple.subjectsMapper.get(topic.getUniqueId());
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
                                subscribe(new Action1<Subject>() {
                                    @Override
                                    public void call(Subject subject) {
                                        subject.onNext(message.getContent());
                                    }
                                });
                    }
                }
            } catch (InterruptedException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }
}
