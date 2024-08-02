package com.evan.dino.manager;

import android.animation.ValueAnimator;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.evan.dino.Dino;
import com.evan.dino.task.RunTask;

import java.util.ArrayList;

/**
 * Created by Evan on 2022/4/20.
 * <p>
 * Description：
 */
public class GameManager {

    public static boolean isGameOver = false;

    public static boolean isJump = false;

    public static int obstacle = 0;

    public static int step = 0;



    private static ValueAnimator jump_ani;

    private static ConstraintLayout constraintLayout;

    public static void setConstraintLayout(ConstraintLayout layout){
        constraintLayout = layout;
    }

    public static ConstraintLayout getConstraintLayout(){
        return constraintLayout;
    }

    public static ValueAnimator getJump_ani(){
        return jump_ani;
    }

    public static void setJump_ani(ValueAnimator animator){
        jump_ani = animator;
    }

    public static void restart(Dino dino, ImageView dino_Img, ArrayList<ImageView> imageViews){
        isGameOver = false;
        isJump = false;
        obstacle = 0;
        step = 0;

        dino.init();
        dino.setHeart(3);

        RunTask runTask = new RunTask(dino_Img);
        TimerManager.startRun(runTask);

        for(int i = 0; i< imageViews.size(); i++){
            imageViews.get(i).setVisibility(View.VISIBLE);
        }
    }
}
