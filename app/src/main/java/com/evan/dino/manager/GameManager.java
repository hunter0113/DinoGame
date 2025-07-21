package com.evan.dino.manager;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.evan.dino.constants.GameConstants;
import com.evan.dino.model.Dino;
import com.evan.dino.R;
import com.evan.dino.utils.RunTask;
import com.evan.dino.utils.ExceptionHandler;
import com.evan.dino.viewmodel.GamingViewModel;

import java.util.ArrayList;

/**
 * 遊戲管理器
 * 負責管理遊戲的核心邏輯，包括跳躍、碰撞檢測、分數更新等
 */
public class GameManager {
    private static final String TAG = "GameManager";
    
    private ValueAnimator jumpAnimator;
    private final GamingViewModel viewModel;

    public GameManager(GamingViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public ValueAnimator getJumpAnimator(){
        return jumpAnimator;
    }

    /**
     * 重新開始遊戲
     */
    public void restart(Dino dino, ArrayList<ImageView> heartImageViews, ActionTimerManager timerManager) {
        ExceptionHandler.safeExecute(() -> {
            // 重置遊戲狀態
            viewModel.reset();
            
            // 初始化跳躍動畫
            initJumpAnimation(dino, timerManager);

            // 重置生命值顯示
            resetHeartsDisplay(heartImageViews);
            
            // 開始跑步動畫
            startRunningAnimation(dino, timerManager);
            
            Log.d(TAG, "Game restarted successfully");
        }, "Game restart");
    }
    
    /**
     * 開始跑步動畫
     */
    private void startRunningAnimation(Dino dino, ActionTimerManager timerManager) {
        if (dino != null && dino.getDinoImageView() != null) {
            RunTask runTask = new RunTask(dino.getDinoImageView());
            timerManager.startRun(runTask);
        }
    }
    
    /**
     * 重置生命值顯示
     */
    private void resetHeartsDisplay(ArrayList<ImageView> heartImageViews) {
        ExceptionHandler.safeExecute(() -> {
            if (heartImageViews != null) {
                Integer currentHeart = viewModel.getHeart().getValue();
                int heartCount = currentHeart != null ? currentHeart : 3;
                
                Log.d(TAG, "Resetting hearts display with " + heartCount + " hearts");
                
                for (int i = 0; i < heartImageViews.size(); i++) {
                    ImageView heart = heartImageViews.get(i);
                    if (heart != null) {
                        boolean shouldBeVisible = i < heartCount;
                        heart.setVisibility(shouldBeVisible ? View.VISIBLE : View.GONE);
                        Log.d(TAG, "Heart " + (i + 1) + " visibility: " + shouldBeVisible);
                    }
                }
            }
        }, "Reset hearts display");
    }

    /**
     * 初始化跳躍動畫
     */
    public void initJumpAnimation(Dino dino, ActionTimerManager timerManager) {
        ExceptionHandler.safeExecute(() -> {
            if (dino == null || dino.getDinoImageView() == null) {
                Log.w(TAG, "Dino or dino image view is null");
                return;
            }
            
            // 創建跳躍動畫
            jumpAnimator = ValueAnimator.ofInt(0, (int) GameConstants.Physics.JUMP_HEIGHT, 0);
            jumpAnimator.setDuration(GameConstants.Animation.JUMP_DURATION);
            jumpAnimator.setInterpolator(new LinearInterpolator());
            
            // 設置動畫更新監聽器
            jumpAnimator.addUpdateListener(animation -> {
                try {
                    if (dino.getDinoImageView() != null) {
                        int currentValue = (Integer) animation.getAnimatedValue();
                        
                        // 確保在主線程中更新UI
                        dino.getDinoImageView().post(() -> {
                            try {
                                dino.getDinoImageView().setTranslationY(currentValue);
                            } catch (Exception e) {
                                // 忽略異常
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error updating jump animation", e);
                }
            });

            // 設置動畫狀態監聽器
            jumpAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    viewModel.setJumping(true);
                    if (dino.getDinoImageView() != null) {
                        dino.getDinoImageView().setImageResource(R.drawable.dino_1);
                    }
                    timerManager.stopRun();
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    viewModel.setJumping(false);
                    
                    Boolean isGameOverValue = viewModel.isGameOver().getValue();
                    if (isGameOverValue == null || !isGameOverValue) {
                        startRunningAnimation(dino, timerManager);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    viewModel.setJumping(false);
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                    // 無需操作
                }
            });
        }, "Jump animation initialization");
    }
    
    /**
     * 開始跳躍
     */
    public void startJump() {
        ExceptionHandler.safeExecute(() -> {
            if (jumpAnimator != null && !jumpAnimator.isRunning()) {
                jumpAnimator.start();
                Log.d(TAG, "Jump started");
            }
        }, "Start jump");
    }
    
    /**
     * 停止跳躍動畫
     */
    public void stopJump() {
        ExceptionHandler.safeExecute(() -> {
            if (jumpAnimator != null && jumpAnimator.isRunning()) {
                jumpAnimator.cancel();
                Log.d(TAG, "Jump stopped");
            }
        }, "Stop jump");
    }
    
    /**
     * 清理資源
     */
    public void cleanup() {
        ExceptionHandler.safeExecute(() -> {
            if (jumpAnimator != null) {
                jumpAnimator.cancel();
                jumpAnimator = null;
            }
            Log.d(TAG, "GameManager cleaned up");
        }, "GameManager cleanup");
    }


    public void handleCollision() {
        ExceptionHandler.safeExecute(() -> {
            Integer heartValue = viewModel.getHeart().getValue();
            if (heartValue == null) {
                Log.w(TAG, "Heart value is null");
                return;
            }
            
            // 檢查無敵狀態
            Boolean invincibleValue = viewModel.isInvincible().getValue();
            if (invincibleValue != null && invincibleValue) {
                Log.d(TAG, "Dino is invincible, collision ignored");
                return;
            }

            // 更新生命值
            int newHeart = heartValue - 1;
            viewModel.setHeart(newHeart);
            Log.d(TAG, "Collision detected! Heart reduced from " + heartValue + " to " + newHeart);

            // 處理遊戲結束邏輯
            if (newHeart <= 0) {
                viewModel.setGameOver(true);
                Log.d(TAG, "Game over due to collision");
            } else {
                // 播放受傷動畫
                viewModel.setPlayHurtAnimation(true);
                Log.d(TAG, "Hurt animation triggered");
            }
        }, "Handle collision");
    }
    
    // 處理跳躍狀態邏輯
    public void handleJumpState(boolean isJumping) {
        viewModel.setJumping(isJumping);
    }
    
    // 處理得分邏輯
    public void updateScore(long newScore) {
        viewModel.setScore(newScore);
    }
}
