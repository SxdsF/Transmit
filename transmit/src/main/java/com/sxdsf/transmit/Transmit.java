package com.sxdsf.transmit;

import android.util.Log;

import com.sxdsf.transmit.service.TransmitService;
import com.sxdsf.transmit.service.TransmitServiceMode;

/**
 * Created by sunbowen on 2015/12/16.
 */
public class Transmit {

    private static final String TAG = "Transmit";

    public static <T extends TransmitService> T create(TransmitServiceMode mode) {
        T service = null;
        if (mode != null) {
            Class<?> cls = mode.getCls();
            try {
                service = (T) cls.newInstance();
            } catch (InstantiationException e) {
                Log.e(TAG, e.getMessage());
            } catch (IllegalAccessException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return service;
    }
}
