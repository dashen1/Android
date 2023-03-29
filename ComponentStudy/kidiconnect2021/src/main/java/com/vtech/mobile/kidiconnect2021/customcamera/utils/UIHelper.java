package com.vtech.mobile.kidiconnect2021.customcamera.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

/**
 * UI相关工具类
 * Author: T.L. QIU
 * Date: 2020-08-26 15:16.
 */
public class UIHelper {

  private static String defaultUserNick;
  private static String defaultGroupNick;
  private static String deleteUserNick;

  private static String OT_IMAGE;
  private static String OT_AUDIO;
  private static String OT_STICKER;
  private static String OT_VIDEO;

  private static String OT_IMAGE_GROUP;
  private static String OT_AUDIO_GROUP;
  private static String OT_STICKER_GROUP;
  private static String OT_VIDEO_GROUP;

  public static void init(Context context) {

  }

  /**
   * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
   */
  public static int dip2px(Context context, float dpValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }

  /**
   * dp转px
   *
   * @param context
   * @param dpVal
   * @return
   */
  public static int dp2px(Context context, float dpVal) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            dpVal, context.getResources().getDisplayMetrics());
  }

  public static void showCustomTextView(Activity activity, TextView view, String text, Long time) {
    if (view != null) {
      if (view.getVisibility() == View.VISIBLE)
        view.setVisibility(View.GONE);
      view.setText(text);
      view.setVisibility(View.VISIBLE);
      Handler handler = new Handler();
      Runnable runnable = new Runnable() {
        @Override
        public void run() {
          activity.runOnUiThread(() -> {
            view.setVisibility(View.GONE);
          });
          handler.removeCallbacks(this);
        }
      };
      handler.postDelayed(runnable, time);
    }
  }

  public static void hideCustomToast(TextView view) {
    if (view != null && view.getVisibility() == View.VISIBLE) {
      view.setVisibility(View.GONE);
    }
  }

  /**
   * 获取屏幕高度(dp)
   */
  public static int getDPValueOfScreenHeight(Context context) {
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics dm = new DisplayMetrics();
    wm.getDefaultDisplay().getMetrics(dm);
    int height = dm.heightPixels;       // 屏幕高度（像素）
    float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
    // 屏幕高度(dp)
    return (int) (height / density);
  }

  /**
   * 获取屏幕宽度(dp)
   */
  public static int getDPValueOfScreenWidth(Context context) {
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics dm = new DisplayMetrics();
    wm.getDefaultDisplay().getMetrics(dm);
    int width = dm.widthPixels;         // 屏幕宽度（像素）
    float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
    // 屏幕宽度(dp)
    return (int) (width / density);
  }

  public static int getPXValueOfScreenWidth(Context context) {
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics dm = new DisplayMetrics();
    wm.getDefaultDisplay().getMetrics(dm);
    return dm.widthPixels;
  }

  public static byte[] bitmap2Bytes(Bitmap bitmap) {
    if (bitmap == null)
      return null;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    return baos.toByteArray();
  }

  public static Bitmap bytes2Bitmap(byte[] bytes) {
    if (bytes.length != 0)
      return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    return null;
  }

  public static boolean checkBitmapComplete(Bitmap bitmap) {
    try {
      bitmap.getWidth();
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  public static Bitmap rotateBitmap(Bitmap bm, int orientationDegree) {
    // 方便判断，角度都转换为正值
    int degree = orientationDegree;
    if (degree < 0) {
      degree = 360 + orientationDegree;
    }

    int srcW = bm.getWidth();
    int srcH = bm.getHeight();

    Matrix m = new Matrix();
    m.setRotate(degree, (float) srcW / 2, (float) srcH / 2);
    float targetX, targetY;

    int destH = srcH;
    int destW = srcW;

    //根据角度计算偏移量，原理不明
    if (degree == 90) {
      targetX = srcH;
      targetY = 0;
      destH = srcW;
      destW = srcH;
    } else if (degree == 270) {
      targetX = 0;
      targetY = srcW;
      destH = srcW;
      destW = srcH;
    } else if (degree == 180) {
      targetX = srcW;
      targetY = srcH;
    } else {
      return bm;
    }

    final float[] values = new float[9];
    m.getValues(values);

    float x1 = values[Matrix.MTRANS_X];
    float y1 = values[Matrix.MTRANS_Y];

    m.postTranslate(targetX - x1, targetY - y1);

    //注意destW 与 destH 不同角度会有不同
    Bitmap bm1 = Bitmap.createBitmap(destW, destH, Bitmap.Config.ARGB_8888);
    Paint paint = new Paint();
    Canvas canvas = new Canvas(bm1);
    canvas.drawBitmap(bm, m, paint);
    return bm1;
  }

  /**
   * 水平翻转
   */
  public static Bitmap bitmapMirrorFlip(Bitmap bitmap) {
    Matrix m = new Matrix();
    m.postScale(-1, 1);
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
  }

  /**
   * 判断是否在UI线程中允许
   *
   * @return
   */
  public static boolean isRunInUIThread() {
    return "main".endsWith(Thread.currentThread().getName());
  }


  /**
   * 显示View组件
   */
  public static void setViewShow(View view) {
    view.setVisibility(View.VISIBLE);
  }

  /**
   * 隐藏View组件
   */
  public static void setViewGone(View view) {
    view.setVisibility(View.GONE);
  }

  /**
   * 隐藏View组件
   */
  public static void setViewHide(View view) {
    view.setVisibility(View.INVISIBLE);
  }

  /**
   * 全屏显示
   * 隐藏导航栏 标题栏 虚拟按键
   */
  public static void setFullScreen(AppCompatActivity activity, boolean show) {
    if (show) {
      setStatusBarShow(activity, false);
      setTitleBarShow(activity, false);
      hideVirtualButton(activity);
    }
  }

  /**
   * 标题栏显示控制
   */
  public static void setTitleBarShow(AppCompatActivity activity, boolean show) {

    if (activity.getSupportActionBar() != null) {
      if (show) {
        activity.getSupportActionBar().show();
      } else {
        activity.getSupportActionBar().hide();
      }
    }
  }

  /**
   * 状态栏显示控制
   */
  public static void setStatusBarShow(Activity activity, boolean show) {
    if (show) {
      activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    } else {
      activity.getWindow().getDecorView().setSystemUiVisibility(View.INVISIBLE);
    }
  }

  /**
   * 隐藏虚拟按键
   */
  public static void hideVirtualButton(Activity activity) {
    Window window = activity.getWindow();
    WindowManager.LayoutParams params = window.getAttributes();
    params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
    window.setAttributes(params);
  }

  public static int getNavigationBarHeight(Context context) {
    int result = 0;
    int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
    if (resourceId > 0) {
      result = context.getResources().getDimensionPixelSize(resourceId);
    }
    return result;
  }

  /**
   * 非全面屏下 虚拟键实际高度(隐藏后高度为0)
   *
   * @param activity
   * @return
   */
  public static int getCurrentNavigationBarHeight(Activity activity) {
    if (isNavigationBarShown(activity)) {
      return getNavigationBarHeight(activity);
    } else {
      return 0;
    }
  }

  /**
   * 非全面屏下 虚拟按键是否打开
   *
   * @param activity
   * @return
   */
  public static boolean isNavigationBarShown(Activity activity) {
    //虚拟键的view,为空或者不可见时是隐藏状态
    View view = null;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
      view = activity.findViewById(android.R.id.navigationBarBackground);
    }
    if (view == null) {
      return false;
    }
    int visible = view.getVisibility();
    if (visible == View.GONE || visible == View.INVISIBLE) {
      return false;
    } else {
      return true;
    }
  }

  public static void setKeepScreen(Activity activity) {
    activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
  }


  public static String getDefaultUserNick() {
    return defaultUserNick;
  }

  public static String getDefaultGroupNick() {
    return defaultGroupNick;
  }

  public static String getDeleteUserNick() {
    return deleteUserNick;
  }

  public static String getDefaultNick(boolean isGroup) {
    if (isGroup)
      return getDefaultGroupNick();
    else
      return getDefaultUserNick();
  }

  public static String getDefauleNick(boolean isGroup) {
    return isGroup ? defaultGroupNick : defaultUserNick;
  }


  public static String getOT_IMAGE() {
    return OT_IMAGE;
  }

  public static String getOT_AUDIO() {
    return OT_AUDIO;
  }

  public static String getOT_STICKER() {
    return OT_STICKER;
  }

  public static String getOT_VIDEO() {
    return OT_VIDEO;
  }

  public static String getOT_IMAGE_GROUP() {
    return OT_IMAGE_GROUP;
  }

  public static String getOT_AUDIO_GROUP() {
    return OT_AUDIO_GROUP;
  }

  public static String getOT_STICKER_GROUP() {
    return OT_STICKER_GROUP;
  }

  public static String getOT_VIDEO_GROUP() {
    return OT_VIDEO_GROUP;
  }
}
