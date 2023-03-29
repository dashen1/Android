package com.vtech.mobile.kidiconnect2021.customview;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.vtech.mobile.kidiconnect2021.R;

public class RecordCircleView extends View implements View.OnTouchListener, View.OnClickListener, ValueAnimator.AnimatorUpdateListener {

    private static final String TAG = "RecordCircleView";
    private boolean isInit = false;
    private int view_radius = 0;
    private ValueAnimator progressAnimator;
    private ValueAnimator btnWidthAnimator;
    private ValueAnimator btnRadiusAnimator;
    private int trigger;
    private boolean isStop = true;
    private long time = 10000;
    private Paint backRoundPaint;
    private Paint btnPaint;
    private Paint thinRingPaint;
    private Paint animateRingPaint;
    private int selectRing = 0;
    private int maxValue = 100;
    private float totalFrames = 1000.0F;
    private int backRoundColor;
    private float btnBorderSize;
    private int btnColor;
    private Bitmap btnDrawable = null;
    private int thinRingColor;
    private int animateRingColor;
    private float thinRingWidth;
    private float animateRingWidth;
    private float roundBtnWidth;
    private float rectBtnWidth;
    private float roundBtnRadius;
    private float btnChangeWidth;
    private float btnChangeRadius;
    private RectF btnRectF;
    private RectF ringRectF;

    private RecordCircleView.RecordCircleOnClickListener recordCircleOnClickListener;
    private RecordCircleView.RecordCircleOnTouchListener recordCircleOnTouchListener;

    private boolean manualStartProgress = false;

    private boolean enableReturnFalseNotRun = false;

    public RecordCircleView(Context context) {
        this(context, null);
    }

