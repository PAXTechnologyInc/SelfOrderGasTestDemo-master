package com.pax.order.eventbus;

public class DetectCardAnytimeEvent extends Event {
    public enum Status {
        START_DETECT_CARD,
        STOP_DETECT_CARD,
    }

    public DetectCardAnytimeEvent(Object status) {
        super(status);
    }

    public DetectCardAnytimeEvent(Object status, Object data) {
        super(status, data);
    }
}
