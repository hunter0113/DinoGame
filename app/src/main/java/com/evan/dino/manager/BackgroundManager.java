package com.evan.dino.manager;

import android.util.Log;
import android.widget.ImageView;

import androidx.lifecycle.LifecycleOwner;

import com.evan.dino.utils.ExceptionHandler;
import com.evan.dino.viewmodel.GamingViewModel;

/**
 * 背景管理器
 * 負責管理遊戲背景元素和動畫控制
 */
public class BackgroundManager {
    private static final String TAG = "BackgroundManager";
    
    private final ImageView groundOne;
    private final ImageView groundTwo;
    private final ImageView cloudOne;
    private final ImageView cloudTwo;
    private final GamingViewModel viewModel;

    /**
     * 構造函數
     */
    public BackgroundManager(ImageView groundOne, ImageView groundTwo, 
                            ImageView cloudOne, ImageView cloudTwo, 
                            GamingViewModel viewModel) {
        this.groundOne = groundOne;
        this.groundTwo = groundTwo;
        this.cloudOne = cloudOne;
        this.cloudTwo = cloudTwo;
        this.viewModel = viewModel;
    }

    /**
     * 觀察遊戲狀態，控制動畫
     */
    public void observeGameState(LifecycleOwner lifecycleOwner, AnimationManager animationManager) {
        ExceptionHandler.safeExecute(() -> {
            if (viewModel != null) {
                viewModel.isGameOver().observe(lifecycleOwner, isGameOver -> {
                    if (isGameOver != null && isGameOver && animationManager != null) {
                        animationManager.pause();
                        Log.d(TAG, "Background animation paused due to game over");
                    }
                });
            }
        }, "Observe game state");
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
    
    /**
     * 清理資源
     */
    public void cleanup() {
        ExceptionHandler.safeExecute(() -> {
            Log.d(TAG, "BackgroundManager cleaned up");
        }, "BackgroundManager cleanup");
    }
}
