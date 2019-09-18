package com.pax.order.entity;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.List;

/*
 * ============================================================================
 * = COPYRIGHT
 *               PAX TECHNOLOGY, Inc. PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or
 *   nondisclosure agreement with PAX  Technology, Inc. and may not be copied
 *   or disclosed except in accordance with the terms in that agreement.
 *      Copyright (C) 2000-2018 PAX Technology, Inc. All rights reserved.
 * mDescription: // Detail description about the function of this module,
 *             // interfaces with the other modules, and dependencies.
 * Revision History:
 * Date	                 Author	                Action
 * 2018/8/7 	        wanglz           	Create/Add/Modify/Delete
 * ============================================================================
 */

/**
 * Advertisement data class
 */
public class Advertisement {
    /**
     * associated items
     */
    private List<Integer> mItemIds;
    private String mPictureUrl;
    private String mDescription;
    private String mExtData;
    private String mPicLocalFullpath;
    private Bitmap mBitmap;
    private ImageView mImageView;

    public String getPicLocalFullpath() {
        return mPicLocalFullpath;
    }

    /**
     * setPicLocalFullpath
     * @param picLocalFullpath advertisement picture store path
     */
    public void setPicLocalFullpath(String picLocalFullpath) {
        mPicLocalFullpath = picLocalFullpath;
    }

    public List<Integer> getItemIds() {
        return mItemIds;
    }

    public void setItemIds(List<Integer> itemIds) {
        mItemIds = itemIds;
    }

    public String getPictureUrl() {
        return mPictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        mPictureUrl = pictureUrl;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getExtData() {
        return mExtData;
    }

    public void setExtData(String extData) {
        mExtData = extData;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public ImageView getImageView() {
        return mImageView;
    }

    public void setImageView(ImageView imageView) {
        mImageView = imageView;
    }
}
