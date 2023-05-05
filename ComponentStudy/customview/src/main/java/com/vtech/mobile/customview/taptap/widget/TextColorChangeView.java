package com.vtech.mobile.customview.taptap.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class TextColorChangeView extends AppCompatTextView {

    private static final String TAG = "TextColorChangeView";

    private Paint normalPaint;

    private Paint changePaint;

    private float progress = 0;

    // 绘制方向跟滑动方向一致
    private boolean front = true;

    private boolean after = false;


    public TextColorChangeView(Context context) {
        this(context, null);
    }

    public TextColorChangeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextColorChangeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        normalPaint = new Paint();
        normalPaint.setAntiAlias(true);
        normalPaint.setColor(getTextColors().getDefaultColor());
        normalPaint.setTextSize(getTextSize());

        changePaint = new Paint();
        changePaint.setAntiAlias(true);
        changePaint.setColor(Color.RED);
        changePaint.setTextSize(getTextSize());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        String text = getText().toString();
        if (!TextUtils.isEmpty(text)) {
            int position = (int) (getWidth() * progress);
            if (after) {
                drawText(canvas, text, 0, position);
            } else if (front) {
                drawText(canvas, text, getWidth() - position, getWidth());
            }
        }
    }

    private void drawText(Canvas canvas, String text, int left, int right) {
        canvas.save();
        canvas.clipRect(0, 0, getWidth(), getHeight());
        int textWidth = (int) normalPaint.measureText(text);
        // descent是正数，ascent是负数
        Paint.FontMetrics fontMetrics = normalPaint.getFontMetrics();
        float textHeight = -fontMetrics.ascent - (fontMetrics.descent - fontMetrics.ascent) / 2;
        canvas.drawText(text, getWidth() / 2 - textWidth / 2, getHeight() / 2 + textHeight, normalPaint);
        canvas.restore();

        canvas.save();
        canvas.clipRect(left, 0, right, getHeight());
        canvas.drawText(text, getWidth() / 2 - textWidth / 2, getHeight() / 2 + textHeight, changePaint);
        canvas.restore();
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    public void setAfter(boolean after) {
        this.after = after;
        this.front = !after;
    }
}
