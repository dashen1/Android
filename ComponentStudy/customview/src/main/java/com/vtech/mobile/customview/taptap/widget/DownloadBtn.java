package com.vtech.mobile.customview.taptap.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.vtech.mobile.customview.R;

import java.math.BigDecimal;

public class DownloadBtn extends View {

    private static final String TAG = "DownloadBtn";

    private Paint mPaint;
    private Path path;
    private RectF leftRect = new RectF();
    private RectF rightRect = new RectF();
    private RectF progressRect = new RectF();

    // 1、默认状态 2、下载中 3、下载完成 4、暂停
    private int state = 1;
    private int progress = 0;

    // 下载中的color
    private int color = 0x0000;
    // 为下载时的color
    private int normalColor = 0x0000;
    private int finishColor = 0x0000;
    private int max = 100;
    private int textSize = 25;

    public DownloadBtn(Context context) {
        this(context,null);
    }

    public DownloadBtn(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DownloadBtn(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DownloadBtn);
        color = typedArray.getColor(R.styleable.DownloadBtn_loadingColor,context.getResources().getColor(R.color.detail_downloadbutton_processing));
        normalColor = typedArray.getColor(R.styleable.DownloadBtn_normalColor, Color.BLUE);
        finishColor = typedArray.getColor(R.styleable.DownloadBtn_finishColor,context.getResources().getColor(R.color.detail_comment_user_level_titel_color));
        textSize = (int) typedArray.getDimension(R.styleable.DownloadBtn_textSize,25);
        typedArray.recycle();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        switch (state){
            case 1:
                drawNormal(canvas);
                break;
            case 2:
                drawProgress(canvas);
                drawText(canvas,progress+"%",Color.MAGENTA);
                break;
            case 3:
                drawFinish(canvas);
                break;
            case 4:
                drawProgress(canvas);
                drawText(canvas,"暂停中",Color.MAGENTA);
                break;
        }
    }

    private void drawFinish(Canvas canvas) {
        mPaint.setColor(finishColor);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.clipPath(path);
        canvas.drawRoundRect(new RectF(0,0,getWidth(),getHeight()),0,0,mPaint);
        drawText(canvas,"下载完成",Color.WHITE);
    }

    private void drawProgress(Canvas canvas) {
        drawBgLine(canvas);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.clipPath(path);
        int width = getWidth();
        BigDecimal b1 = new BigDecimal(progress);
        float toRight = b1.divide(new BigDecimal(max), 2, BigDecimal.ROUND_HALF_UP).floatValue() * width;
        Log.d(TAG, "drawProgress: " + toRight);
        progressRect.setEmpty();
        progressRect.left=0;
        progressRect.top=0;
        progressRect.right=toRight;
        progressRect.bottom = getHeight();
        canvas.drawRoundRect(progressRect,0,0,mPaint);
        if (progress==max){
            setState(3);
        }
    }

    private void drawNormal(Canvas canvas) {
        drawBgLine(canvas);
        // 绘制圆角内部填充色
        Log.e(TAG,"mPaint:"+mPaint);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(normalColor);
        canvas.drawPath(path,mPaint);
        // 绘制中间文字
        drawText(canvas,"下载",Color.WHITE);
    }

    private void drawBgLine(Canvas canvas) {
        // 先绘制最外层边框
        int strokeWidth = 0;
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.STROKE);
        int width = getWidth();
        int height = getHeight();
        //        float[] rids = {30.0f, 30.0f, 30.0f, 30.0f, 15.0f, 15.0f, 15.0f, 15.0f};
        //        path.addRoundRect(new RectF(0 , 0 , width, height), rids, Path.Direction.CW);
        int arcWidth = height;
        // 从0、0开始
        // path.moveTo(0,0); 这个可以不用
        // 左边圆角的上下左右坐标
        leftRect.setEmpty();
        leftRect.left = strokeWidth;
        leftRect.top = strokeWidth;
        leftRect.right = arcWidth-strokeWidth;
        leftRect.bottom = arcWidth-strokeWidth;
        path.addArc(leftRect,90,180);
        // 顶部额连接线
        path.lineTo(width-(arcWidth/2),strokeWidth);
        // 右边圆角的上下左右坐标
        rightRect.setEmpty();
        rightRect.left = width-arcWidth;
        rightRect.top = strokeWidth;
        rightRect.right = width-strokeWidth;
        rightRect.bottom = arcWidth-strokeWidth;
        path.addArc(rightRect,-90,180);
        // 底部的连接线
        path.lineTo(arcWidth/2,height-strokeWidth);
        // 绘制圆角边框
        canvas.drawPath(path,mPaint);
    }

    public void setState(int newState){
        if (state!=newState){
            this.state = newState;
            invalidate();
        }
    }

    private void drawText(Canvas canvas, String text, int color) {
        int width = getWidth();
        int height = getHeight();
        canvas.save();
        mPaint.setTextSize(textSize);
        mPaint.setColor(color);
        int textW = (int) mPaint.measureText(text);
        int textH = (int) (mPaint.descent()+mPaint.ascent());
        canvas.drawText(text,width/2-textW/2,height/2-textH/2,mPaint);
        canvas.restore();
    }

    float x1;
    float y1;
    float x2;
    float y2;



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG,"ACTION_DOWN");
                x1 = event.getX();
                y1 = event.getY();
                return true;
            case MotionEvent.ACTION_UP:
                Log.e(TAG,"ACTION_UP");
                x2 = event.getX();
                y2 = event.getY();
                doDownload();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void doDownload() {
        progress = 50;
        setState(2);
    }

    public void setProgress(int newp) {
        this.progress = newp;
        invalidate();
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getState() {
        return state;
    }
}
