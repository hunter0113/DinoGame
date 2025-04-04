package com.evan.dino.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GamingViewModel extends ViewModel {
    private final MutableLiveData<Integer> heart = new MutableLiveData<>(3); // 生命值
    private final MutableLiveData<Long> score = new MutableLiveData<>(0L);
    private final MutableLiveData<Boolean> gameOver = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> playHurtAnimation = new MutableLiveData<>(false); // 控制動畫的顯示
    
    // 新增：無敵狀態
    private final MutableLiveData<Boolean> invincible = new MutableLiveData<>(false);
    
    private final MutableLiveData<Boolean> needRestart = new MutableLiveData<>(false);
    private final MutableLiveData<Integer> obstacleCount = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> stepCount = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> isJumping = new MutableLiveData<>(false);

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

    public void setPlayHurtAnimation(boolean value) {
        playHurtAnimation.setValue(value);
    }

    // 新增方法：獲取和設置遊戲狀態
    public LiveData<Boolean> needRestart() {
        return needRestart;
    }

    public void setNeedRestart(boolean value) {
        needRestart.setValue(value);
    }

    public LiveData<Integer> getObstacleCount() {
        return obstacleCount;
    }

    public void increaseObstacleCount() {
        if (obstacleCount.getValue() != null) {
            obstacleCount.setValue(obstacleCount.getValue() + 1);
        }
    }

    public void decreaseObstacleCount() {
        if (obstacleCount.getValue() != null && obstacleCount.getValue() > 0) {
            obstacleCount.setValue(obstacleCount.getValue() - 1);
        }
    }

    public LiveData<Boolean> isJumping() {
        return isJumping;
    }

    public void setJumping(boolean jumping) {
        isJumping.setValue(jumping);
    }

    // 新增無敵狀態相關方法
    public LiveData<Boolean> isInvincible() {
        return invincible;
    }
    
    public void setInvincible(boolean value) {
        invincible.setValue(value);
    }


    public void setHeart(int value) { heart.setValue(value); }
    public void setScore(long value) { score.setValue(value); }
    public void setGameOver(boolean value) { gameOver.setValue(value); }

    public void reset() {
        heart.setValue(3);
        score.setValue(0L);
        gameOver.setValue(false);
        playHurtAnimation.setValue(false);
        needRestart.setValue(false);
        obstacleCount.setValue(0);
        stepCount.setValue(0);
        isJumping.setValue(false);
        invincible.setValue(false); // 重置無敵狀態
    }
}
