package com.sxdsf.transmit.service.filter;

import com.sxdsf.transmit.Message;

/**
 * Created by sunbowen on 2015/12/18.
 */
public interface Filter {
    <T> boolean filter(Message<T> message);
}
