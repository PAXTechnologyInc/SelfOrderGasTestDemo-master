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

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.pax.order.R;

import java.util.List;

public class FragmentManagerUtils {
    private FragmentManager mManager;
    private int mContainerResid;
    private List<Fragment> mFragments;
    private int mPosition = 0;

    public FragmentManagerUtils(FragmentManager manager, List<Fragment> fragments) {
        this.mManager = manager;
        this.mFragments = fragments;
    }

    public FragmentManagerUtils(FragmentManager manager, int containerResid, List<Fragment> fragments) {
        this(manager, fragments);
        this.mContainerResid = containerResid;
        changeFragment(0, false);
    }

    public void changeFragment(int position, boolean animations) {
        FragmentTransaction ft = mManager.beginTransaction();

        mPosition = position;
        List<Fragment> addedFragments = mManager.getFragments();
        for (int i = 0; i < mFragments.size(); i++) {
            Fragment fragment = mFragments.get(i);
            if ((addedFragments == null || !addedFragments.contains(fragment)) && mContainerResid != 0) {
                ft.add(mContainerResid, fragment);
            }
            if ((mFragments.size() > position && i == position)
                    || (mFragments.size() <= position && i == mFragments.size() - 1)) {
                if(animations) {
                    ft.setCustomAnimations(R.anim.scale_in, R.anim.scale_out);
                }
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
        }

        //        Fragment fragment = mFragments.get(position);
        //        ft.replace(mContainerResid, fragment);

//        ft.commit();
        ft.commitAllowingStateLoss();
        mManager.executePendingTransactions();
    }

    public void detach() {
        FragmentTransaction ft = mManager.beginTransaction();
        for (Fragment fragment : mFragments) {
            ft.remove(fragment);
        }
//        ft.commit();
        ft.commitAllowingStateLoss();
        mManager.executePendingTransactions();
        mManager = null;
        mFragments = null;
    }

    public int getFragmentPosition() {
        return mPosition;
    }
}

