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
 * 19-1-17 上午9:48           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 */

package com.pax.order.print;

import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.annotation.StringRes;

import com.pax.gl.page.IPage;
import com.pax.gl.page.IPage.EAlign;
import com.pax.order.FinancialApplication;
import com.pax.order.ParamConstants;
import com.pax.order.R;
import com.pax.order.entity.Order;
import com.pax.order.entity.PrintData;
import com.pax.order.logger.AppLog;
import com.pax.order.pay.paydata.IPayResponse;
import com.pax.order.pay.paydata.PayResponseVar;
import com.pax.order.util.AmountUtils;
import com.pax.order.util.DoubleUtils;
import com.pax.order.util.ImageUtils;
import com.pax.order.util.Tools;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.pax.gl.page.IPage.ILine.IUnit.TEXT_STYLE_ITALIC;

public class OrderAndPayImageGenerator implements IReceiptGenerator {
    private static final String TAG = OrderImageGenertor.class.getSimpleName();
    private static final String BYEVENLY = "BYEVENLY";
    private static final String BYITEM = "BYITEM";
    private PrintData mPrintData;
    private IPayResponse mIPayResponse;


    public OrderAndPayImageGenerator(PrintData printData, IPayResponse IPayResponse) {
        mPrintData = printData;
        mIPayResponse = IPayResponse;
    }

    private String getString(@StringRes int resId) {
        return FinancialApplication.getApp().getString(resId);
    }

    @Override
    public Bitmap generateBitmap() {
        float nameWeight = 0.7f, valueWeight = 0.4f;
        IPage page = Device.generatePage();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        // title
        //String imageDir = getCacheDir() + "/pax_logo_normal.png";
        File cachedImage = new File(FinancialApplication.getApp().getCacheDir(), "/prnImage/pax_logo_normal.png");
        if (cachedImage.exists()) {
            page.addLine().addUnit(ImageUtils.getBitmapByFullPath(cachedImage.getAbsolutePath()), EAlign.CENTER);
        } else {
            page.addLine().addUnit(ImageUtils.getImageFromAssetsFile("pax_logo_normal.png"), EAlign.CENTER);
        }

        String storeName = PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp())
                .getString(ParamConstants.STORE_NAME, "PAXTestStore");
        page.addLine().addUnit("\n", 10);
        if ((storeName != null) && (storeName.length() != 0)) {
            page.addLine().addUnit(getString(R.string.prn_storename) + " " + storeName, FONT_NORMAL, EAlign.LEFT);
        }
        page.addLine().addUnit(getString(R.string.prn_line), FONT_NORMAL, EAlign.CENTER);

