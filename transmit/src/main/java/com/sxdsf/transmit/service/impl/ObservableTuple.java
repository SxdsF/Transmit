package com.sxdsf.transmit.service.impl;

import com.sxdsf.transmit.service.filter.Filter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import rx.subjects.Subject;

/**
 * Created by sunbowen on 2015/12/18.
 */
public class ObservableTuple {

    public final Map<String, List<Subject>> subjectsMapper = new ConcurrentHashMap<>();
    public final Map<Subject, List<Filter>> filtersMapper = new ConcurrentHashMap<>();
}
