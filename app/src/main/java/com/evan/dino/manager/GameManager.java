package com.evan.dino.manager;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.evan.dino.Dino;
import com.evan.dino.R;
import com.evan.dino.task.RunTask;
import com.evan.dino.viewmodel.GamingViewModel;

import java.util.ArrayList;

/**
 * Created by Evan on 2022/4/20.
 * <p>
 * Description：
 */
public class GameManager {
    private ValueAnimator jumpAnimator;
    private final GamingViewModel viewModel;
    private static final int JUMP_HEIGHT = -300;
    private static final int JUMP_DURATION = 700;

    public GameManager(GamingViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public ValueAnimator getJumpAnimator(){
        return jumpAnimator;
    }

    public void restart(Dino dino, ArrayList<ImageView> hearts, ActionTimerManager timerManager) {
        // 重置遊戲狀態
        viewModel.reset();
        
        // 初始化跳躍動畫
        initJumpAnimation(dino, timerManager);

        // 開始跑步動畫
        RunTask runTask = new RunTask(dino.getDinoImageView());
        timerManager.startRun(runTask);

        // 重置生命值顯示
        for(ImageView heart : hearts){
            heart.setVisibility(View.VISIBLE);
        }
    }

    public void initJumpAnimation(Dino dino, ActionTimerManager timerManager) {
        // 創建跳躍動畫
        jumpAnimator = ValueAnimator.ofInt(0, JUMP_HEIGHT, 0);
        jumpAnimator.setDuration(JUMP_DURATION);
        jumpAnimator.setInterpolator(new LinearInterpolator());
        
        // 設置動畫更新監聽器
        jumpAnimator.addUpdateListener(animation -> {
            int currentValue = (Integer) animation.getAnimatedValue();
            dino.getDinoImageView().setTranslationY(currentValue);
        });

        // 設置動畫狀態監聽器
        jumpAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                viewModel.setJumping(true);
                dino.getDinoImageView().setImageResource(R.drawable.dino_1);
                timerManager.stopRun();
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                viewModel.setJumping(false);
                
                Boolean isGameOverValue = viewModel.isGameOver().getValue();
                if(isGameOverValue == null || !isGameOverValue){
                    RunTask runTask = new RunTask(dino.getDinoImageView());
                    timerManager.startRun(runTask);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                // 無需操作
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                // 無需操作
            }
        });
    }
    
    public void startJump() {
        if (jumpAnimator != null && !jumpAnimator.isRunning()) {
            jumpAnimator.start();
        }
    }
}
