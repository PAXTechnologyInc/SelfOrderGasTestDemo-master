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
package com.pax.order.orderserver.entity;

import java.io.File;

/**
 * download job class
 */
public class DownloadJob {
    private String mUrl;
    private String mLocalDir;
    private String mLocalOldDir;
    private String mImageName;

    /**
     * constructor: backup local directory to mLocalOldDir
     * @param url picture URL
     * @param localDir local directory /data/data/files/
     * @param imageName image file name with format suffix, e.g. test.png
     */
    public DownloadJob(String url, String localDir, String imageName) {
        this.mUrl = url;
        this.mLocalDir = localDir;
        this.mLocalOldDir = localDir;
        this.mImageName = imageName;

        File fileDr = new File(localDir);
        if (!fileDr.exists()) {
            fileDr.mkdir();
        }
    }

    public String getUrl() {
        return mUrl;
    }

    public String getLocalDir() {
        return mLocalDir;
    }

    /**
     * set picture store directory
     * @param localDir new local directory
     */
    public void setLocalDir(String localDir) {
        this.mLocalDir = localDir;
        File fileDr = new File(localDir);
        if (!fileDr.exists()) {
            fileDr.mkdir();
        }
    }

    public String getLocalOldDir() {
        return mLocalOldDir;
    }

    public String getImageName() {
        return mImageName;
    }
}
