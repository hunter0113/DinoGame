package com.evan.dino.manager;

import android.util.Log;

import com.evan.dino.utils.RunTask;
import com.evan.dino.utils.ExceptionHandler;

import java.util.Timer;

/**
 * 動作計時器管理器
 * 負責管理遊戲中的定時任務
 */
public class ActionTimerManager {
    private static final String TAG = "ActionTimerManager";
    
    private Timer runTimer;

    /**
     * 開始跑步動畫
     */
    public void startRun(RunTask runTask) {
        ExceptionHandler.safeExecute(() -> {
            if (runTask == null) {
                Log.w(TAG, "RunTask is null");
                return;
            }
            
            stopRun(); // 先停止之前的計時器
            runTimer = new Timer();
            runTimer.schedule(runTask, 0, 100);
            Log.d(TAG, "Run animation started");
        }, "Start run animation");
    }

    /**
     * 停止跑步動畫
     */
    public void stopRun() {
        ExceptionHandler.safeExecute(() -> {
            if (runTimer != null) {
                runTimer.cancel();
                runTimer = null;
                Log.d(TAG, "Run animation stopped");
            }
        }, "Stop run animation");
    }
    
    /**
     * 清理資源
     */
    public void cleanup() {
        ExceptionHandler.safeExecute(() -> {
            stopRun();
            Log.d(TAG, "ActionTimerManager cleaned up");
        }, "ActionTimerManager cleanup");
    }
}
