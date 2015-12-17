package com.sxdsf.transmit.service;

import com.sxdsf.transmit.service.impl.AsyncTransmitServiceImpl;
import com.sxdsf.transmit.service.impl.CompositeTransmitServiceImpl;
import com.sxdsf.transmit.service.impl.SyncTransmitServiceImpl;

/**
 * Created by sunbowen on 2015/12/17.
 */
public enum TransmitServiceMode {

    SYNC(SyncTransmitServiceImpl.class),
    ASYNC(AsyncTransmitServiceImpl.class),
    COMPOSITE(CompositeTransmitServiceImpl.class);

    private Class<?> cls;

    private TransmitServiceMode(Class<?> cls) {
        this.cls = cls;
    }

    public Class<?> getCls() {
        return cls;
    }
}
