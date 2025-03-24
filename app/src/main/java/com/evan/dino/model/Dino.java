package com.evan.dino.model;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.evan.dino.viewmodel.GamingViewModel;

/**
 * Created by Evan on 2022/4/13.
 * <p>
 * Description：
 */
public class Dino {

    private final ImageView dinoImg;
    private final GamingViewModel viewModel;

    public boolean getInvincible(){
        Boolean invincible = viewModel.isInvincible().getValue();
        return invincible != null && invincible;
    }

    public Dino(ImageView dino, GamingViewModel viewModel) {
        this.dinoImg = dino;
        this.viewModel = viewModel;
    }

    public void playHurtAnimation() {
        if (getInvincible()){
            return;
        }

        viewModel.setInvincible(true);
        initAnimation();
    }

    private void initAnimation() {
        final Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(50);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(8);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setAnimationListener(createAnimationListener());
        dinoImg.startAnimation(animation);
    }

    private Animation.AnimationListener createAnimationListener() {
        return new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                viewModel.setInvincible(true);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                viewModel.setInvincible(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
    }

    public ImageView getDinoImageView() {
        return dinoImg;
    }
}
