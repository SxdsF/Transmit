package com.sxdsf.transmit.service.impl;

import android.util.Log;

import com.sxdsf.transmit.Message;
import com.sxdsf.transmit.Topic;
import com.sxdsf.transmit.service.AsyncTransmitService;
import com.sxdsf.transmit.service.TransmitServiceMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by sunbowen on 2015/12/17.
 */
public class AsyncTransmitServiceImpl implements AsyncTransmitService {

    private final Map<Topic, Message> subjectMapper = new ConcurrentHashMap<>();
    private final Map<Topic, List<Subject>> subjectsMapper = new ConcurrentHashMap<>();
    private final BlockingQueue<TransmitMessage> messageQueue = new LinkedBlockingQueue<>();
    private final Thread pollingThread = new Thread(new Task());

    private static final String TAG = "AsyncTransmitService";

    @Override
    public <T> void asyncPublish(final Topic topic, Message<T> message) {
        if (message != null) {
            try {
                TransmitMessage<T> tm = new TransmitMessage<>(message.getContent());
                tm.setTopic(topic);
                this.messageQueue.put(tm);
            } catch (InterruptedException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    @Override
    public TransmitServiceMode getServiceMode() {
        return TransmitServiceMode.ASYNC;
    }

    @Override
    public void init() {
        this.pollingThread.start();
    }

    @Override
    public <T> void post(Topic topic, Message<T> message) {
        this.subjectMapper.put(topic, message);
    }

    @Override
    public <T> T receive(Topic topic) {
        T subject = null;
        Message<T> content;
        synchronized (this.subjectMapper) {
            content = this.subjectMapper.get(topic);
            this.subjectMapper.remove(topic);
        }
        if (content != null) {
            subject = content.getContent();
        }
        return subject;
    }

    @Override
    public <T> Observable<T> register(Topic topic) {
        Subject<T, T> subject = PublishSubject.create();
        List<Subject> subjects;
        synchronized (this.subjectsMapper) {
            subjects = this.subjectsMapper.get(topic);
            if (subjects == null) {
                subjects = new ArrayList<>();
                this.subjectsMapper.put(topic, subjects);
                subjects.add(subject);
            }
        }
        return subject;
    }

    @Override
    public <T> void unRegister(Topic topic, Observable<T> observable) {
        synchronized (this.subjectsMapper) {
            List<Subject> subjects = this.subjectsMapper.get(topic);
            if (subjects != null) {
                subjects.remove((Subject) observable);
                if (subjects.isEmpty()) {
                    this.subjectsMapper.remove(topic);
                }
            }
        }
    }

    private class Task implements Runnable {
        @Override
        public void run() {
            try {
                TransmitMessage message = messageQueue.take();
                if (message != null) {
                    synchronized (subjectsMapper) {
                        List<Subject> subjectList = subjectsMapper.get(message.getTopic());
                        if (subjectList != null) {
                            if (!subjectList.isEmpty()) {
                                for (Subject subject : subjectList) {
                                    subject.onNext(message.getContent());
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
