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
        scoreViewModel = new ViewModelProvider(this, new ViewModuleFactory()).get(ScoreViewModel.class);

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
}