        // table id guestNo
        page.addLine().addUnit(getString(R.string.prn_table) + " " + mPrintData.getTableId(), FONT_NORMAL);
//        page.addLine().addUnit(getString(R.string.prn_seq) + " " + mPrintData.getCurrentPayer(), FONT_NORMAL);
//        page.addLine().addUnit(getString(R.string.prn_seq) + " " + mPrintData.getCurrentPayer(), FONT_NORMAL);
        double discount = 0.0;
        if (mPrintData.getOrders() != null) {
            for (Order order : mPrintData.getOrders()) {
                //ordertime
                String transTime = "";
                if(mIPayResponse instanceof PayResponseVar){
                    PayResponseVar payRsp = (PayResponseVar) mIPayResponse;
                    transTime = Tools.dateTimeFormat(payRsp.getTransDate(), payRsp.getTransTime());
                }
                if("".equals(transTime)){
                    Date date = new Date();
                    SimpleDateFormat formatDate = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
                    transTime = formatDate.format(date);
                }

                page.addLine()
                        .addUnit(getString(R.string.prn_ordertime), FONT_NORMAL, valueWeight)
                        .addUnit(/*order.getOrderTime()*/transTime, FONT_NORMAL, EAlign.RIGHT, nameWeight);
                // traceno
                if (mPrintData.getPrintMode().equals(BYITEM) == false) {
                    page.addLine()
                            .addUnit(getString(R.string.prn_ordernum), FONT_NORMAL, 0.3f)
                            .addUnit(order.getTraceNo(), FONT_NORMAL, EAlign.RIGHT, 0.7f);
                }
                page.addLine().addUnit(getString(R.string.prn_line), FONT_NORMAL, EAlign.CENTER);
//            //title
                page.addLine()
                        .addUnit(getString(R.string.prn_dishname), FONT_NORMAL, EAlign.LEFT, (float) 6)
                        .addUnit(getString(R.string.prn_dishunit), FONT_NORMAL, EAlign.CENTER, (float) 4)
                        .addUnit(getString(R.string.prn_dishPrice), FONT_NORMAL, EAlign.RIGHT, (float) 4);
                page.addLine().addUnit();
                AppLog.d(TAG, "generateTotalOrderBitmap: dishTotal:" + order.getDishSortTotal());
                //content
                for (int i = 0; i < Integer.parseInt(order.getDishSortTotal()); i++) {
                    boolean quantityChange = false, priceChange = false;
                    String quantity = order.getSingleDishTotal()[i];
                    String changePriceByPriceChange = "0";
                    String changePriceByQuantityChange = "0";
                    int quantityTmp = 0;
                    double changePriceTmp = 0.0;
                    double changePriceByQuantityChageTmp = 0.0;

                    if (order.getQuantityAdjustment() != null) {
                        if ((order.getQuantityAdjustment()[i] != null)) {
                            quantityTmp = Integer.parseInt(order.getQuantityAdjustment()[i]);
                            if (quantityTmp != 0) {
                                quantityChange = true;
                            }
                        }

                        quantity = String.valueOf(quantityTmp);
                        if (quantityTmp > 0) {
                            quantity = "+" + quantity;
                        }

                        changePriceByQuantityChageTmp = quantityTmp * (Double.parseDouble(order.getSingleDishTotalPrice()[i])
                                / Integer.parseInt(order.getSingleDishTotal()[i]));
                        changePriceByQuantityChange = decimalFormat.format(changePriceByQuantityChageTmp);
                        //由于斜体，会导致打印少一个像素点，故增加后缀空格
                        changePriceByQuantityChange += " ";
                        if (changePriceByQuantityChageTmp > 0) {
                            changePriceByQuantityChange = "+" + changePriceByQuantityChange;
                        }
                    }
                    if (order.getPriceAdjustment() != null) {
                        if (order.getPriceAdjustment()[i] != null) {
                            changePriceTmp = Double.parseDouble(order.getPriceAdjustment()[i]);
                            if (changePriceTmp != 0) {
                                priceChange = true;
                                discount = DoubleUtils.round(DoubleUtils.sum(discount, changePriceTmp));
                            }
                        }

                        changePriceByPriceChange = decimalFormat.format(changePriceTmp);
                        //由于斜体，会导致打印少一个像素点，故增加后缀空格
                        changePriceByPriceChange += " ";
                        if (changePriceTmp > 0) {
                            changePriceByPriceChange = "+" + changePriceByPriceChange;
                        }
                    }
                    String singleDishTotalPriceEnd;

                    singleDishTotalPriceEnd = decimalFormat.format(DoubleUtils.sum(Double.valueOf(order.getSingleDishTotalPrice()[i]),
                            DoubleUtils.sum(changePriceByQuantityChageTmp, changePriceTmp)));
                    singleDishTotalPriceEnd += " ";
                    double oriPrice = 0.0;
                    if ((order.getAtrributeidPrice() != null)
                            && (order.getAtrributeidPrice().length > 0)) {
                        if ((order.getAtrributeidPrice()[i] != null)
                                && (!order.getAtrributeidPrice()[i].isEmpty())) {
                            oriPrice = DoubleUtils.round(DoubleUtils.sub(Double.parseDouble(
                                    order.getSingleDishPrice()[i]), Double.parseDouble(order.getAtrributeidPrice()[i])));
                        } else {
                            oriPrice = DoubleUtils.round(Double.parseDouble(order.getSingleDishPrice()[i]));
                        }
                    } else {
                        oriPrice = DoubleUtils.round(Double.parseDouble(order.getSingleDishPrice()[i]));
                    }

                    if (order.getSingleDishTotal()[i].equals("0") == false) {
                        if (order.getSingleDishTotal()[i].equals("1") == true) {
                            page.addLine()
                                    .addUnit(order.getDishName()[i], FONT_NORMAL, EAlign.LEFT, nameWeight+0.4f)
                                    .addUnit(AmountUtils.amountFormat(oriPrice).toString(), FONT_NORMAL, EAlign.CENTER, nameWeight)
                                    .addUnit(AmountUtils.amountFormat(Double.parseDouble(order.getSingleDishTotalPrice()[i])).toString(), FONT_NORMAL, EAlign.RIGHT, nameWeight);
                        } else {
                            page.addLine()
                                    .addUnit(order.getSingleDishTotal()[i] + " " + order.getDishName()[i], FONT_NORMAL, EAlign.LEFT, nameWeight+0.4f)
                                    .addUnit(AmountUtils.amountFormat(oriPrice).toString(), FONT_NORMAL, EAlign.CENTER, nameWeight)
                                    .addUnit(AmountUtils.amountFormat(Double.parseDouble(order.getSingleDishTotalPrice()[i])).toString(), FONT_NORMAL, EAlign.RIGHT, nameWeight);
                        }
                        if ((order.getAtrributeidName() != null)
                                && (order.getAtrributeidName().length > 0)) {
                            if ((order.getAtrributeidName()[i] != null)
                                    && (!order.getAtrributeidName()[i].isEmpty())) {
                                page.addLine()
                                        .addUnit("    *" +order.getAtrributeidName()[i], FONT_NORMAL, EAlign.LEFT,nameWeight+0.4f)
                                        .addUnit("    " + AmountUtils.amountFormat(
                                                Double.parseDouble(order.getAtrributeidPrice()[i])).toString(), FONT_NORMAL, EAlign.CENTER,nameWeight)
                                        .addUnit(" ", FONT_NORMAL, EAlign.RIGHT,nameWeight);
                            }
                        }
                    }
                    // TODO: 2019/1/8 暂先保留此段代码，待需求进一步明确后再做处理,当前修改为仅保留调整数量的记录，调整价格记录不再展示
                    /*
                    if (quantityChange && priceChange) {
                        quantityChange = false;
                        priceChange = false;
                        page.addLine()
                                .addUnit(quantity + " " + order.getDishName()[i],
                                        FONT_NORMAL, TEXT_STYLE_ITALIC, nameWeight)
                                .addUnit(AmountUtils.amountFormat(Double.parseDouble(changePriceByQuantityChange)).toString(),
                                        FONT_NORMAL, EAlign.RIGHT, TEXT_STYLE_ITALIC, valueWeight);

                        page.addLine()
                                .addUnit(order.getDishName()[i],
                                        FONT_NORMAL, TEXT_STYLE_ITALIC, nameWeight)
                                .addUnit(AmountUtils.amountFormat(Double.parseDouble(changePriceByPriceChange)).toString(),
                                        FONT_NORMAL, EAlign.RIGHT, TEXT_STYLE_ITALIC, valueWeight);
//                        page.addLine()
//                                .addUnit("Modify merge:", FONT_NORMAL, TEXT_STYLE_ITALIC, nameWeight)
//                                .addUnit(AmountUtils.amountFormat(Double.parseDouble(singleDishTotalPriceEnd)).
//                                        toString(), FONT_NORMAL, EAlign.RIGHT, TEXT_STYLE_ITALIC, valueWeight);
                    } else if (quantityChange && (priceChange == false)) {
                        quantityChange = false;
                        page.addLine()
                                .addUnit(quantity + " " + order.getDishName()[i],
                                        FONT_NORMAL, TEXT_STYLE_ITALIC, nameWeight)
                                .addUnit(AmountUtils.amountFormat(Double.parseDouble(changePriceByQuantityChange)).toString(),
                                        FONT_NORMAL, EAlign.RIGHT, TEXT_STYLE_ITALIC, valueWeight);
                    } else if ((quantityChange == false) && priceChange) {
                        priceChange = false;
                        page.addLine()
                                .addUnit(order.getDishName()[i],
                                        FONT_NORMAL, TEXT_STYLE_ITALIC, nameWeight)
                                .addUnit(AmountUtils.amountFormat(Double.parseDouble(changePriceByPriceChange)).toString(),
                                        FONT_NORMAL, EAlign.RIGHT, TEXT_STYLE_ITALIC, valueWeight);

                    }
                    */
                    if (quantityChange) {
                        quantityChange = false;
                        page.addLine()
                                .addUnit(quantity + " " + order.getDishName()[i],
                                        FONT_NORMAL, TEXT_STYLE_ITALIC, nameWeight)
                                .addUnit(AmountUtils.amountFormat(Double.parseDouble(changePriceByQuantityChange)).toString(),
                                        FONT_NORMAL, EAlign.RIGHT, TEXT_STYLE_ITALIC, valueWeight);
                    }
                    page.addLine().addUnit("\n", 4);
                    priceChange = false;
                    quantityChange = false;
                }
                page.addLine().addUnit(getString(R.string.prn_line), FONT_NORMAL, EAlign.CENTER);

                page.addLine()
                        .addUnit(getString(R.string.prn_subtotal), FONT_NORMAL)
                        .addUnit(AmountUtils.amountFormat(DoubleUtils.round(DoubleUtils.sub(
                                Double.parseDouble(order.getTotalPrice()), discount))).toString(), FONT_NORMAL, EAlign.RIGHT);
                page.addLine().addUnit();
            }
        }

