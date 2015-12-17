package com.sxdsf.transmit;

import android.app.Application;

import com.sxdsf.transmit.service.AsyncTransmitService;
import com.sxdsf.transmit.service.TransmitServiceMode;

/**
 * Created by sunbowen on 2015/12/17.
 */
public class MyApplication extends Application {

    public static AsyncTransmitService asyncTransmitService = null;

    @Override
    public void onCreate() {
        super.onCreate();
        asyncTransmitService = Transmit.create(TransmitServiceMode.ASYNC);
        asyncTransmitService.init();
    }
}
