package com.sxdsf.transmit;

/**
 * Created by sunbowen on 2015/12/17.
 */
public class TransmitTopic extends TransmitDestination implements Topic {

    public TransmitTopic(String physicalName) {
        super(physicalName);
    }


    @Override
    public String getTopicName() {
        return this.getPhysicalName();
    }

    @Override
    public String toString() {
        return this.getTopicName();
    }

    @Override
    public boolean equals(Object o) {
        boolean flag = false;
        if (o != null) {
            if (this.toString().equals(o.toString())) {
                flag = true;
            }
        }
        return flag;
    }
}