        page.addLine().addUnit();

        page.addLine().addUnit(getString(R.string.prn_totaltax), FONT_NORMAL).addUnit(" ", FONT_NORMAL)
                .addUnit(AmountUtils.amountFormat(Double.parseDouble(mPrintData.getTotalOrderTaxAmount())).toString(), FONT_NORMAL, EAlign.RIGHT);
        if (!mPrintData.getPrintMode().equals(BYITEM)) {
            if (discount > 0) {
                page.addLine().addUnit(getString(R.string.prn_discount), FONT_NORMAL)
                        .addUnit(" ", FONT_NORMAL)
                        .addUnit(AmountUtils.amountFormat(DoubleUtils.round(discount)).toString(), FONT_NORMAL, EAlign.RIGHT);
            }
        }
        page.addLine().addUnit(getString(R.string.prn_totaltip), FONT_NORMAL)
                .addUnit(" ", FONT_NORMAL)
                .addUnit(AmountUtils.amountFormat(Double.parseDouble(mPrintData.getTotalOrderTipAmount())).toString(), FONT_NORMAL, EAlign.RIGHT);

        if ((Integer.parseInt(mPrintData.getTotalGuest()) > 1) && (mPrintData.getPrintMode().equals(BYEVENLY) == true)) {
            String proportion;
            proportion = "(1/" + mPrintData.getTotalGuest() + "):";
            page.addLine().addUnit(getString(R.string.prn_total) + proportion, FONT_NORMAL)
                    .addUnit(" ", FONT_NORMAL)
                    .addUnit(AmountUtils.amountFormat(Double.parseDouble(mPrintData.getTotalOrderPrice())).toString(), FONT_NORMAL, EAlign.RIGHT);
        } else {
            page.addLine().addUnit(getString(R.string.prn_total) + ":", FONT_NORMAL)
                    .addUnit(" ", FONT_NORMAL)
                    .addUnit(AmountUtils.amountFormat(Double.parseDouble(mPrintData.getTotalOrderPrice())).toString(), FONT_NORMAL, EAlign.RIGHT);
        }
        page.addLine().addUnit(getString(R.string.prn_line), FONT_NORMAL, EAlign.CENTER);
//        page.addLine().addUnit(mIPayResponse.getTransType(), FONT_BIG, IPage.EAlign.LEFT);
//        page.addLine().addUnit("\n", FONT_SMALL);
        //金额大于0才会用卡
        if (Double.parseDouble(mPrintData.getTotalOrderPrice()) > 0) {
            page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_cardType), FONT_NORMAL, IPage.EAlign.LEFT, nameWeight)
                    .addUnit(mIPayResponse.getCardOrg(), FONT_NORMAL, IPage.EAlign.RIGHT, valueWeight);
            page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_account), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                    .addUnit(mIPayResponse.getCardNo(), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
            page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_entry), FONT_NORMAL, IPage.EAlign.LEFT, nameWeight)
                    .addUnit(mIPayResponse.getEntryMode(), FONT_NORMAL, IPage.EAlign.RIGHT, valueWeight);
            page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_expireDate), FONT_NORMAL, IPage.EAlign.LEFT, nameWeight)
                    .addUnit(mIPayResponse.getExpireDate(), FONT_NORMAL, IPage.EAlign.RIGHT, valueWeight);
            page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_batchNo), FONT_NORMAL, IPage.EAlign.LEFT, nameWeight)
                    .addUnit(mIPayResponse.getBatchNum(), FONT_NORMAL, IPage.EAlign.RIGHT, valueWeight);
            page.addLine().addUnit();
