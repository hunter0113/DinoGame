package com.evan.dino.manager;

import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.evan.dino.constants.Constants;

public class AnimationManager {
    private ValueAnimator groundAnimator, cloudAnimator;
    private int width;

    public AnimationManager(int width) {
        this.width = width;
    }

    public void startGroundAnimation(ImageView backgroundOne, ImageView backgroundTwo, int duration) {
        groundAnimator = ValueAnimator.ofFloat(1.0f, 0f);
        groundAnimator.setRepeatCount(ValueAnimator.INFINITE); // 無限循環
        groundAnimator.setInterpolator(new LinearInterpolator()); // 均速
        groundAnimator.setDuration(duration);
        groundAnimator.addUpdateListener(animation -> {
            float progress = (float) animation.getAnimatedValue();
            float translationX = width * progress;
            backgroundOne.setTranslationX(translationX - width);
            backgroundTwo.setTranslationX(translationX);
        });
        groundAnimator.start();
    }

    public void startCloudAnimation(ImageView cloud1, ImageView cloud2, int duration) {
        cloudAnimator = ValueAnimator.ofFloat(1.0f, -1.0f);
        cloudAnimator.setRepeatCount(ValueAnimator.INFINITE);
        cloudAnimator.setInterpolator(new LinearInterpolator());
        cloudAnimator.setDuration(duration);
        cloudAnimator.addUpdateListener(animation -> {
            float progress = (float) animation.getAnimatedValue();
            float translationX = width * progress;
            cloud1.setTranslationX(translationX);
            cloud2.setTranslationX(translationX);
        });
        cloudAnimator.start();
    }

    public void pause() {
        if (groundAnimator != null) {
            groundAnimator.pause();
        }
        if (cloudAnimator != null) {
            cloudAnimator.pause();
        }
    }

    public void resume() {
        if (groundAnimator != null) {
            groundAnimator.resume();
        }
        if (cloudAnimator != null) {
            cloudAnimator.resume();
        }
    }

    public void stop() {
        if (groundAnimator != null) {
            groundAnimator.cancel();
        }
        if (cloudAnimator != null) {
            cloudAnimator.cancel();
        }
    }
}
