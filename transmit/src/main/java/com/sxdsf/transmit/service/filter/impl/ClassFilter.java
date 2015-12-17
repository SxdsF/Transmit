package com.sxdsf.transmit.service.filter.impl;

import com.sxdsf.transmit.Message;
import com.sxdsf.transmit.service.filter.Filter;

/**
 * Created by sunbowen on 2015/12/18.
 */
public class ClassFilter implements Filter {

    private final Class<?> cls;

    public ClassFilter(Class<?> cls) {
        this.cls = cls;
    }

    @Override
    public <T> boolean filter(Message<T> message) {
        boolean result = false;
        if (message != null) {
            T content = message.getContent();
            if (content != null) {
                if (this.cls == content.getClass()) {
                    result = true;
                }
            }
        }
        return result;
    }
}
