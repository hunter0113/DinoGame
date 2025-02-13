package com.evan.dino.task;

import android.widget.ImageView;

import com.evan.dino.R;

import java.util.TimerTask;

/**
 * Created by Evanwei on 2022/4/29.
 * <p>
 * Description：
 */
public class RunTask extends TimerTask {

    private int run_count = 0;
    private final int[] imgDinoRun = new int[]{
            R.drawable.dino_3,
            R.drawable.dino_4,
    };

    private final ImageView dinoImg;

    public RunTask(ImageView dinoImg) {
        this.dinoImg = dinoImg;
    }

    @Override
    public void run() {
        dinoImg.setImageResource(imgDinoRun[run_count]);

        if (run_count == 0) {
            run_count = 1;
        } else {
            run_count = 0;
        }
    }
}
