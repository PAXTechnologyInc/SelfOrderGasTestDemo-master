package com.pax.order.db;

import com.clj.fastble.data.BleDevice;

import java.io.Serializable;


/**
 * Created by Sim.G on 2017/6/2 09:53
 */
@SuppressWarnings("serial")
public class TransData implements Serializable, Cloneable {

    public TransData() {
    }

    public BleDevice getBleDevice() {
        return bleDevice;
    }

    public void setBleDevice(BleDevice bleDevice) {
        this.bleDevice = bleDevice;
    }

    public BleDevice bleDevice;
}

