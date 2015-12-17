package com.sxdsf.transmit.service.filter.impl;

import com.sxdsf.transmit.Message;
import com.sxdsf.transmit.service.filter.Filter;

/**
 * Created by sunbowen on 2015/12/18.
 */
public class MessageIdFilter implements Filter {

    private final int messageId;

    public MessageIdFilter(int messageId) {
        this.messageId = messageId;
    }

    @Override
    public <T> boolean filter(Message<T> message) {
        boolean result = false;
        if (message != null) {
            if (this.messageId == message.getMessageId()) {
                result = true;
            }
        }
        return result;
    }
}
