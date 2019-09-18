/*
 * ============================================================================
 * = COPYRIGHT
 *               PAX TECHNOLOGY, Inc. PROPRIETARY INFORMATION
 *    This software is supplied under the terms of a license agreement or
 *    nondisclosure agreement with PAX  Technology, Inc. and may not be copied
 *    or disclosed except in accordance with the terms in that agreement.
 *        Copyright (C) 2017 -? PAX Technology, Inc. All rights reserved.
 * Revision History:
 * Date	                     Author	                        Action
 * 17-11-28 上午11:31  	     fengjl           	    Create/Add/Modify/Delete
 * ============================================================================
 *
 */

package com.pax.order.settings;

import java.lang.ref.WeakReference;
import java.util.List;

import com.pax.order.FinancialApplication;
import com.pax.order.R;
import com.pax.order.util.ActivityStack;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class SettingsHeaderPrefActivity extends PreferenceActivity {
    private List<Header> mCopyHeaders;
    private static boolean sIsSinglePane = false;
    private WeakReference<SettingsHeaderPrefActivity> mWeakReference;

    private static class HeaderAdapter extends ArrayAdapter<Header> {
        private static class HeaderViewHolder {
            private ImageView mIcon;
            private TextView mTitle;
            private TextView mSummary;
            private ImageView mArrow;
        }

        private int mSelectItem = -1;
        private LayoutInflater mInflater;

        public void setSelectItem(int selectItem) {
            this.mSelectItem = selectItem;
        }

        HeaderAdapter(Context context, List<Header> objects) {
            super(context, 0, objects);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HeaderViewHolder holder;
            View view;

            if (convertView == null) {
                view = mInflater.inflate(R.layout.preference_header_item, parent, false);
                holder = new HeaderViewHolder();
                holder.mIcon = (ImageView) view.findViewById(R.id.icon);
                holder.mTitle = (TextView) view.findViewById(android.R.id.title);
                holder.mSummary = (TextView) view.findViewById(android.R.id.summary);
                holder.mArrow = (ImageView) view.findViewById(R.id.arrow);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (HeaderViewHolder) view.getTag();
            }

            // All view fields must be updated every time, because the view may
            // be recycled
            Header header = getItem(position);
            holder.mIcon.setImageResource(header.iconRes);
            holder.mTitle.setText(header.getTitle(getContext().getResources()));
            CharSequence summary = header.getSummary(getContext().getResources());
            if (!TextUtils.isEmpty(summary)) {
                holder.mSummary.setVisibility(View.VISIBLE);
                holder.mSummary.setText(summary);
            } else {
                holder.mSummary.setVisibility(View.GONE);
            }
            if (position == mSelectItem) {
          //      view.setBackgroundColor(getContext().getResources().getColor(R.color.textedit_hint));
                holder.mArrow.setVisibility(View.VISIBLE);
            } else {
                view.setBackgroundColor(Color.TRANSPARENT);
                if (!sIsSinglePane) {
                    holder.mArrow.setVisibility(View.INVISIBLE);
                }
            }
            return view;
        }
    }

    @Override
    public void onHeaderClick(Header header, int position) {
        super.onHeaderClick(header, position);
        mHeaderAdapter.setSelectItem(position);
    }

    @Override
    public void loadHeadersFromResource(int resid, List<Header> target) {
        super.loadHeadersFromResource(resid, target);
        mCopyHeaders = target;
    }

    private HeaderAdapter mHeaderAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        sIsSinglePane = onIsHidingHeaders() || !onIsMultiPane();
        mHeaderAdapter = new HeaderAdapter(this, mCopyHeaders);
        if (mCopyHeaders != null && mCopyHeaders.size() > 0) {
            setListAdapter(mHeaderAdapter);
            // D800默认获取焦点 A920则不设置焦点
            getListView().setFocusable(!sIsSinglePane);
            if (!sIsSinglePane) {
                mHeaderAdapter.setSelectItem(0);
            }
        }
        if (sIsSinglePane) {
            getListView().setDivider(getResources().getDrawable(R.drawable.list_divider));
            getListView().setDividerHeight(1);
            LayoutParams lp = (LayoutParams) getListView().getLayoutParams();
            lp.setMargins(0, 25, 0, 0);
            getListView().setLayoutParams(lp);
            ((ViewGroup) getListView().getParent()).setBackgroundColor(getResources().getColor(
                    R.color.settings_background_color));
            getListView().setBackgroundColor(getResources().getColor(R.color.settings_background_color));
            //左右间距为0
            getListView().setPadding(0, getListView().getPaddingTop(), 0, getListView().getPaddingBottom());
        }

        mWeakReference = new WeakReference<>(this);
        ActivityStack.getInstance().push(mWeakReference.get());
    }

    @Override
    protected void onDestroy() {
//        FinancialApplication.removeActivity(this);
        super.onDestroy();
    }

    public static boolean isIsSinglePane() {
        return sIsSinglePane;
    }

    @Override
    public boolean onIsMultiPane() {
        return false;
    }

    /**
     * 浸入式状态栏实现同时取消5.0以上的阴影
     */
    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {//5.0及以上
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
        }
    }
}
