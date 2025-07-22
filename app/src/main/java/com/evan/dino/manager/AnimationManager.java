package com.evan.dino.manager;

import android.animation.ValueAnimator;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.evan.dino.constants.GameConstants;
import com.evan.dino.utils.ExceptionHandler;

/**
 * 動畫管理器
 * 負責管理遊戲中的背景動畫效果
 */
public class AnimationManager {
    private static final String TAG = "AnimationManager";
    
    private ValueAnimator groundAnimator, cloudAnimator;
    private final int screenWidth;

    public AnimationManager(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    /**
     * 開始地面動畫
     */
    public void startGroundAnimation(ImageView backgroundOne, ImageView backgroundTwo, int duration) {
        ExceptionHandler.safeExecute(() -> {
            if (backgroundOne == null || backgroundTwo == null) {
                Log.w(TAG, "Background views are null");
                return;
            }
            
            groundAnimator = ValueAnimator.ofFloat(1.0f, 0f);
            groundAnimator.setRepeatCount(ValueAnimator.INFINITE);
            groundAnimator.setInterpolator(new LinearInterpolator());
            groundAnimator.setDuration(duration);
            groundAnimator.addUpdateListener(animation -> {
                try {
                    if (backgroundOne != null && backgroundTwo != null) {
                        float progress = (float) animation.getAnimatedValue();
                        float translationX = screenWidth * progress;
                        
                        // 確保在主線程中更新UI
                        backgroundOne.post(() -> {
                            try {
                                backgroundOne.setTranslationX(translationX - screenWidth);
                            } catch (Exception e) {
                                // 忽略異常
                            }
                        });
                        
                        backgroundTwo.post(() -> {
                            try {
                                backgroundTwo.setTranslationX(translationX);
                            } catch (Exception e) {
                                // 忽略異常
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error updating ground animation", e);
                }
            });
            groundAnimator.start();
        }, "Start ground animation");
    }

    /**
     * 開始雲朵動畫
     */
    public void startCloudAnimation(ImageView cloud1, ImageView cloud2, int duration) {
        ExceptionHandler.safeExecute(() -> {
            if (cloud1 == null || cloud2 == null) {
                Log.w(TAG, "Cloud views are null");
                return;
            }
            
            cloudAnimator = ValueAnimator.ofFloat(1.0f, -1.0f);
            cloudAnimator.setRepeatCount(ValueAnimator.INFINITE);
            cloudAnimator.setInterpolator(new LinearInterpolator());
            cloudAnimator.setDuration(duration);
            cloudAnimator.addUpdateListener(animation -> {
                try {
                    if (cloud1 != null && cloud2 != null) {
                        float progress = (float) animation.getAnimatedValue();
                        float translationX = screenWidth * progress;
                        
                        // 確保在主線程中更新UI
                        cloud1.post(() -> {
                            try {
                                cloud1.setTranslationX(translationX);
                            } catch (Exception e) {
                                // 忽略異常
                            }
                        });
                        
                        cloud2.post(() -> {
                            try {
                                cloud2.setTranslationX(translationX);
                            } catch (Exception e) {
                                // 忽略異常
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error updating cloud animation", e);
                }
            });
            cloudAnimator.start();
        }, "Start cloud animation");
    }

    /**
     * 暫停動畫
     */
    public void pause() {
        ExceptionHandler.safeExecute(() -> {
            if (groundAnimator != null) {
                groundAnimator.pause();
            }
            if (cloudAnimator != null) {
                cloudAnimator.pause();
            }
        }, "Pause animations");
    }

    /**
     * 恢復動畫
     */
    public void resume() {
        ExceptionHandler.safeExecute(() -> {
            if (groundAnimator != null) {
                groundAnimator.resume();
            }
            if (cloudAnimator != null) {
                cloudAnimator.resume();
            }
        }, "Resume animations");
    }

    /**
     * 停止動畫
     */
    public void stop() {
        ExceptionHandler.safeExecute(() -> {
            if (groundAnimator != null) {
                groundAnimator.cancel();
            }
            if (cloudAnimator != null) {
                cloudAnimator.cancel();
            }
        }, "Stop animations");
    }
    
    /**
     * 清理資源
     */
    public void cleanup() {
        ExceptionHandler.safeExecute(() -> {
            if (groundAnimator != null) {
                groundAnimator.cancel();
                groundAnimator = null;
            }
            if (cloudAnimator != null) {
                cloudAnimator.cancel();
                cloudAnimator = null;
            }
            Log.d(TAG, "AnimationManager cleaned up");
        }, "AnimationManager cleanup");
    }
}
