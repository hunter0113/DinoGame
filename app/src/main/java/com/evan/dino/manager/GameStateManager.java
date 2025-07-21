package com.evan.dino.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.evan.dino.constants.GameConstants;

/**
 * 遊戲狀態管理器
 * 負責管理遊戲的暫停、恢復、設置等功能
 */
public class GameStateManager {
    private static final String PREF_NAME = "game_settings";
    private static final String KEY_SOUND_ENABLED = "sound_enabled";
    private static final String KEY_MUSIC_ENABLED = "music_enabled";
    private static final String KEY_DIFFICULTY = "difficulty";
    private static final String KEY_VIBRATION_ENABLED = "vibration_enabled";
    
    private final SharedPreferences preferences;
    private final Context context;
    
    // 遊戲狀態
    private boolean isPaused = false;
    private boolean isGameOver = false;
    private boolean isFirstLaunch = true;
    
    public GameStateManager(Context context) {
        this.context = context.getApplicationContext();
        this.preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    
    /**
     * 暫停遊戲
     */
    public void pauseGame() {
        if (!isGameOver) {
            isPaused = true;
        }
    }
    
    /**
     * 恢復遊戲
     */
    public void resumeGame() {
        isPaused = false;
    }
    
    /**
     * 檢查遊戲是否暫停
     */
    public boolean isPaused() {
        return isPaused;
    }
    
    /**
     * 設置遊戲結束狀態
     */
    public void setGameOver(boolean gameOver) {
        this.isGameOver = gameOver;
        if (gameOver) {
            isPaused = false; // 遊戲結束時取消暫停狀態
        }
    }
    
    /**
     * 檢查遊戲是否結束
     */
    public boolean isGameOver() {
        return isGameOver;
    }
    
    /**
     * 重置遊戲狀態
     */
    public void resetGameState() {
        isPaused = false;
        isGameOver = false;
    }
    
    // 音效設置
    public boolean isSoundEnabled() {
        return preferences.getBoolean(KEY_SOUND_ENABLED, true);
    }
    
    public void setSoundEnabled(boolean enabled) {
        preferences.edit().putBoolean(KEY_SOUND_ENABLED, enabled).apply();
    }
    
    // 音樂設置
    public boolean isMusicEnabled() {
        return preferences.getBoolean(KEY_MUSIC_ENABLED, true);
    }
    
    public void setMusicEnabled(boolean enabled) {
        preferences.edit().putBoolean(KEY_MUSIC_ENABLED, enabled).apply();
    }
    
    // 難度設置
    public int getDifficulty() {
        return preferences.getInt(KEY_DIFFICULTY, 1); // 1=簡單, 2=中等, 3=困難
    }
    
    public void setDifficulty(int difficulty) {
        if (difficulty >= 1 && difficulty <= 3) {
            preferences.edit().putInt(KEY_DIFFICULTY, difficulty).apply();
        }
    }
    
    // 震動設置
    public boolean isVibrationEnabled() {
        return preferences.getBoolean(KEY_VIBRATION_ENABLED, true);
    }
    
    public void setVibrationEnabled(boolean enabled) {
        preferences.edit().putBoolean(KEY_VIBRATION_ENABLED, enabled).apply();
    }
    
    /**
     * 獲取基於難度的遊戲參數
     */
    public float getObstacleSpeedMultiplier() {
        int difficulty = getDifficulty();
        switch (difficulty) {
            case 1: return 0.8f; // 簡單
            case 2: return 1.0f; // 中等
            case 3: return 1.3f; // 困難
            default: return 1.0f;
        }
    }
    
    public int getObstacleGenerationInterval() {
        int difficulty = getDifficulty();
        int baseInterval = GameConstants.GameLogic.OBSTACLE_GENERATION_INTERVAL;
        switch (difficulty) {
            case 1: return (int) (baseInterval * 1.2); // 簡單：障礙物生成較慢
            case 2: return baseInterval; // 中等：正常速度
            case 3: return (int) (baseInterval * 0.8); // 困難：障礙物生成較快
            default: return baseInterval;
        }
    }
    
    /**
     * 檢查是否為首次啟動
     */
    public boolean isFirstLaunch() {
        return isFirstLaunch;
    }
    
    public void setFirstLaunch(boolean firstLaunch) {
        this.isFirstLaunch = firstLaunch;
    }
    
    /**
     * 清理資源
     */
    public void cleanup() {
        // 清理相關資源
    }
} 