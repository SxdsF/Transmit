package com.sxdsf.transmit.service.filter.impl;

import com.sxdsf.transmit.Message;
import com.sxdsf.transmit.service.filter.Filter;

/**
 * IsConsumedFilter
 *
 * @author Administrator
 * @date 2016/3/9-10:56
 * @desc ${描述类实现的功能}
 */
public class IsConsumedFilter implements Filter {
    @Override
    public <T> boolean filter(Message<T> message) {
        boolean result = false;
        if (message != null) {
            result = !message.isConsumed();
        }
        return result;
    }
}
