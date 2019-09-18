package q.rorbin.verticaltablayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Px;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import q.rorbin.badgeview.Badge;
import q.rorbin.verticaltablayout.R;
import q.rorbin.verticaltablayout.util.DisplayUtil;

public class QTabView extends TabView {
    private Context mContext;
    private TextView mTitle;
    private Badge mBadgeView;
    private TabIcon mTabIcon;
    private TabTitle mTabTitle;
    private TabBadge mTabBadge;
    private boolean mChecked;
    private Drawable mDefaultBackground;

    private QTabView.TabViewContainer mContainer;
    private ImageView mIcon;
    private int mMinHeight;

    public QTabView(Context context) {
        super(context);
        mContext = context;
        mMinHeight = DisplayUtil.dp2px(context, 30.0F);
        mTabIcon = new TabIcon.Builder().build();
        mTabTitle = new TabTitle.Builder().build();
        mTabBadge = new TabBadge.Builder().build();
        initView();
        int[] attrs;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            attrs = new int[]{android.R.attr.selectableItemBackgroundBorderless};
        } else {
            attrs = new int[]{android.R.attr.selectableItemBackground};
        }
        TypedArray a = mContext.getTheme().obtainStyledAttributes(attrs);
        mDefaultBackground = a.getDrawable(0);
        a.recycle();
        setDefaultBackground();
    }

    private void initView() {
        this.initContainer();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        this.addView(this.mContainer, params);

        initTitleView();
        initIconView();
        initBadge();
    }

    private void initContainer() {
        this.mContainer = new QTabView.TabViewContainer(this.mContext);
        this.mContainer.setOrientation(LinearLayout.HORIZONTAL);
        this.mContainer.setMinimumHeight(this.mMinHeight);
        this.mContainer.setPadding(DisplayUtil.dp2px(this.mContext, 5.0F), DisplayUtil.dp2px(this.mContext, 5.0F), DisplayUtil.dp2px(this.mContext, 5.0F), DisplayUtil.dp2px(this.mContext, 5.0F));
        this.mContainer.setGravity(17);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void setPaddingRelative(@Px int start, @Px int top, @Px int end, @Px int bottom) {
        mTitle.setPaddingRelative(start, top, end, bottom);
    }

    @Override
    public void setPadding(@Px int left, @Px int top, @Px int right, @Px int bottom) {
        mTitle.setPadding(left, top, right, bottom);
    }

    private void initBadge() {
        mBadgeView = TabBadgeView.bindTab(this);
        if (mTabBadge.getBackgroundColor() != 0xFFE84E40) {
            mBadgeView.setBadgeBackgroundColor(mTabBadge.getBackgroundColor());
        }
        if (mTabBadge.getBadgeTextColor() != 0xFFFFFFFF) {
            mBadgeView.setBadgeTextColor(mTabBadge.getBadgeTextColor());
        }
        if (mTabBadge.getStrokeColor() != Color.TRANSPARENT || mTabBadge.getStrokeWidth() != 0) {
            mBadgeView.stroke(mTabBadge.getStrokeColor(), mTabBadge.getStrokeWidth(), true);
        }
        if (mTabBadge.getDrawableBackground() != null || mTabBadge.isDrawableBackgroundClip()) {
            mBadgeView.setBadgeBackground(mTabBadge.getDrawableBackground(), mTabBadge.isDrawableBackgroundClip());
        }
        if (mTabBadge.getBadgeTextSize() != 11) {
            mBadgeView.setBadgeTextSize(mTabBadge.getBadgeTextSize(), true);
        }
        if (mTabBadge.getBadgePadding() != 5) {
            mBadgeView.setBadgePadding(mTabBadge.getBadgePadding(), true);
        }
        if (mTabBadge.getBadgeNumber() != 0) {
            mBadgeView.setBadgeNumber(mTabBadge.getBadgeNumber());
        }
        if (mTabBadge.getBadgeText() != null) {
            mBadgeView.setBadgeText(mTabBadge.getBadgeText());
        }
        if (mTabBadge.getBadgeGravity() != (Gravity.END | Gravity.TOP)) {
            mBadgeView.setBadgeGravity(mTabBadge.getBadgeGravity());
        }
        if (mTabBadge.getGravityOffsetX() != 5 || mTabBadge.getGravityOffsetY() != 5) {
            mBadgeView.setGravityOffset(mTabBadge.getGravityOffsetX(), mTabBadge.getGravityOffsetY(), true);
        }
        if (mTabBadge.isExactMode()) {
            mBadgeView.setExactMode(mTabBadge.isExactMode());
        }
        if (!mTabBadge.isShowShadow()) {
            mBadgeView.setShowShadow(mTabBadge.isShowShadow());
        }
        if (mTabBadge.getOnDragStateChangedListener() != null) {
            mBadgeView.setOnDragStateChangedListener(mTabBadge.getOnDragStateChangedListener());
        }
    }

    private void initTitleView() {
        if (this.mTitle != null) {
            this.mContainer.removeView(this.mTitle);
        }
        this.mTitle = new TextView(this.mContext);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        this.mTitle.setLayoutParams(params);
        this.mTitle.setTextColor(this.mTabTitle.getColorNormal());
        this.mTitle.setTextSize((float) this.mTabTitle.getTitleTextSize());
        this.mTitle.getPaint().setFakeBoldText(this.mTabTitle.getTitleTextBold());
        this.mTitle.setText(this.mTabTitle.getContent());
        this.mTitle.setGravity(Gravity.CENTER);
        this.mTitle.setEllipsize(TextUtils.TruncateAt.END);
        this.requestContainerLayout(this.mTabIcon.getIconGravity());
    }

    private void initIconView() {

        if (this.mIcon != null) {
            this.mContainer.removeView(this.mIcon);
        }

        this.mIcon = new ImageView(this.mContext);
        LayoutParams params = new LayoutParams(this.mTabIcon.getIconWidth(), this.mTabIcon.getIconHeight());
        this.mIcon.setLayoutParams(params);
        if (this.mTabIcon.getNormalIcon() != 0) {
            this.mIcon.setImageResource(this.mTabIcon.getNormalIcon());
        } else if (this.mTabIcon.getNormalIconBitmap() != null) {
            this.mIcon.setImageBitmap(this.mTabIcon.getNormalIconBitmap());
        } else if (this.mTabIcon.getNormalIconUrl() != null) {
            this.mIcon.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(this.mTabIcon.getSelectedIconUrl())
                    .placeholder(R.drawable.icon_default)
                    .error(R.drawable.icon_default)
                    .centerCrop()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(this.mIcon);
        } else {
            this.mIcon.setVisibility(View.GONE);
        }

        this.requestContainerLayout(this.mTabIcon.getIconGravity());
    }

    private void requestContainerLayout(int gravity) {
        this.mContainer.removeAllViews();
        android.widget.LinearLayout.LayoutParams lp;
        switch (gravity) {
            case Gravity.TOP:
                this.mContainer.setOrientation(LinearLayout.VERTICAL);
                if (this.mIcon != null) {
                    this.mContainer.addView(this.mIcon);
                    lp = (android.widget.LinearLayout.LayoutParams) this.mIcon.getLayoutParams();
                    lp.setMargins(0, 0, 0, this.mTabIcon.getMargin());
                    this.mIcon.setLayoutParams(lp);
                }

                if (this.mTitle != null) {
                    this.mContainer.addView(this.mTitle);
                }
                break;

            case Gravity.BOTTOM:
                this.mContainer.setOrientation(LinearLayout.VERTICAL);
                if (this.mTitle != null) {
                    this.mContainer.addView(this.mTitle);
                }

                if (this.mIcon != null) {
                    this.mContainer.addView(this.mIcon);
                    lp = (android.widget.LinearLayout.LayoutParams) this.mIcon.getLayoutParams();
                    lp.setMargins(0, this.mTabIcon.getMargin(), 0, 0);
                    this.mIcon.setLayoutParams(lp);
                }
                break;

            case Gravity.START:
                this.mContainer.setOrientation(LinearLayout.HORIZONTAL);
                if (this.mIcon != null) {
                    this.mContainer.addView(this.mIcon);
                    lp = (android.widget.LinearLayout.LayoutParams) this.mIcon.getLayoutParams();
                    lp.setMargins(0, 0, this.mTabIcon.getMargin(), 0);
                    this.mIcon.setLayoutParams(lp);
                }

                if (this.mTitle != null) {
                    this.mContainer.addView(this.mTitle);
                }
                break;

            case Gravity.END:
                this.mContainer.setOrientation(LinearLayout.HORIZONTAL);
                if (this.mTitle != null) {
                    this.mContainer.addView(this.mTitle);
                }

                if (this.mIcon != null) {
                    this.mContainer.addView(this.mIcon);
                    lp = (android.widget.LinearLayout.LayoutParams) this.mIcon.getLayoutParams();
                    lp.setMargins(this.mTabIcon.getMargin(), 0, 0, 0);
                    this.mIcon.setLayoutParams(lp);
                }
        }

    }

    @Override
    public QTabView setBadge(TabBadge badge) {
        if (badge != null) {
            mTabBadge = badge;
        }
        initBadge();
        return this;
    }

    @Override
    public QTabView setIcon(TabIcon icon) {
        if (icon != null) {
            mTabIcon = icon;
        }
        initIconView();
        return this;
    }

    @Override
    public QTabView setTitle(TabTitle title) {
        if (title != null) {
            mTabTitle = title;
        }
        initTitleView();
        return this;
    }

    /**
     * @param resId The Drawable res to use as the background, if less than 0 will to remove the
     *              background
     */
    @Override
    public QTabView setBackground(int resId) {
        if (resId == 0) {
            setDefaultBackground();
        } else if (resId <= 0) {
            setBackground(null);
        } else {
            super.setBackgroundResource(resId);
        }
        return this;
    }

    @Override
    public TabBadge getBadge() {
        return mTabBadge;
    }

    @Override
    public TabIcon getIcon() {
        return mTabIcon;
    }

    @Override
    public TabTitle getTitle() {
        return mTabTitle;
    }

    @Override
    @Deprecated
    public ImageView getIconView() {
        return null;
    }

    @Override
    public TextView getTitleView() {
        return mTitle;
    }

    @Override
    public Badge getBadgeView() {
        return mBadgeView;
    }

    @Override
    public void setBackground(Drawable background) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            super.setBackground(background);
        } else {
            super.setBackgroundDrawable(background);
        }
    }

    @Override
    public void setBackgroundResource(int resid) {
        setBackground(resid);
    }

    private void setDefaultBackground() {
        if (getBackground() != mDefaultBackground) {
            setBackground(mDefaultBackground);
        }
    }

    @Override
    public void setChecked(boolean checked) {
        this.mChecked = checked;
        this.setSelected(checked);
        this.refreshDrawableState();
        if (this.mChecked) {
            this.mTitle.setTextColor(this.mTabTitle.getColorSelected());
            if (this.mTabIcon.getSelectedIcon() != 0) {
                this.mIcon.setVisibility(View.VISIBLE);
                this.mIcon.setImageResource(this.mTabIcon.getSelectedIcon());
            } else if (this.mTabIcon.getSelectedIconBitmap() != null) {
                this.mIcon.setVisibility(View.VISIBLE);
                this.mIcon.setImageBitmap(this.mTabIcon.getSelectedIconBitmap());
            } else if (this.mTabIcon.getSelectedIconUrl() != null) {
                this.mIcon.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(this.mTabIcon.getSelectedIconUrl())
                        .placeholder(R.drawable.icon_default)
                        .error(R.drawable.icon_default)
                        .centerCrop()
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(this.mIcon);
            } else {
                this.mIcon.setVisibility(View.GONE);
            }
        } else {
            this.mTitle.setTextColor(this.mTabTitle.getColorNormal());
            if (this.mTabIcon.getNormalIcon() != 0) {
                this.mIcon.setVisibility(View.VISIBLE);
                this.mIcon.setImageResource(this.mTabIcon.getNormalIcon());
            } else if (this.mTabIcon.getNormalIconBitmap() != null) {
                this.mIcon.setVisibility(View.VISIBLE);
                this.mIcon.setImageBitmap(this.mTabIcon.getNormalIconBitmap());
            } else if (this.mTabIcon.getNormalIconUrl() != null) {
                this.mIcon.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(this.mTabIcon.getNormalIconUrl())
                        .placeholder(R.drawable.icon_default)
                        .error(R.drawable.icon_default)
                        .centerCrop()
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(this.mIcon);

            } else {
                this.mIcon.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    public void setOnClickListener(final OnClickListener l) {
        this.mContainer.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                l.onClick(QTabView.this);
            }
        });
    }

    private static class TabViewContainer extends LinearLayout {
        public TabViewContainer(Context context) {
            super(context);
        }
    }

}