package com.pax.order.util;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 * 2018/8/1 	        wanglz           	Create/Add/Modify/Delete
 * ============================================================================
 */
public abstract class BaseFragment extends Fragment {
    public Activity mActivity;
    protected BasePresenter<IView> mPresenter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getViewID(), null);
        initView(view);
        bindEvent();

        //创建Presenter
        mPresenter = createPresenter();
        //关联View
        if (mPresenter != null) {
            mPresenter.attachView((IView) this);
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除关联
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    protected abstract int getViewID();

    protected abstract void initView(View view);

    protected abstract void bindEvent();

    protected abstract BasePresenter<IView> createPresenter();

}
