package com.evan.dino.manager;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.Log;

import com.evan.dino.R;
import com.evan.dino.constants.GameConstants;
import com.evan.dino.utils.ExceptionHandler;

/**
 * 音效管理器
 * 負責管理遊戲中的所有音效播放
 */
public class SoundManager {
    private static final String TAG = "SoundManager";
    
    private SoundPool soundPool;
    private int jumpSoundId;
    private int deathSoundId;
    private int scoreSoundId;
    private boolean isLoaded = false;
    private boolean isSoundEnabled = true;
    private float volume = GameConstants.Audio.DEFAULT_VOLUME;

    public SoundManager(Context context) {
        ExceptionHandler.safeExecute(() -> {
            initializeSoundPool(context);
        }, "SoundManager initialization");
    }
    
    /**
     * 初始化音效池
     */
    private void initializeSoundPool(Context context) {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(audioAttributes)
                .build();

        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            if (status == 0) {
                isLoaded = true;
                Log.d(TAG, "Sound pool loaded successfully");
            } else {
                Log.e(TAG, "Failed to load sound pool, status: " + status);
            }
        });

        // 載入音效文件
        jumpSoundId = soundPool.load(context, R.raw.jump, 1);
        deathSoundId = soundPool.load(context, R.raw.death, 1);
        scoreSoundId = soundPool.load(context, R.raw.score, 1);
    }

    /**
     * 播放跳躍音效
     */
    public void playJumpSound() {
        if (isLoaded && isSoundEnabled && soundPool != null) {
            soundPool.play(jumpSoundId, volume * GameConstants.Audio.JUMP_SOUND_VOLUME, 
                          volume * GameConstants.Audio.JUMP_SOUND_VOLUME, 1, 0, 1);
        }
    }

    /**
     * 播放死亡音效
     */
    public void playDeathSound() {
        if (isLoaded && isSoundEnabled && soundPool != null) {
            soundPool.play(deathSoundId, volume * GameConstants.Audio.HURT_SOUND_VOLUME, 
                          volume * GameConstants.Audio.HURT_SOUND_VOLUME, 1, 0, 1);
        }
    }

    /**
     * 播放得分音效
     */
    public void playScoreSound() {
        if (isLoaded && isSoundEnabled && soundPool != null) {
            soundPool.play(scoreSoundId, volume, volume, 1, 0, 1);
        }
    }
    
    /**
     * 設置音效開關
     */
    public void setSoundEnabled(boolean enabled) {
        this.isSoundEnabled = enabled;
    }
    
    /**
     * 設置音量
     */
    public void setVolume(float volume) {
        this.volume = Math.max(0.0f, Math.min(1.0f, volume));
    }
    
    /**
     * 檢查音效是否已載入
     */
    public boolean isLoaded() {
        return isLoaded;
    }
    
    /**
     * 檢查音效是否啟用
     */
    public boolean isSoundEnabled() {
        return isSoundEnabled;
    }

    /**
     * 釋放資源
     */
    public void release() {
        ExceptionHandler.safeExecute(() -> {
            if (soundPool != null) {
                soundPool.release();
                soundPool = null;
                isLoaded = false;
                Log.d(TAG, "Sound pool released");
            }
        }, "SoundManager release");
    }
}
