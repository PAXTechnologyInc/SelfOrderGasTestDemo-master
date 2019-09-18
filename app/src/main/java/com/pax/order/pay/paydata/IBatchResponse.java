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
 * 18-12-24 下午7:58           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 *
 */

package com.pax.order.pay.paydata;

public interface IBatchResponse extends IBaseResponse {
    String getCashAmt();

    void setCashAmt(String cashAmt);

    String getCashCount();

    void setCashCount(String cashCount);

    String getCheckAmt();

    void setCheckAmt(String checkAmt);

    String getCheckCount();

    void setCheckCount(String checkCount);

    String getCreditAmount();

    void setCreditAmount(String creditAmount);

    String getCreditCount();

    void setCreditCount(String creditCount);

    String getDebitAmount();

    void setDebitAmount(String debitAmount);

    String getDebitCount();

    void setDebitCount(String debitCount);

    String getSn();

    void setSn(String sn);

    String getTotalBatchAmt();

    void setTotalBatchAmt(String totalBatchAmt);

    String getTotalBatchCount();

    void setTotalBatchCount(String totalBatchCount);

}
