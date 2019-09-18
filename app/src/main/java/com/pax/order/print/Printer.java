/*
 * ============================================================================
 * = COPYRIGHT
 *     PAX Computer Technology(Shenzhen) CO., LTD PROPRIETARY INFORMATION
 *     This software is supplied under the terms of a license agreement or
 *     nondisclosure agreement with PAX  Computer Technology(Shenzhen) CO., LTD.
 *     and may not be copied or disclosed except in accordance with the terms
 *     in that agreement.
 *          Copyright (C) 2018 -? PAX Computer Technology(Shenzhen) CO., LTD.
 *          All rights reserved.Revision History:
 * Date                      Author                        Action
 * 18-8-15 下午5:51           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 */

package com.pax.order.print;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import com.pax.dal.IPrinter;
import com.pax.dal.IPrinter.IPinterListener;
import com.pax.dal.exceptions.PrinterDevException;
import com.pax.order.FinancialApplication;

public class Printer {
    private static final int START_PRN = 0;
    private static final int FULL_CUT = 0;
    public Handler mHandler;
    IPrinter printer = FinancialApplication.getDal().getPrinter();

    public Printer() {
    }

    public Printer(Handler mHandler) {
        this.mHandler = mHandler;
    }

    public int printBitmap(Bitmap bitmap) {

        IPinterListener printListener = new PrintListenr();
        try {
            printer.init();
            printer.print(bitmap, FULL_CUT, printListener);
            Message message = new Message();
            message.what = START_PRN;
            mHandler.sendMessage(message);
            return 0;
        } catch (PrinterDevException e) {
            e.printStackTrace();
        }
        return -1;
    }

    class PrintListenr implements IPinterListener {

        private static final int PRN_SUCC = 100;

        @Override
        public void onError(int i) {
            if (i != 0) {
                Message message = new Message();
                message.what = i;
                mHandler.sendMessage(message);
            }
        }

        @Override
        public void onSucc() {
            Message message = new Message();
            message.what = PRN_SUCC;
            mHandler.sendMessage(message);
            try {
//                printer.step(10000);
                printer.cutPaper(FULL_CUT);
            } catch (PrinterDevException e) {
                e.printStackTrace();
            }

        }
    }
}

