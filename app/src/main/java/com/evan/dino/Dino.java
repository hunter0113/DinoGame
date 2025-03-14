package com.evan.dino;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;


/**
 * Created by Evan on 2022/4/13.
 * <p>
 * Description：
 */
public class Dino {

    private final ImageView dinoImg;
    private boolean invincible;

    private OnHurtListener hurtListener;

    public boolean getInvincible(){
        return invincible;
    }

    public Dino(ImageView dino) {
        dinoImg = dino;
    }

    public interface OnHurtListener {
        void onHurt();
    }


    public void setOnHurtListener(OnHurtListener listener) {
        this.hurtListener = listener;
    }


    public void hurtHandle(){
        hurtListener.onHurt();
    }


    // 播放受傷動畫
    public void playHurtAnimation() {
        if (invincible){
            return;
        }

        invincible = true;
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
                invincible = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                invincible = false;
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
