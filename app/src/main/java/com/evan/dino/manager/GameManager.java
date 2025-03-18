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
    // 移除所有靜態變數，完全依賴ViewModel
    // public static boolean isGameOver = false;
    // public static boolean needRestart = false;
    // public static int obstacle = 0;
    // public static int step = 0;

    private ValueAnimator jump_ani;
    private final GamingViewModel viewModel;

    public GameManager(GamingViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public ValueAnimator getJump_ani(){
        return jump_ani;
    }
    
    public void setJump_ani(ValueAnimator animator){
        jump_ani = animator;
    }

    public void restart(Dino dino, ArrayList<ImageView> imageViews, ActionTimerManager timerManager) {
        // 使用ViewModel重置遊戲狀態
        viewModel.setJumping(false);
        viewModel.reset(); // 重置所有狀態
        
        // 不再設置靜態變數

        setTranslateAnimation(dino, timerManager);

        RunTask runTask = new RunTask(dino.getDinoImageView());
        timerManager.startRun(runTask);

        for(int i = 0; i< imageViews.size(); i++){
            imageViews.get(i).setVisibility(View.VISIBLE);
        }
    }

    public void setTranslateAnimation(Dino dino, ActionTimerManager timerManager) {
        jump_ani = ValueAnimator.ofInt(0, -300, 0);
        setJump_ani(jump_ani);

        jump_ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (Integer) animation.getAnimatedValue();
                dino.getDinoImageView().setTranslationY(currentValue);
            }
        });

        jump_ani.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                // 只使用ViewModel設置跳躍狀態
                viewModel.setJumping(true);
                
                dino.getDinoImageView().setImageResource(R.drawable.dino_1);
                timerManager.stopRun();
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                // 只使用ViewModel獲取遊戲狀態
                viewModel.setJumping(false);
                
                Boolean isGameOverValue = viewModel.isGameOver().getValue();
                if(isGameOverValue == null || !isGameOverValue){
                    RunTask runTask = new RunTask(dino.getDinoImageView());
                    timerManager.startRun(runTask);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                // Do nothing
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                // Do nothing
            }
        });
        jump_ani.setInterpolator(new LinearInterpolator());
        jump_ani.setDuration(700);
    }
    
    // 也移除isJump變數，完全使用ViewModel的isJumping
    // public boolean isJump = false;
    
    // 為了方便檢查跳躍狀態，添加輔助方法
    public boolean isJumping() {
        Boolean isJumping = viewModel.isJumping().getValue();
        return isJumping != null && isJumping;
    }
}
