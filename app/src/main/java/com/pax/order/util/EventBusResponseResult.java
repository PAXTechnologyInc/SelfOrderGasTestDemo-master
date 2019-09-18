package com.pax.order.util;

/**
 * Created by lixc on 2017/5/9.
 */

public class EventBusResponseResult {
    private String resultMsg;

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public enum ResultEvent {
        SUCCESS,
        FAILED,
    }
}
