/*
 *
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
 * 18-12-25 下午4:13           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 *
 */

package com.pax.order.print;

import android.graphics.Bitmap;

import com.pax.gl.page.IPage;
import com.pax.order.FinancialApplication;
import com.pax.order.R;
import com.pax.order.pay.paydata.IBaseResponse;
import com.pax.order.pay.paydata.IBatchResponse;

import java.text.DecimalFormat;

public class BatchInfoImageGenerator implements IReceiptGenerator {

    private IBatchResponse mIBatchResponse;

    public BatchInfoImageGenerator(IBaseResponse responseVar) {
        mIBatchResponse = (IBatchResponse) responseVar;
    }

    @Override
    public Bitmap generateBitmap() {
        float nameWeight = 0.7f, valueWeight = 0.4f;
        IPage page = Device.generatePage();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        // title
        page.addLine().addUnit("\n\n", FONT_SMALL);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_batchline), FONT_NORMAL, IPage.EAlign.LEFT);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_batchTransName), FONT_NORMAL, IPage.EAlign.CENTER);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_line), FONT_NORMAL, IPage.EAlign.LEFT);
        page.addLine().addUnit(mIBatchResponse.getTransDate(), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                .addUnit(mIBatchResponse.getTransTime(), FONT_NORMAL, IPage.EAlign.RIGHT, valueWeight);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_line), FONT_NORMAL, IPage.EAlign.LEFT);

        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_sn), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                .addUnit(mIBatchResponse.getSn(), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_batchNo), FONT_NORMAL, IPage.EAlign.LEFT, nameWeight)
                .addUnit(mIBatchResponse.getBatchNum(), FONT_NORMAL, IPage.EAlign.RIGHT, valueWeight);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_line), FONT_NORMAL, IPage.EAlign.RIGHT);

        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_edc), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                .addUnit(FinancialApplication.getApp().getString(R.string.prn_credit), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_creditCount), FONT_NORMAL, IPage.EAlign.LEFT, nameWeight)
                .addUnit(mIBatchResponse.getCreditCount(), FONT_NORMAL, IPage.EAlign.RIGHT, valueWeight);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_creditAmt), FONT_NORMAL, IPage.EAlign.LEFT, nameWeight)
                .addUnit(mIBatchResponse.getCreditAmount(), FONT_NORMAL, IPage.EAlign.RIGHT, valueWeight);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_line), FONT_NORMAL, IPage.EAlign.LEFT);

        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_edc), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                .addUnit(FinancialApplication.getApp().getString(R.string.prn_debit), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_debitCount), FONT_NORMAL, IPage.EAlign.LEFT, nameWeight)
                .addUnit(mIBatchResponse.getDebitCount(), FONT_NORMAL, IPage.EAlign.RIGHT, valueWeight);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_debitAmt), FONT_NORMAL, IPage.EAlign.LEFT, nameWeight)
                .addUnit(mIBatchResponse.getDebitAmount(), FONT_NORMAL, IPage.EAlign.RIGHT, valueWeight);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_line), FONT_NORMAL, IPage.EAlign.LEFT);

        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_edc), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                .addUnit(FinancialApplication.getApp().getString(R.string.prn_cash), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_cashCount), FONT_NORMAL, IPage.EAlign.LEFT, nameWeight)
                .addUnit(mIBatchResponse.getCashCount(), FONT_NORMAL, IPage.EAlign.RIGHT, valueWeight);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_cashAmt), FONT_NORMAL, IPage.EAlign.LEFT, nameWeight)
                .addUnit(mIBatchResponse.getCashAmt(), FONT_NORMAL, IPage.EAlign.RIGHT, valueWeight);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_line), FONT_NORMAL, IPage.EAlign.LEFT);

        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_edc), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                .addUnit(FinancialApplication.getApp().getString(R.string.prn_check), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_checkCount), FONT_NORMAL, IPage.EAlign.LEFT, nameWeight)
                .addUnit(mIBatchResponse.getCheckCount(), FONT_NORMAL, IPage.EAlign.RIGHT, valueWeight);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_checkAmt), FONT_NORMAL, IPage.EAlign.LEFT, nameWeight)
                .addUnit(mIBatchResponse.getCheckAmt(), FONT_NORMAL, IPage.EAlign.RIGHT, valueWeight);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_line), FONT_NORMAL, IPage.EAlign.LEFT);

        page.addLine().addUnit("\n", FONT_SMALL);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_totalCount), FONT_NORMAL, IPage.EAlign.LEFT, nameWeight)
                .addUnit(mIBatchResponse.getTotalBatchCount(), FONT_NORMAL, IPage.EAlign.RIGHT, valueWeight);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_overall), FONT_NORMAL, IPage.EAlign.LEFT, nameWeight)
                .addUnit(mIBatchResponse.getTotalBatchAmt(), FONT_NORMAL, IPage.EAlign.RIGHT, valueWeight);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_endReport), FONT_NORMAL, IPage.EAlign.LEFT);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_line), FONT_NORMAL, IPage.EAlign.CENTER);

        page.addLine().addUnit("\n\n\n\n\n\n", FONT_NORMAL);


        Bitmap pageMap = FinancialApplication.getApp().getGlPage().pageToBitmap(page, 384);

        return pageMap;


    }

    @Override
    public String generateString() {
        return null;
    }
}
