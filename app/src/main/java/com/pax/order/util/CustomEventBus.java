package com.pax.order.util;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by lixc on 2017/6/6.
 */

public class CustomEventBus {

    private CustomEventBus() {

    }

    public static void register(Object obj) {
        EventBus.getDefault().register(obj);
    }

    public static void unregister(Object obj) {
        EventBus.getDefault().unregister(obj);
    }

    public static void doEvent(EventBusResponseResult.ResultEvent event) {
        EventBus.getDefault().post(event);
    }
}
