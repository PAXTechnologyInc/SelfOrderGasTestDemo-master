/*
 * ============================================================================
 * = COPYRIGHT
 *               PAX TECHNOLOGY, Inc. PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or
 *   nondisclosure agreement with PAX  Technology, Inc. and may not be copied
 *   or disclosed except in accordance with the terms in that agreement.
 *      Copyright (C) 2000-2018 PAX Technology, Inc. All rights reserved.
 * Description: // Detail description about the function of this module,
 *             // interfaces with the other modules, and dependencies.
 * Revision History:
 * Date	                 Author	                Action
 * 2018/10/10 	         zenglc           	Create/Add/Modify/Delete
 * ============================================================================
 */
package com.pax.order.eventbus;

public class FCMMessageEvent extends Event {
    public enum Status {
        MSG_SERVER,
        MSG_LOCAL,
    }

    public enum MsgType {
        MSG_TYPE_CALL_SERVERR(1),
        MSG_TYPE_GET_CHECK(2),
        MSG_TYPE_READY_TO_PAY(3),
        MSG_TYPE_PAYMENT_COMPLETE(4),
        MSG_TYPE_MODIFY_TICKET(5),
        MSG_TYPE_SPLIT_TICKET(6),
        MSG_TYPE_ORDER_COMPLETE(7);

        private int mMsgType;

        MsgType(int msgType) {
            this.mMsgType = msgType;
        }

        public int getMsgType() {
            return mMsgType;
        }

        public void setMsgType(int msgType) {
            this.mMsgType = msgType;
        }

        // 根据value返回枚举类型,主要在switch中使用
        public static MsgType getByValue(int value) {
            for (MsgType msgType : values()) {
                if (msgType.getMsgType() == value) {
                    return msgType;
                }
            }
            return null;
        }
    }

    public FCMMessageEvent(Status status) {
        super(status);
    }

    public FCMMessageEvent(Status status, Object data) {
        super(status, data);
    }
}
