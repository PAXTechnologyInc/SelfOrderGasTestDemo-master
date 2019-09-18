package com.pax.order.util;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.pax.order.entity.ProcessMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
 * 2018/9/7 	        wanglz           	Create/Add/Modify/Delete
 * ============================================================================
 */
public abstract class MsgProActivity extends BaseActivity {
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ProcessMessage processMessage) {
        dealProcessMsg(processMessage);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("OnCreate MsgProActivity called");
        EventBus.getDefault().register(this);
    }

    public abstract void dealProcessMsg(ProcessMessage processMessage);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

