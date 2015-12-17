package com.sxdsf.transmit;

import android.app.Application;

import com.sxdsf.transmit.service.SyncTransmitService;
import com.sxdsf.transmit.service.TransmitServiceMode;

/**
 * Created by sunbowen on 2015/12/17.
 */
public class MyApplication extends Application {

    public static SyncTransmitService syncTransmitService = null;

    @Override
    public void onCreate() {
        super.onCreate();
        syncTransmitService = Transmit.create(TransmitServiceMode.SYNC);
        syncTransmitService.init();
    }
}
