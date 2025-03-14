package com.evan.dino.manager;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import com.evan.dino.R;

public class SoundManager {
    private SoundPool soundPool;
    private final int jumpID, deathID, scoreID;
    private boolean loaded = false;

    public SoundManager(Context context) {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(audioAttributes)
                .build();

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) {
                    loaded = true;
                }
            }
        });

        jumpID = soundPool.load(context, R.raw.jump, 1);
        deathID = soundPool.load(context, R.raw.death, 1);
        scoreID = soundPool.load(context, R.raw.score, 1);
    }

    public void playJumpSound() {
        if (loaded) {
            soundPool.play(jumpID, 1, 1, 1, 0, 1);
        }
    }

    public void playDeathSound() {
        if (loaded) {
            soundPool.play(deathID, 1, 1, 1, 0, 1);
        }
    }

    public void playScoreSound() {
        if (loaded) {
            soundPool.play(scoreID, 1, 1, 1, 0, 1);
        }
    }

    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}
