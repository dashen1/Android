package com.example.componentstudy.viewmodule;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.componentstudy.R;

import java.util.Random;

/**
 * ViewModel 的生命周期从Activity create到 onDestroy, 和Activity生命周期一致
 *
 * 因为在屏幕旋转的时候，activity 会执行 onDestroy()方法，然后重新执行 onCreate()方法，
 * 这就导致了屏幕旋转后显示的数据不一致
 */
public class ViewModuleActivity extends AppCompatActivity {

    private Score score;

    private TextView tv_score;

    private TextView tv_score_viewmodel;

    private ScoreViewModel scoreViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewmodule);

        tv_score = findViewById(R.id.tv_score);
        tv_score_viewmodel = findViewById(R.id.tv_score_viewmodel);

        score = new Score();
        tv_score.setText("s"+score.getScoreA());

        System.out.println("ViewModuleActivity : onCreate");

        // viewmodule
        scoreViewModel = new ViewModelProvider(this).get(ScoreViewModel.class);

        tv_score_viewmodel.setText("v:"+scoreViewModel.getScoreA());
    }

    public void changeScore(View view) {
        score.setScoreA(score.getScoreA()+new Random().nextInt(10));
        tv_score.setText("s"+score.getScoreA());
    }

    public void changeScoreViewModel(View view) {
        scoreViewModel.setScoreA(scoreViewModel.getScoreA()+new Random().nextInt(10));
        tv_score_viewmodel.setText("v:"+scoreViewModel.getScoreA());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("ViewModuleActivity : onDestroy");
    }
}
