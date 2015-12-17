package com.sxdsf.transmit;

/**
 * Created by sunbowen on 2015/12/17.
 */
public class TransmitTopic implements Topic {

    private String physicalName;

    public TransmitTopic(String physicalName) {
        this.physicalName = physicalName;
    }

    public String getPhysicalName() {
        return physicalName;
    }

    public void setPhysicalName(String physicalName) {
        this.physicalName = physicalName;
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
