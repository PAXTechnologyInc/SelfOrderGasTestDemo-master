package com.pax.order.adapter;


import com.pax.order.FinancialApplication;
import com.pax.dal.IIcc;
import com.pax.dal.exceptions.IccDevException;
import com.pax.order.logger.AppLog;

public class IccReader {
    private static IccReader iccTester;

    private IIcc icc;

    private IccReader() {
        icc = FinancialApplication.getDal().getIcc();
    }

    public static IccReader getInstance() {
        if (iccTester == null) {
            iccTester = new IccReader();
        }
        return iccTester;
    }

    public byte[] init(byte slot) {
        try {
            return icc.init(slot);
        } catch (Exception e) {
            AppLog.e(e.getMessage());
            return null;
        }
    }

    public boolean detect(byte slot) {
        try {
            return icc.detect(slot);
        } catch (Exception e) {
            AppLog.e(e.getMessage());
            return false;
        }
    }

    public void close(byte slot) {
        try {
            icc.close(slot);
        } catch (Exception e) {
            AppLog.e(e.getMessage());
        }
    }

    public void iccLight(boolean on) {
        try {
            icc.light(on);
        } catch (IccDevException e) {
            e.printStackTrace();
        }
    }

    public void autoResp(byte slot, boolean autoresp) {
        try {
            icc.autoResp(slot, autoresp);
        } catch (Exception e) {
            AppLog.e(e.getMessage());
        }
    }

    public byte[] isoCommand(byte slot, byte[] send) {
        try {
            return icc.isoCommand(slot, send);
        } catch (Exception e) {
            AppLog.e(e.getMessage());
            return null;
        }
    }
}
