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
 * 19-2-18 下午5:06           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 */

package com.pax.order.adver;

import com.pax.order.entity.Advertisement;
import com.pax.order.util.IPresenter;
import com.pax.order.util.IView;

import java.util.List;

public interface GuideContract {
    interface View extends IView<Presenter> {
        void dispAdver(List<String> adverUrlList);
    }

    interface Presenter extends IPresenter {
        void setAdverisement(List<Advertisement> advertisementList);

        List<String> getAdverUrl();
    }
}
