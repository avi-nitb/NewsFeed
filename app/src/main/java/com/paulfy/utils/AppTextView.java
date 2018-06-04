package com.paulfy.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

import com.paulfy.R;

public class AppTextView extends android.support.v7.widget.AppCompatTextView {

    public Paint paint;
    public boolean addStrike = false;

    public AppTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) init(attrs);
    }

    public AppTextView(Context context) {
        super(context);
    }

    private void init(AttributeSet attrs) {
//        TypedArray attrsArray = getContext().obtainStyledAttributes(attrs, R.styleable.apptext);

        try {

            setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
            paint = new Paint();
            paint.setColor(Color.GRAY);
            paint.setStrokeWidth(getResources().getDisplayMetrics().density * 1);

        } finally {
//            attrsArray.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        if (addStrike) {
            canvas.drawLine(0, getHeight() / 2, getWidth(),
                    getHeight() / 2, paint);
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (text != null) {
            super.setText(text, type);

        }
    }

    public void setHTMLText(String value) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            setText(value != null ? Html.fromHtml(value, Html.FROM_HTML_MODE_LEGACY) : "");
        } else {
            setText(value != null ? Html.fromHtml(value) : "");
        }
    }
}