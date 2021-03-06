/*
 * ============================================================================
 * = COPYRIGHT
 *               PAX TECHNOLOGY, Inc. PROPRIETARY INFORMATION
 *    This software is supplied under the terms of a license agreement or
 *    nondisclosure agreement with PAX  Technology, Inc. and may not be copied
 *    or disclosed except in accordance with the terms in that agreement.
 *        Copyright (C) 2018 -? PAX Technology, Inc. All rights reserved.
 * Revision History:
 * Date	                     Author	                        Action
 * 18-9-13 下午8:25  	     JoeyTan           	    Create/Add/Modify/Delete
 * ============================================================================
 */
package com.pax.order.orderserver.inf;

/**
 * download pictures callback class
 */
public interface DLPictureListener {
    /**
     * download success
     */
    void onDownloadSuccess();

    /**
     * download failed.
     *
     * @param errMsg error message
     */
    void onDownloadFailed(String errMsg);

    void onProgress(int succeedCount, int totalCount);
}
