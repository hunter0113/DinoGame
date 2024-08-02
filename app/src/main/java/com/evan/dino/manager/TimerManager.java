package com.evan.dino.manager;

import android.widget.ImageView;

import com.evan.dino.task.RunTask;

import java.util.Timer;

/**
 * Created by Evanwei on 2022/5/4.
 * <p>
 * Description：
 */
public class TimerManager {
    public static Timer runTimer;

    public static void startRun(RunTask runTask) {
        runTimer = new Timer();
        runTimer.schedule(runTask, 0, 100);
    }

    public static void stopRun() {
        runTimer.cancel();
    }
}
