/*
 * Copyright 2014 Green Wang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.greenwang.ExpandableTextView;

import com.greenwang.ExpandableTextView.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ExpandableTextView extends LinearLayout implements OnClickListener {
    private static final String TAG = ExpandableTextView.class.getSimpleName();

    // The default number of lines;
    private static final int MAX_COLLAPSED_LINES = 8;

    protected TextView mTextView;

    protected TextView mCollapseBtn; // Button to expand/collapse

    private boolean mRelayout;

    private boolean mCollapsed = true; // Show short version as default.

    private boolean mEnable = true; // Enable collapse.

    private int mMaxCollapsedLines;

    private int mTextColor;

    private float mTextSize;

    private int mCollapseColor;

    private float mCollapseSize;

    private String mText;

    private String mCollapseText;

    private String mExpandText;

    private CollapseListener mListener;

    public interface CollapseListener {
        void onExpandClicked(View v);
    }

    public ExpandableTextView(Context context) {
        this(context, null, 0);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public void setCollapseListener(CollapseListener listener) {
        mListener = listener;
    }

    public void setExpandEnable(boolean enable) {
        mEnable = enable;
        if (!mEnable) {
            setCollapse(false);
        } else {
            requestLayout();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() != R.id.expand_collapse) {
            return;
        }

        if (mListener != null) {
            mListener.onExpandClicked(view);
            return;
        }

        setCollapse(!mCollapsed);
    }

    public boolean isCollapsed() {
        return mCollapsed;
    }

    public void setCollapse(boolean collapse) {
        if (mCollapsed == collapse) {
            return;
        }
        mRelayout = true;
        mCollapsed = collapse;
        if (mCollapseBtn == null) {
            findViews();
        }
        mCollapseBtn.setText(mCollapsed ? mExpandText : mCollapseText);
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // If no change, measure and return
        if (!mRelayout || getVisibility() == View.GONE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        mRelayout = false;

        // Setup with optimistic case
        // i.e. Everything fits. No button needed
        mCollapseBtn.setVisibility(View.GONE);
        mTextView.setMaxLines(Integer.MAX_VALUE);

        // Measure
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // If the text fits in collapsed mode, we are done.
        if (mTextView.getLineCount() <= mMaxCollapsedLines || !mEnable) {
            return;
        }

        // Doesn't fit in collapsed mode. Collapse text view as needed. Show
        // button.
        if (mCollapsed) {
            mTextView.setMaxLines(mMaxCollapsedLines);
        }
        mCollapseBtn.setVisibility(View.VISIBLE);

        // Re-measure with new setup
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray =
                getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        mMaxCollapsedLines =
                typedArray.getInt(R.styleable.ExpandableTextView_maxCollapsedLines,
                        MAX_COLLAPSED_LINES);
        mText = typedArray.getString(R.styleable.ExpandableTextView_text);
        mExpandText = typedArray.getString(R.styleable.ExpandableTextView_expandText);
        mCollapseText = typedArray.getString(R.styleable.ExpandableTextView_collapseText);

        mTextColor = typedArray.getColor(R.styleable.ExpandableTextView_textColor, 0xff0000);
        mCollapseColor =
                typedArray.getColor(R.styleable.ExpandableTextView_collapseColor, 0xff0000);

        mTextSize = typedArray.getDimension(R.styleable.ExpandableTextView_textSize, 24);
        mCollapseSize = typedArray.getDimension(R.styleable.ExpandableTextView_collapseSize, 21);

        typedArray.recycle();
    }

    private void findViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.expandable_textview, this, true);
        mTextView = (TextView) findViewById(R.id.expandable_text);
        // mTextView.setTextColor(mTextColor);
        // mTextView.setTextSize(mTextSize);
        mTextView.setText(mText);

        mCollapseBtn = (TextView) findViewById(R.id.expand_collapse);
        // mCollapseBtn.setTextColor(mCollapseColor);
        // mCollapseBtn.setTextSize(mCollapseSize);
        mCollapseBtn.setText(mCollapsed ? mExpandText : mCollapseText);
        mCollapseBtn.setOnClickListener(this);
    }

    public void setText(String text, boolean collapse) {
        setText(text);
        setCollapse(collapse);
    }

    public void setText(String text) {
        mRelayout = true;
        if (mTextView == null) {
            findViews();
        }
        String trimmedText = text.trim();
        mTextView.setText(trimmedText);
        setVisibility(trimmedText.length() == 0 ? View.GONE : View.VISIBLE);
    }

    public CharSequence getText() {
        if (mTextView == null) {
            return "";
        }
        return mTextView.getText();
    }

}
