package com.sxdsf.transmit;

import java.util.UUID;

/**
 * Created by sunbowen on 2015/12/17.
 */
public class TransmitDestination implements Destination {

    private String physicalName;
    private final UUID uuid;

    public TransmitDestination(String physicalName) {
        this.physicalName = physicalName;
        this.uuid = UUID.randomUUID();
    }

    public String getPhysicalName() {
        return physicalName;
    }

    public void setPhysicalName(String physicalName) {
        this.physicalName = physicalName;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String getDestinationName() {
        return this.getPhysicalName();
    }

    @Override
    public UUID getUniqueId() {
        return this.getUuid();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }

        if (this == object) {
            return true;
        }

        if (!(object instanceof TransmitDestination)) {
            return false;
        }

        return this.uuid.equals(((TransmitDestination) object).uuid);
    }

}
