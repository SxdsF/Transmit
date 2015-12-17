package com.sxdsf.transmit.service.impl;

import android.util.Log;

/**
 * Created by sunbowen on 2015/12/17.
 */
public class TransmitServiceFactory {

    private static final String TAG = "TransmitServiceFactory";

    public static <T> T createTransmitService(Class<T> cls) {
        T service = null;
        if (cls != null) {
            try {
                service = cls.newInstance();
            } catch (InstantiationException e) {
                Log.e(TAG, e.getMessage());
            } catch (IllegalAccessException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return service;
    }
}
