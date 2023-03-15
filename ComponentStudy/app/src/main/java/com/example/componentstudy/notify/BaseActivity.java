package com.example.componentstudy.notify;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.componentstudy.MediaAidlInterface;
import com.example.componentstudy.notify.service.MediaPlayer;

public class BaseActivity extends AppCompatActivity implements ServiceConnection {

    private MediaPlayer.ServiceToken mToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToken = MediaPlayer.bindToService(this,this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unBindService();
    }

    public void unBindService(){
        if (mToken!=null){
            MediaPlayer.unbindFromService(mToken);
            mToken=null;
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MediaPlayer.mService = MediaAidlInterface.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        MediaPlayer.mService = null;
    }
}
