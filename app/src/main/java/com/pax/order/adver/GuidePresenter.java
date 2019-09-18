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
 * 19-2-18 下午5:04           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 */

package com.pax.order.adver;

import android.support.annotation.NonNull;

import com.pax.order.entity.AdverItemData;
import com.pax.order.entity.Advertisement;
import com.pax.order.util.BasePresenter;
import com.pax.order.util.IView;

import java.util.ArrayList;
import java.util.List;


public class GuidePresenter extends BasePresenter<IView> implements GuideContract.Presenter {
    private final GuideContract.View mGuideView;
    private List<String> adverUrlList = new ArrayList<>();


    public GuidePresenter(@NonNull GuideContract.View guideView) {
        System.out.println("GuidePresented Constructor");
        mGuideView = guideView;
        mGuideView.setPresenter(this);
        System.out.println("GuidePresented Constructor Set");
    }

    private void getAdverData(List<Advertisement> advertisementList) {
        System.out.println("Getting AdvertData");
        if ((advertisementList != null)
                && (advertisementList.size() != 0)) {
            adverUrlList.clear();
            for (Advertisement advertisement : advertisementList) {
                adverUrlList.add(advertisement.getPictureUrl());
            }
        }
    }

    @Override
    public void start() {
        System.out.println("Guide Presenter started.");
        if ((AdverItemData.getAdvertisementList() != null)
                && (AdverItemData.getAdvertisementList().size() != 0)) {
            getAdverData(AdverItemData.getAdvertisementList());
            mGuideView.dispAdver(adverUrlList);
        }
    }

    @Override
    public void setAdverisement(List<Advertisement> advertisementList) {

        boolean isUpdate = false;
        if ((AdverItemData.getAdvertisementList() != null)
                && (AdverItemData.getAdvertisementList().size() != 0)) {
            if (AdverItemData.getAdvertisementList().size() == advertisementList.size()) {
                for (Advertisement advertisement : advertisementList) {
                    for (int i = 0; i < AdverItemData.getAdvertisementList().size(); i++) {
                        if (advertisement.getPictureUrl().equals(AdverItemData.getAdvertisementList().get(i).getPictureUrl())) {
                            break;
                        } else if (i == AdverItemData.getAdvertisementList().size() - 1) {
                            //未找到一致的则更新adver
                            AdverItemData.setAdvertisementList(advertisementList);
                            getAdverData(advertisementList);
                            mGuideView.dispAdver(adverUrlList);
                            isUpdate = true;
                            break;
                        }
                    }
                    if (isUpdate) {
                        break;
                    }
                }

            } else {
                AdverItemData.setAdvertisementList(advertisementList);
                getAdverData(advertisementList);
                mGuideView.dispAdver(adverUrlList);
            }
        } else {
            AdverItemData.setAdvertisementList(advertisementList);
            getAdverData(advertisementList);
            mGuideView.dispAdver(adverUrlList);
        }
    }


    @Override
    public List<String> getAdverUrl() {
        return adverUrlList;
    }

}
