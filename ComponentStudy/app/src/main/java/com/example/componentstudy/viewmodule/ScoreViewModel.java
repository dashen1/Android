package com.example.componentstudy.viewmodule;

import androidx.lifecycle.ViewModel;

public class ScoreViewModel extends ViewModel {

    private int scoreA = 0;

    private int scoreB = 0;

    public int getScoreA() {
        return scoreA;
    }

    public void setScoreA(int scoreA) {
        this.scoreA = scoreA;
    }

    public int getScoreB() {
        return scoreB;
    }

    public void setScoreB(int scoreB) {
        this.scoreB = scoreB;
    }
}
