package com.example.mylibrary;

import android.content.Context;
import android.content.Intent;

public class ActivityUtil {

    public static void  gotoActivity(Class<?> clazz, Context context) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }
}
