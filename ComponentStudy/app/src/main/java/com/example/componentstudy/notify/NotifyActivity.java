package com.example.componentstudy.notify;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.componentstudy.R;
import com.example.componentstudy.notify.service.MediaPlayer;

public class NotifyActivity extends AppCompatActivity {

    Button btn_play_or_pause;
    int clickCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        btn_play_or_pause = findViewById(R.id.btn_play_or_pause);
        btn_play_or_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCount % 2 == 0) {
                    // start
                    MediaPlayer.playOrPause(true);
                } else {
                    // stop
                    MediaPlayer.playOrPause(false);
                }
                clickCount++;
            }
        });
    }
}
