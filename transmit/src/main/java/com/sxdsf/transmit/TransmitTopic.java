package com.sxdsf.transmit;

/**
 * Created by sunbowen on 2015/12/17.
 */
public class TransmitTopic extends TransmitDestination implements Topic, Comparable<TransmitTopic> {

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
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }

        if (this == object) {
            return true;
        }

        if (!(object instanceof TransmitTopic)) {
            return false;
        }

        return this.getUuid().equals(((TransmitTopic) object).getUuid());
    }

    @Override
    public int compareTo(TransmitTopic another) {
        if (another == this) {
            return 0;
        }

        return this.getUuid().compareTo(another.getUuid());
    }
}
