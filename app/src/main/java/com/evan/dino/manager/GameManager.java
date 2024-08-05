package com.evan.dino.manager;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.evan.dino.Dino;
import com.evan.dino.R;
import com.evan.dino.task.RunTask;

import java.util.ArrayList;

/**
 * Created by Evan on 2022/4/20.
 * <p>
 * Description：
 */
public class GameManager {

    public static boolean isGameOver = false;

    public boolean isJump = false;

    public static int obstacle = 0;

    public static int step = 0;


    private ValueAnimator jump_ani;

    public ValueAnimator getJump_ani(){
        return jump_ani;
    }
    public void setJump_ani(ValueAnimator animator){
        jump_ani = animator;
    }

    public void restart(Dino dino, ArrayList<ImageView> imageViews, TimerManager timerManager) {
        isGameOver = false;
        isJump = false;
        obstacle = 0;
        step = 0;

        setTranslateAnimation(dino, timerManager);
        dino.setHeart(3);

        RunTask runTask = new RunTask(dino.getDinoImageView());
        timerManager.startRun(runTask);

        for(int i = 0; i< imageViews.size(); i++){
            imageViews.get(i).setVisibility(View.VISIBLE);
        }
    }

    public void setTranslateAnimation(Dino dino, TimerManager timerManager) {
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
                isJump = true;
                dino.getDinoImageView().setImageResource(R.drawable.dino_1);
                timerManager.stopRun();
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isJump = false;
                if(!isGameOver){

                    RunTask runTask = new RunTask(dino.getDinoImageView());
                    timerManager.startRun(runTask);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        jump_ani.setInterpolator(new LinearInterpolator());
        jump_ani.setDuration(700);
    }
}
