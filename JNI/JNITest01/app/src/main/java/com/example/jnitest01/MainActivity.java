package com.example.jnitest01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.jnitest01.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'jnitest01' library on application startup.
    static {
        System.loadLibrary("jnitest01");
    }

    public static final String TAG = "MainActivity";

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void test01(View view) {
        int[] ints = new int[]{1,2,3,4,5,6};

        String[] strs = new String[]{"李小龙", "李连杰", "李元霸"};

        testArrayAction(99, "hello", ints, strs);

        for (int aInt: ints) {
            Log.d(TAG, "java层 数组值: " + aInt);
        }
    }

    public void test02(View view) {
        int[] ints = new int[]{1,2,3,4,5,6};

        String[] strs = new String[]{"李小龙", "李连杰", "李元霸"};

        testArrayAction(99, "hello", ints, strs);

        for (int aInt: ints) {
            Log.d(TAG, "java层 数组值: " + aInt);
        }
    }

    public void test05(View view) {
        deleteQuote();
    }

    /**
     * A native method that is implemented by the 'jnitest01' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native void testArrayAction(int count, String textInfo, int[] ints, String[] strs );

    public native void putObject(Student student, String str);

    public native void insertObject();

    public native void testQuote();

    public native void deleteQuote();
}