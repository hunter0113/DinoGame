package com.evan.dino.manager;

import com.evan.dino.task.RunTask;

import java.util.Timer;

/**
 * Created by Evanwei on 2022/5/4.
 * <p>
 * Description：
 */
public class ActionTimerManager {
    public Timer runTimer;

    public void startRun(RunTask runTask) {
        runTimer = new Timer();
        runTimer.schedule(runTask, 0, 100);
    }

    public void stopRun() {
        runTimer.cancel();
    }
}
