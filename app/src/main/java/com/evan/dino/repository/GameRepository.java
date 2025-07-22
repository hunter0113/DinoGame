package com.evan.dino.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.evan.dino.model.GameStats;

/**
 * 遊戲數據倉庫，負責管理遊戲狀態的持久化
 */
public class GameRepository {
    private static final String PREF_NAME = "dino_game_prefs";
    private static final String KEY_HIGH_SCORE = "high_score";
    private static final String KEY_TOTAL_PLAYS = "total_plays";
    private static final String KEY_TOTAL_SCORE = "total_score";
    
    private final SharedPreferences sharedPreferences;
    
    public GameRepository(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    
    /**
     * 保存最高分
     */
    public void saveHighScore(long score) {
        long currentHighScore = getHighScore();
        if (score > currentHighScore) {
            sharedPreferences.edit()
                    .putLong(KEY_HIGH_SCORE, score)
                    .apply();
        }
    }
    
    /**
     * 獲取最高分
     */
    public long getHighScore() {
        return sharedPreferences.getLong(KEY_HIGH_SCORE, 0);
    }
    
    /**
     * 更新遊戲統計
     */
    public void updateGameStats(long score) {
        int totalPlays = getTotalPlays() + 1;
        long totalScore = getTotalScore() + score;
        
        sharedPreferences.edit()
                .putInt(KEY_TOTAL_PLAYS, totalPlays)
                .putLong(KEY_TOTAL_SCORE, totalScore)
                .apply();
    }
    
    /**
     * 獲取總遊戲次數
     */
    public int getTotalPlays() {
        return sharedPreferences.getInt(KEY_TOTAL_PLAYS, 0);
    }
    
    /**
     * 獲取總分數
     */
    public long getTotalScore() {
        return sharedPreferences.getLong(KEY_TOTAL_SCORE, 0);
    }
    
    /**
     * 獲取遊戲統計
     */
    public GameStats getGameStats() {
        return new GameStats(
                getHighScore(),
                getTotalPlays(),
                getTotalScore()
        );
    }
    
    /**
     * 清除所有數據
     */
    public void clearAllData() {
        sharedPreferences.edit().clear().apply();
    }
} 