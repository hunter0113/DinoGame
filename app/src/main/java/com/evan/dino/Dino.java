package com.evan.dino;

import static com.evan.dino.manager.GameManager.isGameOver;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.evan.dino.manager.GameManager;
import com.evan.dino.task.RunTask;
import java.util.ArrayList;

/**
 * Created by Evan on 2022/4/13.
 * <p>
 * Description：
 */
public class Dino {

    private final ImageView dinoImg;
    private final ImageView heart1;
    private final ImageView heart2;
    private final ImageView heart3;

    private int heart = 0;
    private boolean invincible = false;


    public Dino(ImageView dino, ArrayList<ImageView> imageViews) {
        dinoImg = dino;
        heart1 = imageViews.get(0);
        heart2 = imageViews.get(1);
        heart3 = imageViews.get(2);
    }

    public void init() {
        setTranslateAnimation();
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public boolean getInvincible() {
        return invincible;
    }


    private void setTranslateAnimation() {
        ValueAnimator jump_ani = ValueAnimator.ofInt(0, -300, 0);
        GameManager.setJump_ani(jump_ani);

        GameManager.getJump_ani().addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (Integer) animation.getAnimatedValue();
                dinoImg.setTranslationY(currentValue);
            }
        });

        GameManager.getJump_ani().addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                GameManager.isJump = true;
                dinoImg.setImageResource(R.drawable.dino_1);
                TimerManager.stopRun();
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                GameManager.isJump = false;
                if(!isGameOver){

                    RunTask runTask = new RunTask(dinoImg);
                    TimerManager.startRun(runTask);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        GameManager.getJump_ani().setInterpolator(new LinearInterpolator());
        GameManager.getJump_ani().setDuration(700);
    }

    public void hurtAnimation() {
        heart--;

        if (heart == 2) {
            heart1.setVisibility(View.INVISIBLE);
        }

        if (heart == 1) {
            heart2.setVisibility(View.INVISIBLE);
        }

        if (heart == 0) {
            heart3.setVisibility(View.INVISIBLE);
            isGameOver = true;
            return;
        }

        initAnimation();
    }

    private void initAnimation() {

        final Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(50);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(8);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                invincible = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                invincible = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        dinoImg.setAnimation(animation);
    }

    public ImageView getDinoImageView() {
        return dinoImg;
    }
}
