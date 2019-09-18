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

import com.pax.order.orderserver.entity.baseModel.BaseRspModel;
import java.util.ArrayList;
import java.util.List;

/**
 * get advertisement reponse
 */
public class GetAdvertisementRsp extends BaseRspModel {
    private List<SpAdvertisement> Advertisements;
    private String ExtDataList;

    public GetAdvertisementRsp() {
        super();
        Advertisements = new ArrayList<>();
        ExtDataList = "";
    }

    public List<SpAdvertisement> getSpAdvertisements() {
        return Advertisements;
    }

    public void setSpAdvertisements(List<SpAdvertisement> spAdvertisements) {
        this.Advertisements = spAdvertisements;
    }

    public String getExtDataList() {
        return ExtDataList;
    }

    public void setExtDataList(String extDataList) {
        ExtDataList = extDataList;
    }
}
