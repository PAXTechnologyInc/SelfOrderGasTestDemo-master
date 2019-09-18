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
 * 2018/9/25 	         zenglc           	Create/Add/Modify/Delete
 * ============================================================================
 */
package com.pax.order.util;

import java.math.BigDecimal;

public class DoubleUtils {
    /**
     * 对double数据进行取精度.
     *
     * @param value        double数据.
     * @param scale        精度位数(保留的小数位数).
     * @param roundingMode 精度取值方式.
     * @return 精度计算后的数据.
     */
    public static double round(double value, int scale, int roundingMode) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(scale, roundingMode);
        double d = bd.doubleValue();
        bd = null;
        return d;
    }

    /**
     * 对double数据进行取精度.（四舍五入, 保留的小数2位数)
     *
     * @param value double数据.
     * @return 精度计算后的数据.
     */
    public static double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        double d = bd.doubleValue();
        bd = null;
        return d;
    }

    /**
     * <span style="white-space:pre">	</span> * 对double数据 进行去除没用的.0
     * <span style="white-space:pre">	</span> *
     * <span style="white-space:pre">	</span> * @param value
     * <span style="white-space:pre">	</span> *            double数据.
     * <span style="white-space:pre">	</span> * @return 精度计算后的数据.
     * <span style="white-space:pre">	</span>
     */
    public String killling(double value) {
        int b = (int) value;
        if (value == b) {
            return String.valueOf(b);
        } else {
            return String.valueOf(value);
        }

    }

    /**
     * double 相加
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double sum(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.add(bd2)
                .doubleValue();
    }

    public static double sum(double d1, String d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(d2);
        return bd1.add(bd2)
                .doubleValue();
    }

    public static double sum(String d1, String d2) {
        BigDecimal bd1 = new BigDecimal(d1);
        BigDecimal bd2 = new BigDecimal(d2);
        return bd1.add(bd2)
                .doubleValue();
    }

    /**
     * double 相减
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double sub(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.subtract(bd2)
                .doubleValue();
    }

    public static double sub(String d1, String d2) {
        BigDecimal bd1 = new BigDecimal(d1);
        BigDecimal bd2 = new BigDecimal(d2);
        return bd1.subtract(bd2)
                .doubleValue();
    }

    /**
     * double 乘法
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double mul(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.multiply(bd2)
                .doubleValue();
    }

    public static double mul(String d1, String d2) {
        BigDecimal bd1 = new BigDecimal(d1);
        BigDecimal bd2 = new BigDecimal(d2);
        return bd1.multiply(bd2)
                .doubleValue();
    }

    public static double mul(double d1, String d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(d2);
        return bd1.multiply(bd2)
                .doubleValue();
    }

    /**
     * double 除法
     *
     * @param d1
     * @param d2
     * @param scale 四舍五入 小数点位数
     * @return
     */
    public static double div(double d1, double d2, int scale) {
        // 当然在此之前，你要判断分母是否为0，
        // 为0你可以根据实际需求做相应的处理
        // scale传入-1，不进行四舍五入
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        if (scale == -1) {
            return bd1.divide(bd2).doubleValue();
        } else {
            return bd1.divide(bd2, scale, BigDecimal.ROUND_HALF_UP)
                    .doubleValue();
        }
    }
}