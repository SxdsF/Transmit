package com.sxdsf.transmit;

/**
 * Created by sunbowen on 2015/12/17.
 */
public class TransmitDestination implements Destination {

    private String physicalName;

    public TransmitDestination(String physicalName) {
        this.physicalName = physicalName;
    }

    public String getPhysicalName() {
        return physicalName;
    }

    public void setPhysicalName(String physicalName) {
        this.physicalName = physicalName;
    }

    @Override
    public String getDestinationName() {
        return this.getPhysicalName();
    }
}
