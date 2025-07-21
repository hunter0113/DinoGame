package com.evan.dino.constants;

/**
 * 遊戲常量配置
 * 按功能分類管理所有遊戲相關常量
 */
public final class GameConstants {
    
    /**
     * 動畫相關常量
     */
    public static final class Animation {
        public static final int DEFAULT_DURATION = 1800;
        public static final int JUMP_DURATION = 700;
        public static final int CLOUD_MOVE_DURATION = 18000;
        public static final int GROUND_MOVE_DURATION = 1800;
        public static final int HURT_ANIMATION_DURATION = 500;
        public static final int INVINCIBLE_DURATION = 2000;
    }
    
    /**
     * 物理相關常量
     */
    public static final class Physics {
        public static final float JUMP_HEIGHT = -300f;
        public static final float GRAVITY = 9.8f;
        public static final int OBSTACLE_SPEED = 5;
        public static final int BACKGROUND_SPEED = 3;
    }
    
    /**
     * 遊戲邏輯常量
     */
    public static final class GameLogic {
        public static final int INITIAL_HEARTS = 3;
        public static final int MAX_HEARTS = 3;
        public static final long SCORE_INCREMENT = 1L;
        public static final int HUNDRED_THOUSAND = 100000;
        public static final int OBSTACLE_GENERATION_INTERVAL = 2000;
        public static final int DIFFICULTY_INCREASE_INTERVAL = 10000;
    }
    
    /**
     * UI相關常量
     */
    public static final class UI {
        public static final int HEART_SIZE = 48;
        public static final int SCORE_TEXT_SIZE = 24;
        public static final int GAME_OVER_TEXT_SIZE = 32;
        public static final int BUTTON_PADDING = 16;
    }
    
    /**
     * 音效相關常量
     */
    public static final class Audio {
        public static final float DEFAULT_VOLUME = 1.0f;
        public static final float JUMP_SOUND_VOLUME = 0.8f;
        public static final float HURT_SOUND_VOLUME = 0.6f;
        public static final float BACKGROUND_MUSIC_VOLUME = 0.4f;
    }
    
    /**
     * 文件相關常量
     */
    public static final class File {
        public static final String SOUND_EFFECTS_FOLDER = "sounds";
        public static final String ANIMATIONS_FOLDER = "animations";
        public static final String IMAGES_FOLDER = "images";
    }
    
    private GameConstants() {
        // 防止實例化
    }
} 