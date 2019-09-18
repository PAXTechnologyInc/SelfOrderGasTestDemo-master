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
 * 18-12-24 下午7:23           wangxf                 Create/Add/Modify/Delete
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
import com.pax.order.pay.paydata.IPayResponse;
import com.pax.order.util.ImageUtils;

import java.io.File;
import java.text.DecimalFormat;

public class PayInfoImageGenerator implements IReceiptGenerator {
    private IPayResponse mIPayResponseVar;

    public PayInfoImageGenerator(IBaseResponse responseVar) {
        mIPayResponseVar = (IPayResponse) responseVar;
    }

    @Override
    public Bitmap generateBitmap() {
        float nameWeight = 0.7f, valueWeight = 0.4f;
        IPage page = Device.generatePage();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        // title
        File cachedImage = new File(FinancialApplication.getApp().getCacheDir(), "/prnImage/pax_logo_normal.png");
        if (cachedImage.exists()) {
            page.addLine().addUnit(ImageUtils.getBitmapByFullPath(cachedImage.getAbsolutePath()), IPage.EAlign.CENTER);
        } else {
            page.addLine().addUnit(ImageUtils.getImageFromAssetsFile("pax_logo_normal.png"), IPage.EAlign.CENTER);
        }
        page.addLine().addUnit("\n", FONT_SMALL);
        page.addLine().addUnit(mIPayResponseVar.getTransDate(), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                .addUnit(mIPayResponseVar.getTransTime(), FONT_NORMAL, IPage.EAlign.RIGHT, valueWeight);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_line), FONT_NORMAL, IPage.EAlign.CENTER);
        page.addLine().addUnit(mIPayResponseVar.getTransType(), FONT_BIG, IPage.EAlign.LEFT);
        page.addLine().addUnit("\n", FONT_SMALL);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_cardType), FONT_NORMAL, IPage.EAlign.LEFT, nameWeight)
                .addUnit(mIPayResponseVar.getCardOrg(), FONT_NORMAL, IPage.EAlign.RIGHT, valueWeight);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_account), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                .addUnit(mIPayResponseVar.getCardNo(), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_entry), FONT_NORMAL, IPage.EAlign.LEFT, nameWeight)
                .addUnit(mIPayResponseVar.getEntryMode(), FONT_NORMAL, IPage.EAlign.RIGHT, valueWeight);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_expireDate), FONT_NORMAL, IPage.EAlign.LEFT, nameWeight)
                .addUnit(mIPayResponseVar.getExpireDate(), FONT_NORMAL, IPage.EAlign.RIGHT, valueWeight);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_batchNo), FONT_NORMAL, IPage.EAlign.LEFT, nameWeight)
                .addUnit(mIPayResponseVar.getBatchNum(), FONT_NORMAL, IPage.EAlign.RIGHT, valueWeight);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_mdse), FONT_NORMAL, IPage.EAlign.LEFT, nameWeight)
                .addUnit(mIPayResponseVar.getApprovedAmt(), FONT_NORMAL, IPage.EAlign.RIGHT, valueWeight);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_tip), FONT_NORMAL, IPage.EAlign.LEFT, nameWeight)
                .addUnit(mIPayResponseVar.getTipAmt(), FONT_NORMAL, IPage.EAlign.RIGHT, valueWeight);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_line), FONT_NORMAL, IPage.EAlign.CENTER);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_totalPay), FONT_NORMAL, IPage.EAlign.LEFT, nameWeight)
                .addUnit(mIPayResponseVar.getTotalAmt(), FONT_NORMAL, IPage.EAlign.RIGHT, valueWeight);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_line), FONT_NORMAL, IPage.EAlign.CENTER);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_refNum), FONT_NORMAL, IPage.EAlign.LEFT, nameWeight)
                .addUnit(mIPayResponseVar.getRefNum(), FONT_NORMAL, IPage.EAlign.RIGHT, valueWeight);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_authCode), FONT_NORMAL, IPage.EAlign.LEFT, nameWeight)
                .addUnit(mIPayResponseVar.getAuthCode(), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_resMessage), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                .addUnit(mIPayResponseVar.getHostMessage(), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
        if ((mIPayResponseVar.getTc() != null) && (mIPayResponseVar.getTc().length() > 0)) {
            page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_tc), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                    .addUnit(mIPayResponseVar.getTc(), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
        }
        if ((mIPayResponseVar.getAtc() != null) && (mIPayResponseVar.getAtc().length() > 0)) {
            page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_atc), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                    .addUnit(mIPayResponseVar.getAtc(), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
        }
        if ((mIPayResponseVar.getTvr() != null) && (mIPayResponseVar.getTvr().length() > 0)) {
            page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_tvr), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                    .addUnit(mIPayResponseVar.getTvr(), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
        }
        if ((mIPayResponseVar.getAid() != null) && (mIPayResponseVar.getAid().length() > 0)) {
            page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_aid), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                    .addUnit(mIPayResponseVar.getAid(), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
        }
        if ((mIPayResponseVar.getIad() != null) && (mIPayResponseVar.getIad().length() > 0)) {
            page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_iad), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                    .addUnit(mIPayResponseVar.getIad(), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
        }
        if ((mIPayResponseVar.getTsi() != null) && (mIPayResponseVar.getTsi().length() > 0)) {
            page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_tsi), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                    .addUnit(mIPayResponseVar.getTsi(), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
        }
        if ((mIPayResponseVar.getArc() != null) && (mIPayResponseVar.getArc().length() > 0)) {
            page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_arc), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                    .addUnit(mIPayResponseVar.getArc(), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
        }
        if ((mIPayResponseVar.getAppn() != null) && (mIPayResponseVar.getAppn().length() > 0)) {
            page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_appn), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                    .addUnit(mIPayResponseVar.getAppn(), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
        }
        if ((mIPayResponseVar.getApplab() != null) && (mIPayResponseVar.getApplab().length() > 0)) {
            page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_applab), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                    .addUnit(mIPayResponseVar.getApplab(), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
        }
        page.addLine().addUnit("\n", FONT_NORMAL);
        page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_end), FONT_NORMAL, IPage.EAlign.CENTER);

        page.addLine().addUnit("\n\n\n\n\n\n", FONT_NORMAL);


        Bitmap pageMap = FinancialApplication.getApp().getGlPage().pageToBitmap(page, 384);

        return pageMap;


    }

    @Override
    public String generateString() {
        return null;
    }
}
