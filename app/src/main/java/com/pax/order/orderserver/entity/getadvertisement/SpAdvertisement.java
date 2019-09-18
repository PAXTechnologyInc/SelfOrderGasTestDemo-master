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
package com.pax.order.orderserver.entity.getadvertisement;

import java.util.ArrayList;
import java.util.List;

/**
 * Sound Payment's advertisement response
 */
public class SpAdvertisement {
    private String AdvertisementId;
    private List<Integer> ItemIds;
    private String PictureUrl;
    private String Description;
    private String ExtData;

    public SpAdvertisement() {
        AdvertisementId = "";
        ItemIds = new ArrayList<>();
        PictureUrl = "";
        Description = "";
        ExtData = "";
    }

    public String getAdvertisementId() {
        return AdvertisementId;
    }

    public void setAdvertisementId(String advertisementId) {
        AdvertisementId = advertisementId;
    }

    public List<Integer> getItemIds() {
        return ItemIds;
    }

    public void setItemIds(List<Integer> itemIds) {
        ItemIds = itemIds;
    }

    public String getPictureUrl() {
        return PictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        PictureUrl = pictureUrl;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getExtData() {
        return ExtData;
    }

    public void setExtData(String extData) {
        ExtData = extData;
    }
}
