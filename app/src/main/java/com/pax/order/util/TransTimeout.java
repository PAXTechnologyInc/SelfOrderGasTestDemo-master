package com.pax.order.util;

/**
 * timeout manager
 * Created by Frank.W on 2017/4/11.
 */

public class TransTimeout {
    private long startTime = 0;
    private boolean isStopTimer = false;
    private long maxTimeout;

    public TransTimeout(long maxTimeout) {
        this.maxTimeout = maxTimeout;
    }

    public void start() {
        startTime = System.currentTimeMillis();
        isStopTimer = false;
    }

    public boolean isTimeout() {
//        System.out.println("The startTime + maxTimeout:" + startTime + maxTimeout);
        return !isStopTimer && (startTime + maxTimeout <= System.currentTimeMillis());
    }

    public boolean isStop() {
        return isStopTimer;
    }

    public void stopTimer() {
        isStopTimer = true;
    }
}
