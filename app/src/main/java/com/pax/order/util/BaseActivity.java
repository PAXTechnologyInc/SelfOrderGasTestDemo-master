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
 * 2018/8/7 	         zenglc           	Create/Add/Modify/Delete
 * ============================================================================
 */
package com.pax.order.util;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.pax.order.FinancialApplication;

import java.lang.ref.WeakReference;

/**
 * Activity 基类
 **/

public abstract class BaseActivity extends FragmentActivity {
    private WeakReference<BaseActivity> mWeakReference;
    protected BasePresenter<IView> mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        initStatusBar();
        mWeakReference = new WeakReference<>(this);
        ActivityStack.getInstance().push(mWeakReference.get());

        //创建Presenter
        mPresenter = createPresenter();
        //关联View
        if (mPresenter != null) {
            mPresenter.attachView((IView) this);
        }
    }

    private void hideNavigationBar() {
        if (FinancialApplication.getDal().getSys().isNavigationBarVisible()) {
            FinancialApplication.getDal().getSys().showNavigationBar(false);
        }

        if (FinancialApplication.getDal().getSys().isStatusBarEnabled()) {
            FinancialApplication.getDal().getSys().enableStatusBar(false);
        }

        if (FinancialApplication.getDal().getSys().isNavigationBarEnabled()) {
            FinancialApplication.getDal().getSys().enableNavigationBar(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigationBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //解除关联
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    protected abstract BasePresenter<IView> createPresenter();

    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    public void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }

    /**
     * 携带数据的页面跳转
     *
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 含有Bundle通过Class打开编辑界面
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 浸入式状态栏实现同时取消5.0以上的阴影
     */
    protected void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {//5.0及以上
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        }
    }
}
