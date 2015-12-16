package com.sxdsf.transmit;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by sunbowen on 2015/12/16.
 */
public class Transmit {

    private static volatile Transmit instance = null;

    private Transmit() {
    }

    public static Transmit getInstance() {
        if (instance == null) {
            synchronized (Transmit.class) {
                if (instance == null) {
                    instance = new Transmit();
                }
            }
        }
        return instance;
    }

    private final Map<Object, Object> subjectMapper = new ConcurrentHashMap<>();

    public <T> T get(@NonNull Object tag, Class<T> cls) {
        T subject = null;
        Object content;
        synchronized (this.subjectMapper) {
            content = this.subjectMapper.get(tag);
            this.subjectMapper.remove(tag);
        }
        if (cls != null) {
            subject = cls.cast(content);
        }
        return subject;
    }

    public <T> void passToNext(@NonNull Object tag, @NonNull T content) {
        this.subjectMapper.put(tag, content);
    }

    private final Map<Object, List<Subject>> subjectsMapper = new ConcurrentHashMap<>();

    public <T> Observable<T> register(@NonNull Object tag) {
        List<Subject> subjects = this.subjectsMapper.get(tag);
        if (subjects == null) {
            subjects = new ArrayList<>();
            this.subjectsMapper.put(tag, subjects);
        }
        Subject<T, T> subject = PublishSubject.create();
        subjects.add(subject);
        return subject;
    }

    public <T> void unRegister(@NonNull Object tag, @NonNull Observable<T> observable) {
        List<Subject> subjects = this.subjectsMapper.get(tag);
        if (subjects != null) {
            subjects.remove((Subject) observable);
            if (subjects.isEmpty()) {
                this.subjectsMapper.remove(tag);
            }
        }
    }

    public <T> void post(@NonNull Object tag, @NonNull T content) {
        List<Subject> subjectList = this.subjectsMapper.get(tag);
        if (subjectList != null) {
            if (!subjectList.isEmpty()) {
                for (Subject subject : subjectList) {
                    subject.onNext(content);
                }
            }
        }
    }
}
