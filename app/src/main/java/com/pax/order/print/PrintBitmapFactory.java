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
 * 18-12-25 下午8:42           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 *
 */

package com.pax.order.print;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.pax.order.FinancialApplication;
import com.pax.order.entity.PrintData;
import com.pax.order.pay.paydata.IBaseResponse;
import com.pax.order.pay.paydata.IBatchResponse;
import com.pax.order.pay.paydata.IPayResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PrintBitmapFactory {
    private PrintBitmapFactory() {
    }

    public static final String PAYPRN_NAME = "payprn";
    public static final String BATCHPRN_NAME = "batchprn";
    public static final String ORDERPRN_NAME = "orderprn";
    public static final String ORDERPAYPRN_NAME = "orderpayprn";
    public static final String RPRN_TAG = "prntag";

    public static Bitmap genPrnBitmap(PrintData printData) {

        if (printData == null) {
            return null;
        }
        IReceiptGenerator receiptGenerator = new OrderImageGenertor(printData);
        Bitmap orderBitMap = receiptGenerator.generateBitmap();
        if (orderBitMap.getHeight() > 4096) {
            orderBitMap = Bitmap.createScaledBitmap(orderBitMap, 384, 4096, true);
        }
        return orderBitMap;

    }

    public static Bitmap genPrnBitmap(PrintData printData, IPayResponse payResponse) {

        if ((printData == null) || (payResponse == null)) {
            return null;
        }
        IReceiptGenerator receiptGenerator = new OrderAndPayImageGenerator(printData, payResponse);
        Bitmap orderBitMap = receiptGenerator.generateBitmap();
        if (orderBitMap.getHeight() > 4096) {
            orderBitMap = Bitmap.createScaledBitmap(orderBitMap, 384, 4096, true);
        }
        return orderBitMap;

    }

    public static Bitmap genPrnBitmap(IBaseResponse responseVar) {

        if (responseVar == null) {
            return null;
        }
        IReceiptGenerator receiptGenerator;
        if (responseVar instanceof IPayResponse) {
            receiptGenerator = new PayInfoImageGenerator(responseVar);
        } else if (responseVar instanceof IBatchResponse) {
            receiptGenerator = new BatchInfoImageGenerator(responseVar);
        } else {
            return null;
        }
        Bitmap orderBitMap = receiptGenerator.generateBitmap();
        if (orderBitMap.getHeight() > 4096) {
            orderBitMap = Bitmap.createScaledBitmap(orderBitMap, 384, 4096, true);
        }
        return orderBitMap;

    }


    public static void savePrnBitmap(String prnBitmapName, Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }

        File imageDir = new File(FinancialApplication.getApp().getCacheDir(), "prnImage");
        if (!imageDir.isDirectory()) {
            imageDir.mkdirs();
        }

        File cachedImage = new File(imageDir, prnBitmapName);
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(cachedImage);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Bitmap getPrnBitmap(String prnBitmapName) {
        File imageDir = new File(FinancialApplication.getApp().getCacheDir(), "prnImage");
        if (!imageDir.isDirectory()) {
            imageDir.mkdirs();
        }
        File imageFile = new File(imageDir, prnBitmapName);
        if (!imageFile.exists() || imageFile.length() == 0) {
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        imageFile.delete();
        if (bitmap != null) {
            return bitmap;
        } else {
            return null;
        }
    }
}
