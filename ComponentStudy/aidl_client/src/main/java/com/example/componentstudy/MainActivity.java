package com.example.componentstudy;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.componentstudy.aidl_service.IPersonAidl;
import com.example.componentstudy.aidl_service.Person;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    private IPersonAidl iPersonAidl;

    private Button btn;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        bindService();

    }

    private void initView() {
        btn = findViewById(R.id.but_click);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    iPersonAidl.addPerson(new Person("coder:"+count,count));
                    List<Person> persons = iPersonAidl.getPersonList();
                    Log.i(TAG, persons.toString());
                    count++;
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void bindService(){
        Intent intent = new Intent();
        // 注意：这里的包名和 Manifest.xml里面的包名一致
        intent.setComponent(new ComponentName("com.example.componentstudy","com.example.componentstudy.aidl_service.PersonAidlService"));
        bindService(intent,connection, Context.BIND_AUTO_CREATE);
        Log.i(TAG,"bindService success.");
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(TAG,"onServiceConnected success.");
            iPersonAidl = IPersonAidl.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(TAG,"onServiceDisconnected success.");
            iPersonAidl = null;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}