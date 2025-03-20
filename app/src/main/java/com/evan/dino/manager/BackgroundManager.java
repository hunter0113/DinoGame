package com.evan.dino.manager;

import android.widget.ImageView;

import androidx.lifecycle.LifecycleOwner;

import com.evan.dino.viewmodel.GamingViewModel;

public class BackgroundManager {
    private final ImageView groundOne;
    private final ImageView groundTwo;
    private final ImageView cloudOne;
    private final ImageView cloudTwo;
    private final GamingViewModel viewModel;

    public BackgroundManager(ImageView groundOne, ImageView groundTwo, 
                            ImageView cloudOne, ImageView cloudTwo, 
                            GamingViewModel viewModel) {
        this.groundOne = groundOne;
        this.groundTwo = groundTwo;
        this.cloudOne = cloudOne;
        this.cloudTwo = cloudTwo;
        this.viewModel = viewModel;
    }

    public void observeGameState(LifecycleOwner lifecycleOwner, AnimationManager animationManager) {
        // 觀察遊戲狀態，控制動畫
        viewModel.isGameOver().observe(lifecycleOwner, isGameOver -> {
            if (isGameOver) {
                animationManager.pause();
            }
        });
    }

    public ImageView getGroundOne() {
        return groundOne;
    }

    public ImageView getGroundTwo() {
        return groundTwo;
    }
    
    public ImageView getCloudOne() {
        return cloudOne;
    }
    
    public ImageView getCloudTwo() {
        return cloudTwo;
    }
}
