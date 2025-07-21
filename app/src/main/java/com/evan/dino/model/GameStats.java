package com.evan.dino.model;

/**
 * 遊戲統計數據模型
 */
public class GameStats {
    private final long highScore;
    private final int totalPlays;
    private final long totalScore;
    
    public GameStats(long highScore, int totalPlays, long totalScore) {
        this.highScore = highScore;
        this.totalPlays = totalPlays;
        this.totalScore = totalScore;
    }
    
    public long getHighScore() {
        return highScore;
    }
    
    public int getTotalPlays() {
        return totalPlays;
    }
    
    public long getTotalScore() {
        return totalScore;
    }
    
    public double getAverageScore() {
        return totalPlays > 0 ? (double) totalScore / totalPlays : 0.0;
    }
} 