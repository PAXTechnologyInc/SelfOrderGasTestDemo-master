package com.pax.utils.eventbus;

import com.pax.order.eventbus.Event;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Leon.F on 2017/12/28.
 */

public class EventBusUtil {

    public static void doEvent(Event event) {
        EventBus.getDefault().post(event);
    }

    public static void unregister(Object obj) {
        if (EventBus.getDefault().isRegistered(obj))
            EventBus.getDefault().unregister(obj);
    }

    public static boolean isRegisteredToEventBus(Object obj) {
        return EventBus.getDefault().isRegistered(obj);
    }

    public static void register(Object obj) {
        EventBus.getDefault().register(obj);
    }
}
