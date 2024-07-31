package com.evan.dino.manager;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ObstacleManager {
    private Timer treeTimer;
    private boolean flag1, flag2, flag3;

    public void startObstacleGeneration(ImageView tree1, ImageView tree2, ImageView tree3, int width, int duration) {
        TimerTask obstacleTask = new TimerTask() {
            @Override
            public void run() {
                int number = new Random().nextInt(3) + 1;
                switch (number) {
                    case 1:
                        if (!flag1) moveObstacle(tree1, width, duration, 1);
                        break;
                    case 2:
                        if (!flag2) moveObstacle(tree2, width, duration, 2);
                        break;
                    case 3:
                        if (!flag3) moveObstacle(tree3, width, duration, 3);
                        break;
                }
            }
        };

        treeTimer = new Timer();
        treeTimer.schedule(obstacleTask, 1000, 2000);
    }

    private void moveObstacle(ImageView obstacleView, int width, int duration, int flag) {
        ValueAnimator obsAnimator = ValueAnimator.ofInt(0, -width - obstacleView.getWidth());
        obsAnimator.setInterpolator(new LinearInterpolator());
        obsAnimator.setDuration(duration);
        obsAnimator.addUpdateListener(animation -> {
            int currentValue = (Integer) animation.getAnimatedValue();
            obstacleView.setTranslationX(currentValue);
            // Add collision detection logic here
        });

        obsAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                switch (flag) {
                    case 1:
                        obstacleView.setVisibility(View.VISIBLE);
                        flag1 = true;
                        break;
                    case 2:
                        obstacleView.setVisibility(View.VISIBLE);
                        flag2 = true;
                        break;
                    case 3:
                        obstacleView.setVisibility(View.VISIBLE);
                        flag3 = true;
                        break;
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                switch (flag) {
                    case 1:
                        flag1 = false;
                        break;
                    case 2:
                        flag2 = false;
                        break;
                    case 3:
                        flag3 = false;
                        break;
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                switch (flag) {
                    case 1:
                        flag1 = false;
                        break;
                    case 2:
                        flag2 = false;
                        break;
                    case 3:
                        flag3 = false;
                        break;
                }
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        obsAnimator.start();
    }
}
