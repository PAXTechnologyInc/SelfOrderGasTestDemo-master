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

import com.pax.order.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


public class SettingsEditTextPreference extends SettingsCustomPreference {
    private static int sPositionOfPrefActHeader = 0;
    private EditText mEdittext;
    private String mText;
    private Context mContext;
    private int mMaxLength;
    private int mMaxLine;
    private int mInputType;
    private int mEms;
    private int mMinEms;
    private int mMaxEms;
    private boolean mSingLine;
    private boolean mSelectAllOnFocus;
    private Object mDefaultValue;
    private String mHint;
    private String mDigits;

    public SettingsEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyDialogPreference);
        mMaxLength = typedArray.getInt(R.styleable.MyDialogPreference_maxLength, 20); // 设置最大多少长度
        mMaxLine = typedArray.getInt(R.styleable.MyDialogPreference_maxLines, 1); // 设置最大行数
        mSingLine = typedArray.getBoolean(R.styleable.MyDialogPreference_singLine, false); // 设置是否单行
        mDigits = typedArray.getString(R.styleable.MyDialogPreference_digitsValue);
        mEms = typedArray.getInt(R.styleable.MyDialogPreference_ems, -1);
        mMaxEms = typedArray.getInt(R.styleable.MyDialogPreference_maxEms, -1);
        mMinEms = typedArray.getInt(R.styleable.MyDialogPreference_minEms, -1);
        mInputType = typedArray.getInt(R.styleable.MyDialogPreference_inputType, EditorInfo.TYPE_CLASS_TEXT);
        mSelectAllOnFocus = typedArray.getBoolean(R.styleable.MyDialogPreference_selectAllOnFocus, false);
        mHint = typedArray.getString(R.styleable.MyDialogPreference_hint);
        mDefaultValue = typedArray.getString(R.styleable.MyDialogPreference_defaultValue);
        if (mDefaultValue != null) {
            setDefaultValue(mDefaultValue);
        }
        // 使用完后回收
        typedArray.recycle();
    }

    public SettingsEditTextPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingsEditTextPreference(Context context) {
        this(context, null);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        SharedPreferences pref = getSharedPreferences();
        mEdittext.setText(pref.getString(getKey(), ""));
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            String value = mEdittext.getText().toString();
            if (callChangeListener(value)) {
                setText(value);
            }
        }
    }

    @Override
    public void findViewsInDialogById(View view) {
        mEdittext = (EditText) view.findViewById(R.id.edit_tv);
        mEdittext.setMaxLines(mMaxLine);
        mEdittext.setFilters(new InputFilter[] {new InputFilter.LengthFilter(mMaxLength)}); // 最大输入长度
        mEdittext.setSingleLine(mSingLine);
        if (!TextUtils.isEmpty(mHint)) {
            mEdittext.setHint(mHint);
        }
        mEdittext.setInputType(mInputType);
        if (!TextUtils.isEmpty(mDigits)) {
            mEdittext.setKeyListener(new NumberKeyListener() {
                @Override
                public int getInputType() {
                    return mInputType;
                }
                @Override
                protected char[] getAcceptedChars() {
                    return mDigits.trim().toCharArray();
                }
            });
        }
        mEdittext.setSelectAllOnFocus(mSelectAllOnFocus);
        mEdittext.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager) mContext
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    onDialogClosed(true);
                    onActivityDestroy();
                    ((SettingsParamActivity) mContext).setSelection(sPositionOfPrefActHeader);
                }

                return false;
            }
        });

        if (mEms > 0) {
            mEdittext.setEms(mEms);
        }
        if (mMaxEms > 0) {
            mEdittext.setMaxEms(mMaxEms);
        }
        if (mMinEms > 0) {
            mEdittext.setMinEms(mMinEms);
        }
        // mText = edittext.getText().toString();
        mText = PreferenceManager.getDefaultSharedPreferences(this.getContext()).getString(this.getKey(), null);

    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        setText(restorePersistedValue ? getPersistedString(mText) : (String) defaultValue);
    }

    @Override
    public int setLayoutInDialog() {
        return R.layout.settings_dialog_layout;
    }

    /**
     * Gets the text from the {@link SharedPreferences}.
     * 
     * @return The current preference value.
     */
    public String getText() {
        return mText;
    }

    public void setText(String text) {
        final boolean wasBlocking = shouldDisableDependents();

        mText = text;

        persistString(text);

        final boolean isBlocking = shouldDisableDependents();
        if (isBlocking != wasBlocking) {
            notifyDependencyChange(isBlocking);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            // No need to save instance state since it's persistent
            return superState;
        }

        final SavedState myState = new SavedState(superState);
        myState.mText = getText();
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        setText(myState.mText);
    }

    private static class SavedState extends Preference.BaseSavedState {
        private String mText;

        public SavedState(Parcel source) {
            super(source);
            mText = source.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(mText);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public static int getPositionOfPrefActHeader() {
        return sPositionOfPrefActHeader;
    }

    public static void setPositionOfPrefActHeader(int positionOfPrefActHeader) {
        sPositionOfPrefActHeader = positionOfPrefActHeader;
    }
}
