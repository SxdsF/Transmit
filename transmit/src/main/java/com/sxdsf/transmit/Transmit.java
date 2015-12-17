package com.sxdsf.transmit;

import com.sxdsf.transmit.service.TransmitService;
import com.sxdsf.transmit.service.TransmitServiceMode;
import com.sxdsf.transmit.service.impl.TransmitServiceFactory;

/**
 * Created by sunbowen on 2015/12/16.
 */
public class Transmit {

    public static <T extends TransmitService> T create(TransmitServiceMode mode) {
        T service = null;
        if (mode != null) {
            service = (T) TransmitServiceFactory.createTransmitService(mode.getCls());
            if (service != null) {
                if (mode != service.getServiceMode()) {
                    service = null;
                }
            }
        }
        return service;
    }
}
