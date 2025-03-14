package com.evan.dino.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GamingViewModel extends ViewModel {
    private final MutableLiveData<Integer> heart = new MutableLiveData<>(3); // 生命值
    private final MutableLiveData<Long> score = new MutableLiveData<>(0L);
    private final MutableLiveData<Boolean> gameOver = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> playHurtAnimation = new MutableLiveData<>(false); // 控制動畫的顯示

    public LiveData<Long> getScore() {
        return score;
    }

    public void increaseScore(long value) {
        score.setValue(value);
    }

    public LiveData<Boolean> isGameOver() {
        return gameOver;
    }

    public LiveData<Integer> getHeart() {
        return heart;
    }

    public LiveData<Boolean> shouldPlayHurtAnimation() {
        return playHurtAnimation;
    }

    public void decreaseHeart() {
        if (heart.getValue() == null) return;

        int newHeart = heart.getValue() - 1;
        heart.setValue(newHeart);

        if (newHeart <= 0) {
            gameOver.setValue(true); // 生命值歸零時，設置遊戲結束
        } else {
            // 播放受傷動畫
            playHurtAnimation.setValue(true);
        }
    }


    public void reset() {
        heart.setValue(3);
        score.setValue(0L);
        gameOver.setValue(false);
        playHurtAnimation.setValue(false);
    }
}
