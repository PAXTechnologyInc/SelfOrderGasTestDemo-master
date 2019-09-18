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
 * 2018/9/10 	         zenglc           	Create/Add/Modify/Delete
 * ============================================================================
 */
package com.pax.order.util;

import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;

import java.text.NumberFormat;
import java.util.Locale;

public class AmountUtils {

    private static NumberFormat nf = null;

    public static SpannableString amountFormat(double amount) {
        if (nf == null) {
            nf = NumberFormat.getCurrencyInstance(Locale.US);
            nf.setMaximumFractionDigits(2);
        }

        SpannableString strAmount = new SpannableString(nf.format(amount));
        strAmount.setSpan(new RelativeSizeSpan(0.7f), 0, 1, 0); // set size
        return strAmount;
    }

    public static SpannableString my_amountFormat(double amount) {
        if (nf == null) {
            nf = NumberFormat.getCurrencyInstance(Locale.US);
            nf.setMaximumFractionDigits(2);
        }

        SpannableString strAmount = new SpannableString("+" + nf.format(amount));
        strAmount.setSpan(new RelativeSizeSpan(0.7f), 0, 1, 0); // set size
        return strAmount;
    }
}
