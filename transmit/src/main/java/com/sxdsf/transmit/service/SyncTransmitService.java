package com.sxdsf.transmit.service;

import android.support.annotation.NonNull;

import com.sxdsf.transmit.Topic;
import com.sxdsf.transmit.service.producer.MessageProducer;

/**
 * Created by sunbowen on 2015/12/18.
 */
public interface SyncTransmitService extends TransmitService {

    MessageProducer createSyncProducer(@NonNull Topic topic);

}
