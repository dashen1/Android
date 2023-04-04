package com.example.demo;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.demo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding dataBinding;

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
            dataBinding.setMCount(getRandomInRange(49));
            dataBinding.setMValue(finalString);
        });

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