package com.pax.order.entity;

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
 * 2018/8/21 	        wanglz           	Create/Add/Modify/Delete
 * ============================================================================		
 */

import java.io.Serializable;
import java.util.List;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.pax.order.FinancialApplication;
import com.pax.order.enums.SplitStep;
import com.pax.order.enums.SplitType;

@DatabaseTable(tableName = "Pay_Data")
public final class PayData implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String ID_FIELD_NAME = "id";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int mId;

    @DatabaseField
    private boolean mIfSplit;

    @DatabaseField
    private SplitType mSplitType;

    @DatabaseField
    private SplitStep mSplitStep;

    @DatabaseField(canBeNull = true, dataType = DataType.SERIALIZABLE)
    private String[] mSplitAmt;

    @DatabaseField(canBeNull = true, dataType = DataType.SERIALIZABLE)
    private String[] mTipAmt;

    @DatabaseField
    private boolean mIfPaymentProcess = false;

    private static PayData sInstance = null;

    private PayData() {
        mIfSplit = false;
        mSplitType = SplitType.NULL;
        mSplitStep = SplitStep.ZERO;
        mSplitAmt = null;
        mTipAmt = null;
        mIfPaymentProcess = false;
    }

    public void setPayData(PayData payData) {
        mIfSplit = payData.isIfSplit();
        mSplitType = payData.getSplitType();
        mSplitStep = payData.getSplitStep();
        mSplitAmt = payData.getSplitAmt();
        mTipAmt = payData.getTipAmt();
        mIfPaymentProcess = payData.isIfPaymentProcess();
    }

    public void init() {
        mIfSplit = false;
        mSplitType = SplitType.NULL;
        mSplitStep = SplitStep.ZERO;
        mSplitAmt = null;
        mTipAmt = null;
        mIfPaymentProcess = false;
    }

    public static PayData getInstance() {
        if (null == sInstance) {
            sInstance = new PayData();
        }
        return sInstance;
    }

    public boolean isIfSplit() {
        return mIfSplit;
    }

    public void setIfSplit(boolean ifSplit) {
        this.mIfSplit = ifSplit;
    }

    public SplitType getSplitType() {
        return mSplitType;
    }

    public void setSplitType(SplitType splitType) {
        this.mSplitType = splitType;
    }

    public SplitStep getSplitStep() {
        return mSplitStep;
    }

    public void setSplitStep(SplitStep splitStep) {
        this.mSplitStep = splitStep;
    }

    public boolean isIfNoPaidTicket() {
        List<OpenTicket> list = FinancialApplication.getOpenTicketDbHelper().findAllOpenTicketData();
        if ((null != list) && (list.size() > 0)) {
            return true;
        }
        return false;
    }

    public boolean isIfNoPrintReceipt() {
        if (null == FinancialApplication.getPrintDataDb().readPrintData()) {
            return false;
        }
        return true;
    }

    public boolean ifNoTransactionData() {
        List<Transaction> list = FinancialApplication.getTransactionDb().readTransactionList();
        if ((null != list) && (list.size() > 0)) {
            return true;
        }
        return false;
    }


    public int getId() {
        return mId;
    }

    public String[] getSplitAmt() {
        return mSplitAmt;
    }

    public void setSplitAmt(String[] splitAmt) {
        this.mSplitAmt = splitAmt;
    }

    public boolean isIfPaymentProcess() {
        return mIfPaymentProcess;
    }

    public void setIfPaymentProcess(boolean mIfPaymentProcess) {
        this.mIfPaymentProcess = mIfPaymentProcess;
        if (mIfPaymentProcess) {
            FinancialApplication.getApp().setReadyToUpdate(false);
        } else {
            FinancialApplication.getApp().setReadyToUpdate(true);
        }
    }

    public String[] getTipAmt() {
        return mTipAmt;
    }

    public void setTipAmt(String[] tipAmt) {
        mTipAmt = tipAmt;
    }
}