//            page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_line), FONT_NORMAL, IPage.EAlign.CENTER);
            page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_refNum), FONT_NORMAL, IPage.EAlign.LEFT, nameWeight)
                    .addUnit(mIPayResponse.getRefNum(), FONT_NORMAL, IPage.EAlign.RIGHT, valueWeight);
            page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_authCode), FONT_NORMAL, IPage.EAlign.LEFT, nameWeight)
                    .addUnit(mIPayResponse.getAuthCode(), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
            page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_resMessage), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                    .addUnit(mIPayResponse.getHostMessage(), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
            if ((mIPayResponse.getTc() != null) && (mIPayResponse.getTc().length() > 0)) {
                page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_tc), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                        .addUnit(mIPayResponse.getTc(), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
            }
            if ((mIPayResponse.getAtc() != null) && (mIPayResponse.getAtc().length() > 0)) {
                page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_atc), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                        .addUnit(mIPayResponse.getAtc(), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
            }
            if ((mIPayResponse.getTvr() != null) && (mIPayResponse.getTvr().length() > 0)) {
                page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_tvr), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                        .addUnit(mIPayResponse.getTvr(), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
            }
            if ((mIPayResponse.getAid() != null) && (mIPayResponse.getAid().length() > 0)) {
                page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_aid), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                        .addUnit(mIPayResponse.getAid(), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
            }
            if ((mIPayResponse.getIad() != null) && (mIPayResponse.getIad().length() > 0)) {
                page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_iad), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                        .addUnit(mIPayResponse.getIad(), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
            }
            if ((mIPayResponse.getTsi() != null) && (mIPayResponse.getTsi().length() > 0)) {
                page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_tsi), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                        .addUnit(mIPayResponse.getTsi(), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
            }
            if ((mIPayResponse.getArc() != null) && (mIPayResponse.getArc().length() > 0)) {
                page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_arc), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                        .addUnit(mIPayResponse.getArc(), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
            }
            if ((mIPayResponse.getAppn() != null) && (mIPayResponse.getAppn().length() > 0)) {
                page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_appn), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                        .addUnit(mIPayResponse.getAppn(), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
            }
            if ((mIPayResponse.getApplab() != null) && (mIPayResponse.getApplab().length() > 0)) {
                page.addLine().addUnit(FinancialApplication.getApp().getString(R.string.prn_applab), FONT_NORMAL, IPage.EAlign.LEFT, valueWeight)
                        .addUnit(mIPayResponse.getApplab(), FONT_NORMAL, IPage.EAlign.RIGHT, nameWeight);
            }
            page.addLine().addUnit();
            page.addLine().addUnit(getString(R.string.prn_line), FONT_NORMAL, EAlign.CENTER);
        }
        String reference = PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp())
                .getString(ParamConstants.PRT_REMARK, "");
        if ((reference != null) && (reference.length() != 0)) {
            page.addLine().addUnit(reference, FONT_NORMAL);
            page.addLine().addUnit("\n", 5);
        }
        page.addLine().addUnit(getString(R.string.prn_end), FONT_NORMAL, EAlign.CENTER);
        page.addLine().addUnit("\n\n\n\n\n\n", FONT_NORMAL);

        Bitmap pageMap = FinancialApplication.getApp().getGlPage().pageToBitmap(page, 384);

        return pageMap;
    }

    @Override
    public String generateString() {
        return null;
    }
}
