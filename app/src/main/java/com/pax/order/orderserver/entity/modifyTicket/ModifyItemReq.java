package com.pax.order.orderserver.entity.modifyTicket;

/**
 * Created by chenyr on 2018/11/15.
 */

public class ModifyItemReq {
    private String OrderLineID;
    private String DiscountAmt;

    public ModifyItemReq() {
        OrderLineID = "";
        DiscountAmt = "";
    }

    public String getTicketItemNum() {
        return OrderLineID;
    }

    public void setTicketItemNum(String ticketItemNum) {
        OrderLineID = ticketItemNum;
    }

    public String getDiscountAmt() {
        return DiscountAmt;
    }

    public void setDiscountAmt(String discountAmt) {
        DiscountAmt = discountAmt;
    }
}
