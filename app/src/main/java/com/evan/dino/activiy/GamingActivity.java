package com.evan.dino.activiy;


import static android.view.View.INVISIBLE;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;
import static com.evan.dino.manager.GameManager.isGameOver;
import static com.evan.dino.constants.Constants.CLOUD_MOVE_DURATION;
import static com.evan.dino.constants.Constants.DEFAULT_DURATION;
import static com.evan.dino.constants.Constants.GROUND_MOVE_DURATION;
import static com.evan.dino.constants.Constants.HUNDRED_THOUSAND;
import static com.evan.dino.constants.Constants.JUMP_DURATION;
import static com.evan.dino.constants.Constants.JUMP_HEIGHT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.evan.dino.Dino;
import com.evan.dino.manager.BackgroundManager;
import com.evan.dino.manager.GameManager;
import com.evan.dino.R;
import com.evan.dino.manager.TimerManager;
import com.evan.dino.manager.AnimationManager;
import com.evan.dino.manager.ObstacleManager;
import com.evan.dino.manager.SoundManager;
import com.evan.dino.task.RunTask;

import java.util.ArrayList;

public class GamingActivity extends AppCompatActivity {

    private ConstraintLayout constraintLayout;

    public static ImageView dinoImg;


    private ImageView heart1, heart2, heart3;

    private ImageView cloud1, cloud2;


    public static Dino dino;

    private int duration = DEFAULT_DURATION;

    public static TextView tv_score, tv_gameOver;

    private final ArrayList<ImageView> hearts = new ArrayList<>();

    private int width = 0;

    private CountDownTimer cdt;

    public static SoundManager soundManager;
    public static  AnimationManager animationManager;
    public static  BackgroundManager backgroundManager;
    private ObstacleManager obstacleManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaming);

        // Model
        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("mode").equals("light")) {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);

        } else if (bundle.getString("mode").equals("night")) {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
        }

        // Get Width //
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;

        // Music //
        // 初始化 SoundManager
        soundManager = new SoundManager(this);

        constraintLayout = findViewById(R.id.constraint_layout);
        dinoImg = findViewById(R.id.dino);
        obstacleManager = new ObstacleManager(findViewById(R.id.tree_one), findViewById(R.id.tree_two), findViewById(R.id.tree_three));
        tv_score = findViewById(R.id.score);
        tv_gameOver = findViewById(R.id.game_over);

        heart1 = findViewById(R.id.heart1);
        heart2 = findViewById(R.id.heart2);
        heart3 = findViewById(R.id.heart3);

        cloud1 = findViewById(R.id.cloud1);
        cloud2 = findViewById(R.id.cloud2);

        backgroundManager = new BackgroundManager(findViewById(R.id.background_one), findViewById(R.id.background_two));
        GameManager.setConstraintLayout(constraintLayout);

        hearts.add(heart1);
        hearts.add(heart2);
        hearts.add(heart3);

        dino = new Dino(dinoImg, hearts);
        dino.init();
        dino.setHeart(3);

        RunTask runTask = new RunTask(dinoImg);
        TimerManager.startRun(runTask);


        // 小恐龍點擊事件 //
        jumpClick();


        animationManager = new AnimationManager(width);
        // 背景移動 //
        animationManager.startGroundAnimation(backgroundManager.getBackgroundOne(), backgroundManager.getBackgroundTwo(), GROUND_MOVE_DURATION); // 設定地面動畫的持續時間，例如3000毫秒
        animationManager.startCloudAnimation(cloud1, cloud2, CLOUD_MOVE_DURATION);

        // 障礙移動與判定 //
        obstacleManager.startObstacleGeneration(width, duration); // 設定障礙物的寬度和持續時間，例如1000毫秒

        // 分數計算與加速 //
        countScore();

    }


    private void countScore() {
        cdt = new CountDownTimer(HUNDRED_THOUSAND, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 更新分數
                // 分數到一定程度 則加速
                if (isGameOver) {
                    this.cancel();
                    return;
                }

                tv_score.setText("" + (HUNDRED_THOUSAND - millisUntilFinished));
            }

            @Override
            public void onFinish() {
                // 破關
                tv_score.setText("" + (1000000));
            }
        };
        cdt.start();
    }


    // TODO 跳耀
    private void jumpClick() {
        GameManager.getConstraintLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGameOver) {
                    reStart();
                    return;
                }

                if (GameManager.isJump) {
                    return;
                }


                soundManager.playJumpSound();
                startJumpAnimation();
            }
        });
    }

    private void startJumpAnimation() {
        GameManager.isJump = true;
        ObjectAnimator jumpUp = ObjectAnimator.ofFloat(dinoImg, "translationY", JUMP_HEIGHT);
        jumpUp.setDuration(JUMP_DURATION);
        ObjectAnimator jumpDown = ObjectAnimator.ofFloat(dinoImg, "translationY", 0f);
        jumpDown.setDuration(JUMP_DURATION);
        AnimatorSet jumpSet = new AnimatorSet();
        jumpSet.playSequentially(jumpUp, jumpDown);
        jumpSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                GameManager.isJump = false;
            }
        });
        jumpSet.start();
    }


    private void reStart() {
        GameManager.restart(dino, dinoImg, hearts);

        duration = DEFAULT_DURATION;
        animationManager.resume();

        obstacleManager.reSetTree();

        tv_gameOver.setVisibility(INVISIBLE);

        // 障礙移動與判定 //
        obstacleManager.startObstacleGeneration(width, duration); // 設定障礙物的寬度和持續時間，例如1000毫秒

        cdt.cancel();
        countScore();

        GameManager.getJump_ani().end();

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundManager.release();
    }
}


