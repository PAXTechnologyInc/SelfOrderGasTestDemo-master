package com.pax.order.adapter;

import com.pax.order.FinancialApplication;
import com.pax.dal.IPicc;
import com.pax.dal.entity.EDetectMode;
import com.pax.dal.entity.EPiccRemoveMode;
import com.pax.dal.entity.EPiccType;
import com.pax.dal.entity.PiccCardInfo;
import com.pax.dal.entity.PiccPara;
import com.pax.dal.exceptions.PiccDevException;
import com.pax.order.logger.AppLog;

public class PiccReader {
    private static PiccReader piccTester;
    private IPicc picc;
    private static EPiccType piccType;

    private PiccReader(EPiccType type) {
        piccType = type;
        picc = FinancialApplication.getDal().getPicc(piccType);
    }

    public static PiccReader getInstance(EPiccType type) {
        if (piccTester == null || type != piccType) {
            piccTester = new PiccReader(type);
        }
        return piccTester;
    }

    // 读取当前参数设置
    public PiccPara setUp() {
        try {
            return picc.readParam();
        } catch (Exception e) {
            AppLog.e(e.getMessage());
            return null;
        }
    }

    public void open() {
        try {
            picc.open();
        } catch (Exception e) {
            AppLog.e(e.getMessage());
        }
    }

    public PiccCardInfo detect(EDetectMode mode) throws PiccDevException {
        return picc.detect(mode);
    }


    public void remove(EPiccRemoveMode mode, byte cid) {
        try {
            picc.remove(mode, cid);
        } catch (Exception e) {
            AppLog.e(e.getMessage());
        }
    }

    public void setLed(byte led) {
        try {
            picc.setLed(led);
        } catch (Exception e) {
            AppLog.e(e.getMessage());
        }
    }

    public void close() {
        try {
            picc.close();
        } catch (Exception e) {
            AppLog.e(e.getMessage());
        }
    }

}
