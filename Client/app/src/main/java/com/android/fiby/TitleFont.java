package com.android.fiby;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Goft on 10/09/2017.
 */



public class TitleFont extends android.support.v7.widget.AppCompatTextView {

    public TitleFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TitleFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitleFont(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSansHebrew-Light.ttf");
        setTypeface(tf ,1);
    }
}