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
 * 18-11-9 上午9:37           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 */

package com.pax.order;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.pax.order.adver.GuideActivity;
import com.pax.order.menu.MenuActivity;
import com.pax.order.util.BaseActivity;
import com.pax.order.util.BasePresenter;
import com.pax.order.util.IView;

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        System.out.println("SplashActivity OnCreate called");
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            String[] permissionList = new String[]{android.Manifest.permission
                    .READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(SplashActivity.this, permissionList, 1);
        } else {
            call();
        }
    }

    @Override
    protected BasePresenter<IView> createPresenter() {
        return null;
    }

    private void call() {
        System.out.println("SplashActivity starting GuideActivity");
//        startActivity(GuideActivity.class);
        startActivity(MenuActivity.class);
        finish();
        System.out.println("Ending SplashActivity Call");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    //ToastUtils.showMessage(this,"you denied the permission!");
                    FinancialApplication.finishAll();
                }
                break;
            default:
        }
    }
}
