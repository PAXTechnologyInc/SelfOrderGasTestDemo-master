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
 * 2018/8/29 	         zenglc           	Create/Add/Modify/Delete
 * ============================================================================
 */
package com.pax.order.util;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int leftRight;
    private int topBottom;

    public SpaceItemDecoration(int leftRight, int topBottom) {
        this.leftRight = leftRight;
        this.topBottom = topBottom;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
//        //竖直方向的
//        if (layoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
//            //最后一项需要 bottom
//            if (parent.getChildAdapterPosition(view) == layoutManager.getItemCount() - 1) {
//                outRect.bottom = topBottom;
//            }
//            outRect.top = topBottom;
//            outRect.left = leftRight;
//            outRect.right = leftRight;
//        } else {
//            //最后一项需要right
//            if (parent.getChildAdapterPosition(view) == layoutManager.getItemCount() - 1) {
//                outRect.right = leftRight;
//            }
//            outRect.top = topBottom;
//            outRect.left = leftRight;
//            outRect.bottom = topBottom;
//        }

        outRect.left = leftRight;
        outRect.bottom = topBottom;
        if (parent.getChildLayoutPosition(view) % 2 == 0) {
            outRect.left = 0;
        }
    }

}