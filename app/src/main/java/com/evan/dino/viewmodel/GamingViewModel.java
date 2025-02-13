
package com.evan.dino.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GamingViewModel extends ViewModel {
    private final MutableLiveData<Integer> score = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> gameOver = new MutableLiveData<>(false);

    public LiveData<Integer> getScore() {
        return score;
    }

    public void increaseScore(int value) {
        score.setValue(score.getValue() + value);
    }

    public LiveData<Boolean> isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean isGameOver) {
        gameOver.setValue(isGameOver);
    }

} 