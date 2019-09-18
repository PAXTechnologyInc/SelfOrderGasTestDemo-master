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
 * 2018/10/10 	         zenglc           	Create/Add/Modify/Delete
 * ============================================================================
 */
package com.pax.order.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pax.order.R;


public class ADView extends RelativeLayout {
    private static final String TAG = "ADView";

    private ImageView mImageView;
    private TextView mTextView;
    private Animation mRotateAnimation;

    public ADView(Context context) {
        super(context);

        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.ad_layout, this);

        // 获取控件
        mImageView = (ImageView) findViewById(R.id.ad_img);
        mTextView = (TextView) findViewById(R.id.ad_tips);

        mRotateAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_circle_rotate);
        mRotateAnimation.setInterpolator(new LinearInterpolator());
    }

    public ImageView getImageView() {
        return mImageView;
    }

    public TextView getTextView() {
        return mTextView;
    }

    public void startAnimation() {
        mImageView.startAnimation(mRotateAnimation);
    }

    public void stopAnimation() {
        mImageView.clearAnimation();
    }

}
