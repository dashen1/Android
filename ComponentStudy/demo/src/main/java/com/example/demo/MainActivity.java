package com.example.demo;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.demo.databinding.ActivityMainBinding;
import com.example.demo.intent.MyIntentService;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity-Animation";

    ActivityMainBinding dataBinding;
    private int random;

    @SuppressLint("Recycle")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        String string = "";
        try {

            ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            string = bundle.getString("string");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        dataBinding.setMCount(0);
        String finalString = string;
        dataBinding.btnClick.setOnClickListener(v -> {
            random = getRandomInRange(49);
            dataBinding.setMCount(random);
            dataBinding.setMValue(finalString);
        });

        Intent intent = new Intent(MainActivity.this, MyIntentService.class);
        Bundle bundle = new Bundle();
        bundle.putString("taskName", "task1");
        intent.putExtras(bundle);
        startService(intent);

        Intent intent2 = new Intent(MainActivity.this, MyIntentService.class);
        Bundle bundle2 = new Bundle();
        bundle2.putString("taskName", "task2");
        intent2.putExtras(bundle2);
        startService(intent2);

        startService(intent);

        dataBinding.btnDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Configuration config = getResources().getConfiguration();
                if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    // 设为竖屏
                    MainActivity.this
                            .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                // 如果当前是竖屏
                if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    // 设为横屏
                    MainActivity.this
                            .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }
        });

        Animation animation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.myanimation);
        dataBinding.btnAnimation.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.i(TAG,"动画重复执行");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.i(TAG,"动画结束");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.i(TAG,"动画开始");
            }
        });

        ViewTreeObserver vtree = dataBinding.btnAnimation.getViewTreeObserver();
        int[] currW = {0};
        int[] currh = {0};
        // 在onCreate()方法中是无法获得一个view的高度和宽度的，这是因为View组件布局是在onResume回调后完成的
        // 在onCreate()获取View宽高值有三种方法
        // (1) 手动调用onMeasure,这样可以通过getMeasuredHeight()取得宽度和高度了
        // (2) 通过设置OnPreDrawListener监听，在view绘制之前调用OnPreDraw，在改方法里获得view的宽高。
        // 注意：这里的onPreDraw()会调用多次，是因为没有调用getViewTreeObserver().removeOnPreDrawListener(this)，加上remove方法即可
        // (3) 就是下面这种
        vtree.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                currW[0] = dataBinding.btnAnimation.getWidth();
                currh[0] = dataBinding.btnAnimation.getHeight();
                // OnGlobalLayoutListener 可能会被多次触发，所以得到宽高值后注销掉
                dataBinding.btnAnimation.getViewTreeObserver().removeOnGlobalLayoutListener(this::onGlobalLayout);
            }
        });
        ValueAnimator scale = ValueAnimator.ofInt(0, 300);
        scale.setDuration(3000);
        scale.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                dataBinding.btnAnimation.layout(0,0,currW[0]+value,currh[0]+value);
            }
        });

        // 属性动画 ObjectAnimator 方式 1
        Animator scaleY = AnimatorInflater.loadAnimator(MainActivity.this, R.animator.object_translate_y);
        Animator scaleX = AnimatorInflater.loadAnimator(MainActivity.this, R.animator.object_translate_x);
        scaleX.setTarget(dataBinding.btnAnimation);
        scaleY.setTarget(dataBinding.btnAnimation);
        scaleX.start();
        scaleY.start();

        // 属性动画 ObjectAnimator 方式 2
        ObjectAnimator scaleY2 = ObjectAnimator.ofFloat(dataBinding.btnAnimation, "scaleY", 1f, 3f);
        ObjectAnimator scaleX2 = ObjectAnimator.ofFloat(dataBinding.btnAnimation, "scaleX", 1f, 3f);
        scaleY2.setDuration(3000);
        scaleY2.setRepeatCount(-1);
        scaleY2.setRepeatMode(ObjectAnimator.REVERSE);
        scaleX2.setDuration(3000);
        scaleX2.setRepeatCount(-1);
        scaleX2.setRepeatMode(ObjectAnimator.REVERSE);
        scaleY2.start();
        scaleY2.start();

        // Path 属性动画 ObjectAnimator 方式 3
        Path path = new Path();
        path.moveTo(0f,0f);
        path.lineTo(100f,100f);
        path.lineTo(0f,100f);
        path.lineTo(0f,0f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(dataBinding.btnAnimation, "translationX", "translationY", path);
        objectAnimator.setDuration(3000);
        objectAnimator.setRepeatCount(-1);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();

        // PropertyValuesHolder 属性动画 ObjectAnimator 方式 4
        ValueAnimator.ofPropertyValuesHolder();
        ValueAnimator anim = ObjectAnimator.ofInt();
        PropertyValuesHolder translationX = PropertyValuesHolder.ofFloat("TranslationX", 0f, 100f, 0f, 0f);
        PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat("TranslationY", 0f, 100f, 100f, 0f);
        PropertyValuesHolder rotation = PropertyValuesHolder.ofFloat("Rotation", 0f, 90f, 180f, 270f, 360f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(dataBinding.btnAnimation, translationX, translationY, rotation);
        animator.setDuration(3000);
        animator.setRepeatCount(-1);
        animator.start();

        // 添加一个悬浮窗
        WindowManager wm = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);

        // 设置Layout属性
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        // 宽高尺寸
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.format = PixelFormat.TRANSPARENT;
        // 设置背景阴暗
        layoutParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.6f;
        // window 类型
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            Log.d(TAG,"version: "+Build.VERSION.SDK_INT);
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        // 构建TextView
        TextView myView = new TextView(this);
        myView.setText("hello widnow");
        // 设置背景为红色
        myView.setBackgroundResource(R.color.purple_500);
        FrameLayout.LayoutParams myParam = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 400);
        myParam.gravity = Gravity.CENTER;
        myView.setLayoutParams(myParam);

        //myFrameLayout作为rootview
        FrameLayout myFrameLayout = new FrameLayout(this);
        // 设置背景为绿色
        myFrameLayout.setBackgroundColor(Color.GREEN);
        myFrameLayout.addView(myView);
        wm.addView(myFrameLayout,layoutParams);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        String screen = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? "横屏"
                : "竖屏";
        Toast.makeText(MainActivity.this, "系统屏幕方向发生改变" + "\n修改后的屏幕方向为：" + screen,
                Toast.LENGTH_LONG).show();
    }

    private int name;

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        name = savedInstanceState.getInt("name");
        //dataBinding.setMCount(name);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("name",random);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 3, "重新开始").setIcon(R.drawable.icon);
        menu.add(0, 2, 0, "游戏指南").setIcon(R.drawable.icon);
        menu.add(0, 3, 2, "关于游戏").setIcon(R.drawable.icon);
        menu.add(0, 4, 1, "不想玩了").setIcon(R.drawable.icon);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case 1:
                //处理代码
                Toast.makeText(MainActivity.this, "重新开始", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                //处理代码
                Toast.makeText(MainActivity.this, "游戏指南", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                //处理代码
                Toast.makeText(MainActivity.this, "关于游戏", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(MainActivity.this, "不想玩了", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return true;
    }



    private int volumeUp = 0;
    private int volumeDown = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (keyCode){
//            case KeyEvent.KEYCODE_VOLUME_UP:
//                //音量键up
//                Toast.makeText(this,"升高音量", Toast.LENGTH_SHORT).show();
//            case KeyEvent.KEYCODE_VOLUME_DOWN:
//                //音量键down
//                Toast.makeText(this,"降低音量", Toast.LENGTH_SHORT).show();
//            default:
//                break;
//        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            volumeUp++;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            volumeDown++;
        }
        if (volumeUp == 1 && volumeDown == 1) {
            Log.d("TAG", "组合键");
            volumeUp = 0;
            volumeDown = 0;
            Toast.makeText(this, "音量组合键", Toast.LENGTH_SHORT).show();
        }
        return super.onKeyDown(keyCode, event);
    }

    // 监听遥控器的左键和右键，当连续按下左左右右后，弹出Toast
    private long liftTime = 0;
    private long rightTime = 0;
    int leftCount = 0;
    int rightCount = 0;



//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode==KeyEvent.KEYCODE_DPAD_LEFT && event.getAction() == KeyEvent.ACTION_DOWN){
//            leftCount++;
//            liftTime = System.currentTimeMillis();
//        }else if (keyCode==KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN){
//            rightCount++;
//            rightTime = System.currentTimeMillis();
//        }
//        Log.e("TAG","rightTime-liftTime = "+(rightTime-liftTime));
//        Log.e("TAG","leftCount = "+leftCount);
//        Log.e("TAG","rightCount"+rightCount);
//
//        if (rightTime-liftTime < 2000 && leftCount == 2 && rightCount == 2){
//            leftCount = 0;
//            rightCount = 0;
//            Toast.makeText(this,"退出应用", Toast.LENGTH_SHORT).show();
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    public static int getRandomInRange(int range) {
        return (int) (1 + Math.random() * (range));
    }

}