    public RecordCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initContent();
    }

    private void initialize(AttributeSet attrs, int defStyleAttr) {
        isInit = false;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RecordCircleView, defStyleAttr, 0);
        trigger = typedArray.getInt(R.styleable.RecordCircleView_rcv_btn_action, 1);
        backRoundColor = typedArray.getColor(R.styleable.RecordCircleView_rcv_back_round_color, getResources().getColor(R.color.uiwidget_color_video_ring_back));
        btnBorderSize = typedArray.getDimension(R.styleable.RecordCircleView_rcv_btn_border_size, getResources().getDimension(R.dimen.uiwidget_video_record_btn_border));
        btnColor = typedArray.getColor(R.styleable.RecordCircleView_rcv_btn_color, getResources().getColor(R.color.uiwidget_color_btn_default));
        thinRingWidth = typedArray.getDimension(R.styleable.RecordCircleView_rcv_thin_ring_width, getResources().getDimension(R.dimen.uiwidget_video_record_thin_ring_width));
        thinRingColor = typedArray.getColor(R.styleable.RecordCircleView_rcv_thin_ting_color, getResources().getColor(R.color.uiwidget_color_white));
        animateRingWidth = typedArray.getDimension(R.styleable.RecordCircleView_rcv_animate_ring_width, getResources().getDimension(R.dimen.uiwidget_video_record_animate_ring_width));
        animateRingColor = typedArray.getColor(R.styleable.RecordCircleView_rcv_animate_ring_color, getResources().getColor(R.color.uiwidget_color_white));
        initPaint();
        if (trigger == 0) {
            this.setOnClickListener(this);
        } else {
            this.setOnTouchListener(this);
        }
        typedArray.recycle();
    }

    private void initPaint() {
        backRoundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backRoundPaint.setAntiAlias(true);
        backRoundPaint.setColor(backRoundColor);

        btnPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        btnPaint.setAntiAlias(true);
        btnPaint.setColor(btnColor);

        thinRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        thinRingPaint.setAntiAlias(true);
        thinRingPaint.setStyle(Paint.Style.STROKE);
        thinRingPaint.setStrokeWidth(thinRingWidth);
        thinRingPaint.setColor(thinRingColor);

        animateRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        animateRingPaint.setAntiAlias(true);
        animateRingPaint.setStyle(Paint.Style.STROKE);
        animateRingPaint.setStrokeJoin(Paint.Join.ROUND);
        animateRingPaint.setStrokeCap(Paint.Cap.ROUND);
        animateRingPaint.setStrokeWidth(animateRingWidth);
        animateRingPaint.setColor(animateRingColor);
        invalidate();
    }

    private void initContent() {
        if (getWidth() < getHeight()) {
            view_radius = getWidth() / 2;
        } else {
            view_radius = getHeight() / 2;
        }
        if (isInit || view_radius == 0) return;
        isInit = true;
        roundBtnWidth = (view_radius - btnBorderSize) * 2 - animateRingWidth;
        roundBtnRadius = roundBtnWidth / 2;
        rectBtnWidth = (float) Math.sqrt(roundBtnWidth * roundBtnWidth / 2);
        btnChangeWidth = roundBtnWidth;
        btnChangeRadius = roundBtnRadius;
        ringRectF = new RectF(animateRingWidth / 2, animateRingWidth / 2, view_radius * 2 - animateRingWidth / 2, view_radius * 2 - animateRingWidth / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.rotate(-90, view_radius, view_radius);
        drawBackRound(canvas);
        drawBtn(canvas);
        drawThinRing(canvas);
        drawAnimateRing(canvas);
    }

    private void drawAnimateRing(Canvas canvas) {
        canvas.drawArc(ringRectF, 360, selectRing, false, animateRingPaint);
    }

    private void drawThinRing(Canvas canvas) {
        canvas.drawArc(ringRectF, 360, 360, false, thinRingPaint);
    }

    private void drawBtn(Canvas canvas) {
        float btnLeft = view_radius - btnChangeWidth / 2;
        float btnTop = view_radius - btnChangeWidth / 2;
        float btnRight = btnLeft + btnChangeWidth;
        float btnBottom = btnTop + btnChangeWidth;
        btnRectF = new RectF(btnLeft, btnTop, btnRight, btnBottom);
        canvas.drawRoundRect(btnRectF, btnChangeRadius, btnChangeRadius, btnPaint);
        if (btnDrawable != null && isStop) {
            canvas.drawBitmap(btnDrawable, new Rect(0, 0, btnDrawable.getWidth(), btnDrawable.getHeight()), btnRectF, btnPaint);
        }
    }

    private void drawBackRound(Canvas canvas) {
        canvas.drawCircle(view_radius, view_radius, view_radius - animateRingWidth / 2, backRoundPaint);
    }

    private void setBtnAnimator(float widthStart, float widthEnd, float radiusStart, float radiusEnd) {
        btnWidthAnimator = ValueAnimator.ofFloat(widthStart, widthEnd);
        btnWidthAnimator.addUpdateListener(this);
        btnRadiusAnimator = ValueAnimator.ofFloat(radiusStart, radiusEnd);
        btnRadiusAnimator.addUpdateListener(this);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(200);
        animatorSet.playTogether(btnWidthAnimator, btnRadiusAnimator);
        animatorSet.start();
    }

    private void startProgressAnimation(int value, long time) {
        if (value > maxValue) {
            value = maxValue;
        }
        int start = 0;
        int end = (int) (value * (totalFrames / maxValue));
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            time = time * 2;
        }
        setProgressAnimator(start, end, time);
    }

    private void setProgressAnimator(int start, int end, long time) {
        progressAnimator = ValueAnimator.ofInt(start,end);
        progressAnimator.setDuration(time);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.addUpdateListener(this);
        progressAnimator.start();
    }

    private void stopProgressAnimation(){
        if (progressAnimator!=null){
            progressAnimator.cancel();
            setProgressAnimator(0,0,0);
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (animation==progressAnimator) {
            int i = Integer.valueOf(String.valueOf(animation.getAnimatedValue()));
            selectRing = (int) ((i/totalFrames)*360);
            if (selectRing==360){
                setBtnAnimator(rectBtnWidth,roundBtnWidth,0,roundBtnRadius);
                stopProgressAnimation();
                isStop = true;
                if (recordCircleOnClickListener!=null) {
                    recordCircleOnClickListener.onCircleBarComplete();
                }
            }
        }else if(animation==btnWidthAnimator){
            btnChangeWidth =Float.parseFloat(animation.getAnimatedValue().toString());
        }else if(animation==btnRadiusAnimator){
            btnChangeRadius = Float.parseFloat(animation.getAnimatedValue().toString());
        }
        invalidate();
    }

    public void initAndStartProgress(){
        if (manualStartProgress){
            setBtnAnimator(roundBtnWidth,rectBtnWidth,roundBtnRadius,0);
            startProgressAnimation(maxValue,time);
            isStop = false;
        }
    }

    public boolean isManualStartProgress(){
        return manualStartProgress;
    }

    public void initToCloseProgress(){
        if (!isStop){
            setBtnAnimator(rectBtnWidth,roundBtnWidth,0,roundBtnRadius);
            stopProgressAnimation();
            invalidate();
            isStop = true;
        }
    }

    public void enableManualStartProgress() {
        this.manualStartProgress = true;
    }

    @Override
    public void onClick(View v) {
        if (v==this){
            if (isStop){
                setBtnAnimator(roundBtnWidth,rectBtnWidth,roundBtnRadius,0);
                startProgressAnimation(maxValue,time);
                isStop = false;
            }else {
                setBtnAnimator(rectBtnWidth,roundBtnWidth,0,roundBtnRadius);
                stopProgressAnimation();
                isStop = true;
                invalidate();
            }
        }
        if (recordCircleOnClickListener!=null){
            recordCircleOnClickListener.onCircleBarClick();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v==this){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    setBtnAnimator(roundBtnWidth,rectBtnWidth,roundBtnRadius,0);
                    startProgressAnimation(maxValue,time);
                    isStop = false;
                    break;
                case MotionEvent.ACTION_UP:
                    setBtnAnimator(rectBtnWidth,roundBtnWidth,0,roundBtnRadius);
                    stopProgressAnimation();
                    isStop=true;
                    invalidate();
                    break;
            }
        }
        boolean flag = true;
        if (recordCircleOnTouchListener!=null){
            flag = recordCircleOnTouchListener.onTouch(event);
        }
        return flag;
    }

    public boolean isStop(){
        return isStop;
    }

    public void setBtnColor(int btnColor){
        this.btnColor = btnColor;
        initPaint();
    }

    public void setThinRingColor(int thinRingColor) {
        this.thinRingColor = thinRingColor;
        initPaint();
    }

    public void setAnimateRingColor(int animateRingColor) {
        this.animateRingColor = animateRingColor;
        initPaint();
    }

    public void setBackRoundColor(int backRoundColor) {
        this.backRoundColor = backRoundColor;
        initPaint();
    }

    public void setThinRingWidth(float thinRingWidth) {
        this.thinRingWidth = thinRingWidth;
        initPaint();
    }

    public void setAnimateRingWidth(float animateRingWidth) {
        this.animateRingWidth = animateRingWidth;
        initPaint();
    }

    public void setBtnBorderSize(float btnBorderSize) {
        this.btnBorderSize = btnBorderSize;
        initPaint();
    }

    public void setBtnBackground(Bitmap bitmap) {
        this.btnDrawable = bitmap;
        initPaint();
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setRecordCircleOnClickListener(RecordCircleOnClickListener recordCircleOnClickListener) {
        this.recordCircleOnClickListener = recordCircleOnClickListener;
    }

    public void setRecordCircleOnTouchListener(RecordCircleOnTouchListener recordCircleOnTouchListener) {
        this.recordCircleOnTouchListener = recordCircleOnTouchListener;
    }

    public interface RecordCircleOnTouchListener {
        boolean onTouch(MotionEvent var1);
    }

    public interface RecordCircleOnClickListener {
        boolean onCircleBarClick();

        void onCircleBarComplete();
    }
}
