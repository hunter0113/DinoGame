package com.evan.dino.activity;


import static android.view.View.VISIBLE;

import static com.evan.dino.constants.Constants.CLOUD_MOVE_DURATION;
import static com.evan.dino.constants.Constants.GROUND_MOVE_DURATION;
import static com.evan.dino.constants.Constants.HUNDRED_THOUSAND;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.evan.dino.model.Dino;
import com.evan.dino.manager.BackgroundManager;
import com.evan.dino.manager.GameManager;
import com.evan.dino.R;
import com.evan.dino.manager.ActionTimerManager;
import com.evan.dino.manager.AnimationManager;
import com.evan.dino.manager.ObstacleManager;
import com.evan.dino.manager.SoundManager;
import com.evan.dino.task.RunTask;
import com.evan.dino.viewmodel.GamingViewModel;

import java.util.ArrayList;

import androidx.lifecycle.ViewModelProvider;

public class GamingActivity extends AppCompatActivity {

    private Dino dino;

    private CountDownTimer cdt;


    private TextView tv_score, tv_gameOver;

    private ConstraintLayout constraintLayout;

    private final ArrayList<ImageView> hearts = new ArrayList<>();

    private int width = 0;

    private SoundManager soundManager;
    private AnimationManager animationManager;
    private ObstacleManager obstacleManager;
    private GameManager gameManager;

    private ActionTimerManager timerManager = new ActionTimerManager();

    private GamingViewModel gamingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaming);

        // 初始化 ViewModel
        gamingViewModel = new ViewModelProvider(this).get(GamingViewModel.class);

        // 初始化視圖元件
        initViews();
        
        // 初始化遊戲管理器
        initGameManagers();
        
        // 設置所有的觀察者
        setupObservers();
        
        // 設置跳躍點擊事件
        setupJumpClickListener();
        
        // 開始背景動畫
        startBackgroundAnimations();
        
        // 開始障礙物生成
        obstacleManager.startObstacleGeneration(width);
        
        // 初始化分數計時器
        initCountDownTimer(gamingViewModel);
    }

    private void initViews() {
        constraintLayout = findViewById(R.id.constraint_layout);
        tv_score = findViewById(R.id.score);
        tv_gameOver = findViewById(R.id.game_over);

        ImageView heart1 = findViewById(R.id.heart1);
        ImageView heart2 = findViewById(R.id.heart2);
        ImageView heart3 = findViewById(R.id.heart3);

        hearts.add(heart1);
        hearts.add(heart2);
        hearts.add(heart3);

        // 獲取屏幕寬度
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;
        
        // 初始化恐龍角色
        dino = new Dino(findViewById(R.id.dino), gamingViewModel);
    }

    private void initGameManagers() {
        // 初始化背景管理器
        BackgroundManager backgroundManager = new BackgroundManager(
            findViewById(R.id.ground_one), 
            findViewById(R.id.ground_two),
            findViewById(R.id.cloud1),
            findViewById(R.id.cloud2),
            gamingViewModel
        );
        
        // 初始化動畫管理器
        animationManager = new AnimationManager(width);
        
        // 設置背景管理器觀察遊戲狀態
        backgroundManager.observeGameState(this, animationManager);
        
        // 初始化聲音管理器
        soundManager = new SoundManager(this);
        
        // 初始化計時器管理器
        timerManager = new ActionTimerManager();
        
        // 初始化遊戲管理器
        gameManager = new GameManager(gamingViewModel);
        gameManager.initJumpAnimation(dino, timerManager);
        
        // 開始跑步動畫
        RunTask runTask = new RunTask(dino.getDinoImageView());
        timerManager.startRun(runTask);
        
        // 初始化障礙物管理器
        obstacleManager = new ObstacleManager(dino, 
                                             findViewById(R.id.tree_one), 
                                             findViewById(R.id.tree_two), 
                                             findViewById(R.id.tree_three), 
                                             gamingViewModel, gameManager);
    }

    private void startBackgroundAnimations() {
        // 獲取背景元素
        ImageView groundOne = findViewById(R.id.ground_one);
        ImageView groundTwo = findViewById(R.id.ground_two);
        ImageView cloudOne = findViewById(R.id.cloud1);
        ImageView cloudTwo = findViewById(R.id.cloud2);
        
        // 開始背景動畫
        animationManager.startGroundAnimation(groundOne, groundTwo, GROUND_MOVE_DURATION);
        animationManager.startCloudAnimation(cloudOne, cloudTwo, CLOUD_MOVE_DURATION);
    }

    private void setupObservers() {
        // 觀察分數變化
        gamingViewModel.getScore().observe(this, score -> {
            tv_score.setText(String.valueOf(score));
        });

        // 觀察遊戲結束狀態
        gamingViewModel.isGameOver().observe(this, isGameOver -> {
            if (isGameOver) {
                handleGameOver();
            } else {
                tv_gameOver.setVisibility(View.INVISIBLE);
            }
        });

        // 觀察受傷動畫觸發
        gamingViewModel.shouldPlayHurtAnimation().observe(this, shouldPlay -> {
            if (shouldPlay) {
                dino.playHurtAnimation();
                gamingViewModel.setPlayHurtAnimation(false);
            }
        });

        // 觀察生命值變化
        gamingViewModel.getHeart().observe(this, heart -> {
            if (heart == 2) {
                hearts.get(0).setVisibility(View.INVISIBLE);
            } else if (heart == 1) {
                hearts.get(1).setVisibility(View.INVISIBLE);
            } else if (heart == 0) {
                hearts.get(2).setVisibility(View.INVISIBLE);
            }
        });
    }

    private void handleGameOver() {
        soundManager.playDeathSound();
        dino.getDinoImageView().setImageResource(R.drawable.dino_6);
        tv_gameOver.setVisibility(VISIBLE);

        if (gameManager.getJumpAnimator().isRunning()) {
            gameManager.getJumpAnimator().cancel();
        }

        timerManager.stopRun();
        cdt.cancel();
    }

    // 設置跳躍點擊事件
    private void setupJumpClickListener() {
        constraintLayout.setOnClickListener(view -> {
            // 檢查是否需要重新開始遊戲
            Boolean needRestartValue = gamingViewModel.needRestart().getValue();
            if (needRestartValue != null && needRestartValue) {
                reStart();
                return;
            }

            // 檢查是否已經在跳躍
            Boolean isJumpingValue = gamingViewModel.isJumping().getValue();
            if (isJumpingValue != null && isJumpingValue) {
                return;
            }

            // 播放跳躍音效
            soundManager.playJumpSound();
            
            // 開始跳躍
            gameManager.startJump();
        });
    }

    private void reStart() {
        gamingViewModel.setNeedRestart(false);
        gameManager.restart(dino, hearts, timerManager);
        animationManager.resume();
        obstacleManager.reSetTree();
        tv_gameOver.setVisibility(View.INVISIBLE);

        // 障礙移動與判定
        obstacleManager.startObstacleGeneration(width);

        cdt.start();

        gameManager.getJumpAnimator().end();
    }

    private void initCountDownTimer(GamingViewModel viewModel) {
        cdt = new CountDownTimer(HUNDRED_THOUSAND, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                viewModel.increaseScore(HUNDRED_THOUSAND - millisUntilFinished);
            }

            @Override
            public void onFinish() {
                // 無需操作
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundManager.release();
        animationManager.stop();
        if (cdt != null) {
            cdt.cancel();
        }
    }
}


