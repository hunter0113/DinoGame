package com.evan.dino.activity;

import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.evan.dino.R;
import com.evan.dino.constants.GameConstants;
import com.evan.dino.di.GameContainer;
import com.evan.dino.manager.ActionTimerManager;
import com.evan.dino.manager.AnimationManager;
import com.evan.dino.manager.BackgroundManager;
import com.evan.dino.manager.GameManager;
import com.evan.dino.manager.GameStateManager;
import com.evan.dino.manager.ObstacleManager;
import com.evan.dino.manager.SoundManager;
import com.evan.dino.model.Dino;
import com.evan.dino.repository.GameRepository;
import com.evan.dino.utils.ExceptionHandler;
import com.evan.dino.utils.RunTask;
import com.evan.dino.viewmodel.GamingViewModel;

import java.util.ArrayList;

/**
 * 遊戲主活動
 * 負責管理遊戲的UI和用戶交互
 */
public class GamingActivity extends AppCompatActivity {
    private static final String TAG = "GamingActivity";

    // 遊戲組件
    private Dino dino;
    private CountDownTimer scoreTimer;
    private TextView scoreTextView, gameOverTextView, highScoreTextView;
    private ConstraintLayout gameLayout;
    private final ArrayList<ImageView> heartImageViews = new ArrayList<>();
    private int screenWidth = 0;

    // 管理器
    private SoundManager soundManager;
    private AnimationManager animationManager;
    private ObstacleManager obstacleManager;
    private GameManager gameManager;
    private ActionTimerManager actionTimerManager;
    private GameStateManager gameStateManager;
    private GameRepository gameRepository;

    // ViewModel
    private GamingViewModel gamingViewModel;
    
    // 依賴注入容器
    private GameContainer gameContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaming);

        ExceptionHandler.safeExecute(() -> {
            // 初始化依賴注入容器
            gameContainer = GameContainer.getInstance(this);
            
            // 初始化 ViewModel
            gamingViewModel = new ViewModelProvider(this).get(GamingViewModel.class);

            // 初始化視圖元件
            initializeViews();
            
            // 初始化遊戲管理器
            initializeGameManagers();
            
            // 設置觀察者
            setupObservers();
            
            // 設置跳躍點擊事件
            setupJumpClickListener();
            
            // 開始背景動畫
            startBackgroundAnimations();
            
            // 開始障礙物生成
            startObstacleGeneration();
            
            // 初始化分數計時器
            initializeScoreTimer();
            
            // 初始化生命值顯示
            Integer initialHeart = gamingViewModel.getHeart().getValue();
            if (initialHeart != null) {
                updateHeartsDisplay(initialHeart);
            }
            
            // 初始化最高分顯示
            initializeHighScore();
            
            Log.d(TAG, "GamingActivity initialized successfully");
        }, "GamingActivity onCreate");
    }

    /**
     * 初始化視圖元件
     */
    private void initializeViews() {
        gameLayout = findViewById(R.id.constraint_layout);
        scoreTextView = findViewById(R.id.score);
        gameOverTextView = findViewById(R.id.game_over);
        highScoreTextView = findViewById(R.id.high_score);

        ImageView heart1 = findViewById(R.id.heart1);
        ImageView heart2 = findViewById(R.id.heart2);
        ImageView heart3 = findViewById(R.id.heart3);

        heartImageViews.add(heart1);
        heartImageViews.add(heart2);
        heartImageViews.add(heart3);

        // 獲取屏幕寬度
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
    }

    /**
     * 初始化遊戲管理器
     */
    private void initializeGameManagers() {
        ExceptionHandler.safeExecute(() -> {
            // 從依賴注入容器獲取管理器
            soundManager = gameContainer.getSoundManager();
            animationManager = gameContainer.createAnimationManager(screenWidth);
            actionTimerManager = gameContainer.getActionTimerManager();
            gameRepository = gameContainer.getGameRepository();
            
            // 從依賴注入容器獲取遊戲狀態管理器
            gameStateManager = gameContainer.getGameStateManager();
            
            // 初始化恐龍角色
            dino = new Dino(findViewById(R.id.dino), gamingViewModel);
            
            // 創建遊戲管理器
            gameManager = gameContainer.createGameManager(gamingViewModel);
            gameManager.initJumpAnimation(dino, actionTimerManager);
            
            // 開始跑步動畫
            if (dino != null && dino.getDinoImageView() != null) {
                RunTask runTask = new RunTask(dino.getDinoImageView());
                actionTimerManager.startRun(runTask);
            }
            
            // 創建背景管理器
            ImageView groundOne = findViewById(R.id.ground_one);
            ImageView groundTwo = findViewById(R.id.ground_two);
            ImageView cloudOne = findViewById(R.id.cloud1);
            ImageView cloudTwo = findViewById(R.id.cloud2);
            
            BackgroundManager backgroundManager = gameContainer.createBackgroundManager(
                groundOne, groundTwo, cloudOne, cloudTwo, gamingViewModel);
            backgroundManager.observeGameState(this, animationManager);
            
            // 創建障礙物管理器
            ImageView treeOne = findViewById(R.id.tree_one);
            ImageView treeTwo = findViewById(R.id.tree_two);
            ImageView treeThree = findViewById(R.id.tree_three);
            
            obstacleManager = gameContainer.createObstacleManager(
                dino, treeOne, treeTwo, treeThree, gamingViewModel, gameManager);
            
            Log.d(TAG, "Game managers initialized successfully");
        }, "Initialize game managers");
    }

    /**
     * 開始背景動畫
     */
    private void startBackgroundAnimations() {
        ExceptionHandler.safeExecute(() -> {
            // 獲取背景元素
            ImageView groundOne = findViewById(R.id.ground_one);
            ImageView groundTwo = findViewById(R.id.ground_two);
            ImageView cloudOne = findViewById(R.id.cloud1);
            ImageView cloudTwo = findViewById(R.id.cloud2);
            
            // 開始背景動畫
            animationManager.startGroundAnimation(groundOne, groundTwo, 
                GameConstants.Animation.GROUND_MOVE_DURATION);
            animationManager.startCloudAnimation(cloudOne, cloudTwo, 
                GameConstants.Animation.CLOUD_MOVE_DURATION);
        }, "Start background animations");
    }
    
    /**
     * 開始障礙物生成
     */
    private void startObstacleGeneration() {
        ExceptionHandler.safeExecute(() -> {
            if (obstacleManager != null) {
                obstacleManager.startObstacleGeneration(screenWidth);
            }
        }, "Start obstacle generation");
    }

    /**
     * 設置觀察者
     */
    private void setupObservers() {
        ExceptionHandler.safeExecute(() -> {
            // 觀察分數變化
            gamingViewModel.getScore().observe(this, score -> {
                if (score != null && scoreTextView != null) {
                    runOnUiThread(() -> {
                        if (scoreTextView != null) {
                            scoreTextView.setText(String.valueOf(score));
                        }
                    });
                }
            });

            // 觀察遊戲結束狀態
            gamingViewModel.isGameOver().observe(this, isGameOver -> {
                if (isGameOver != null && isGameOver) {
                    handleGameOver();
                } else {
                    runOnUiThread(() -> {
                        if (gameOverTextView != null) {
                            gameOverTextView.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            });

            // 觀察受傷動畫觸發
            gamingViewModel.shouldPlayHurtAnimation().observe(this, shouldPlay -> {
                if (shouldPlay != null && shouldPlay) {
                    playHurtAnimation();
                    gamingViewModel.setPlayHurtAnimation(false);
                }
            });

            // 觀察生命值變化
            gamingViewModel.getHeart().observe(this, heart -> {
                if (heart != null) {
                    Log.d(TAG, "Heart observer triggered with value: " + heart);
                    updateHeartsDisplay(heart);
                } else {
                    Log.w(TAG, "Heart observer triggered with null value");
                }
            });

            // 觀察最高分變化
            gamingViewModel.getHighScore().observe(this, highScore -> {
                if (highScore != null && highScoreTextView != null) {
                    runOnUiThread(() -> {
                        if (highScoreTextView != null) {
                            highScoreTextView.setText("最高分: " + highScore);
                        }
                    });
                }
            });
        }, "Setup observers");
    }
    
    /**
     * 初始化最高分顯示
     */
    private void initializeHighScore() {
        ExceptionHandler.safeExecute(() -> {
            if (gameRepository != null && gamingViewModel != null) {
                long highScore = gameRepository.getHighScore();
                gamingViewModel.setHighScore(highScore);
                Log.d(TAG, "High score initialized: " + highScore);
            }
        }, "Initialize high score");
    }

    /**
     * 更新生命值顯示
     */
    private void updateHeartsDisplay(int heartCount) {
        ExceptionHandler.safeExecute(() -> {
            if (heartImageViews != null) {
                Log.d(TAG, "Updating hearts display: " + heartCount + " hearts");
                
                // 確保在主線程中更新UI
                runOnUiThread(() -> {
                    for (int i = 0; i < heartImageViews.size(); i++) {
                        ImageView heart = heartImageViews.get(i);
                        if (heart != null) {
                            boolean shouldBeVisible = i < heartCount;
                            heart.setVisibility(shouldBeVisible ? VISIBLE : View.GONE);
                            Log.d(TAG, "Heart " + (i + 1) + " visibility: " + shouldBeVisible);
                        }
                    }
                });
            }
        }, "Update hearts display");
    }
    
    /**
     * 播放受傷動畫
     */
    private void playHurtAnimation() {
        if (dino != null) {
            dino.playHurtAnimation();
        }
        if (soundManager != null && gameStateManager != null && gameStateManager.isSoundEnabled()) {
            soundManager.playDeathSound();
        }
    }

    /**
     * 處理遊戲結束
     */
    private void handleGameOver() {
        ExceptionHandler.safeExecute(() -> {
            // 播放死亡音效
            if (soundManager != null && gameStateManager != null && gameStateManager.isSoundEnabled()) {
                soundManager.playDeathSound();
            }
            
            // 設置恐龍死亡圖片
            if (dino != null && dino.getDinoImageView() != null) {
                runOnUiThread(() -> {
                    if (dino.getDinoImageView() != null) {
                        dino.getDinoImageView().setImageResource(R.drawable.dino_6);
                    }
                });
            }
            
            // 顯示遊戲結束文字
            if (gameOverTextView != null) {
                runOnUiThread(() -> {
                    if (gameOverTextView != null) {
                        gameOverTextView.setVisibility(VISIBLE);
                    }
                });
            }

            // 停止跳躍動畫
            if (gameManager != null) {
                gameManager.stopJump();
            }

            // 停止跑步動畫
            if (actionTimerManager != null) {
                actionTimerManager.stopRun();
            }
            
            // 停止分數計時器
            if (scoreTimer != null) {
                scoreTimer.cancel();
            }
            
            // 保存遊戲數據
            saveGameData();
            
            Log.d(TAG, "Game over handled");
        }, "Handle game over");
    }
    
    /**
     * 保存遊戲數據
     */
    private void saveGameData() {
        if (gameRepository != null && gamingViewModel != null) {
            Long currentScore = gamingViewModel.getScore().getValue();
            if (currentScore != null) {
                // 保存最高分
                gameRepository.saveHighScore(currentScore);
                // 更新 ViewModel 中的最高分（如果創建了新紀錄）
                long newHighScore = gameRepository.getHighScore();
                gamingViewModel.setHighScore(newHighScore);
                // 更新遊戲統計
                gameRepository.updateGameStats(currentScore);
                Log.d(TAG, "Game data saved. Current score: " + currentScore + ", High score: " + newHighScore);
            }
        }
    }

    /**
     * 設置跳躍點擊事件
     */
    private void setupJumpClickListener() {
        ExceptionHandler.safeExecute(() -> {
            if (gameLayout != null) {
                gameLayout.setOnClickListener(view -> {
                    handleGameClick();
                });
            }
        }, "Setup jump click listener");
    }
    
    /**
     * 處理遊戲點擊事件
     */
    private void handleGameClick() {
        ExceptionHandler.safeExecute(() -> {
            // 檢查是否需要重新開始遊戲
            Boolean needRestartValue = gamingViewModel.needRestart().getValue();
            if (needRestartValue != null && needRestartValue) {
                restartGame();
                return;
            }

            // 檢查是否已經在跳躍
            Boolean isJumpingValue = gamingViewModel.isJumping().getValue();
            if (isJumpingValue != null && isJumpingValue) {
                return;
            }

            // 播放跳躍音效
            if (soundManager != null && gameStateManager != null && gameStateManager.isSoundEnabled()) {
                soundManager.playJumpSound();
            }
            
            // 開始跳躍
            if (gameManager != null) {
                gameManager.startJump();
            }
        }, "Handle game click");
    }

    /**
     * 重新開始遊戲
     */
    private void restartGame() {
        ExceptionHandler.safeExecute(() -> {
            gamingViewModel.setNeedRestart(false);
            
            // 重置遊戲狀態
            if (gameManager != null) {
                gameManager.restart(dino, heartImageViews, actionTimerManager);
            }
            
            // 重置障礙物
            if (obstacleManager != null) {
                obstacleManager.resetObstacles();
                // 重新開始障礙物生成
                obstacleManager.startObstacleGeneration(screenWidth);
            }
            
            // 恢復動畫
            if (animationManager != null) {
                animationManager.resume();
            }
            
            // 隱藏遊戲結束文字
            if (gameOverTextView != null) {
                gameOverTextView.setVisibility(View.INVISIBLE);
            }
            
            // 重新開始分數計時器
            initializeScoreTimer();
            
            Log.d(TAG, "Game restarted");
        }, "Restart game");
    }

    /**
     * 初始化分數計時器
     */
    private void initializeScoreTimer() {
        ExceptionHandler.safeExecute(() -> {
            scoreTimer = new CountDownTimer(GameConstants.GameLogic.HUNDRED_THOUSAND, 1) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (gamingViewModel != null) {
                        gamingViewModel.increaseScore(GameConstants.GameLogic.HUNDRED_THOUSAND - millisUntilFinished);
                    }
                }

                @Override
                public void onFinish() {
                    // 計時器結束
                    Log.d(TAG, "Score timer finished");
                }
            };
            scoreTimer.start();
        }, "Initialize score timer");
    }

    @Override
    protected void onPause() {
        super.onPause();
        ExceptionHandler.safeExecute(() -> {
            // 暫停動畫
            if (animationManager != null) {
                animationManager.pause();
            }
            
            // 停止跑步動畫
            if (actionTimerManager != null) {
                actionTimerManager.stopRun();
            }
            
            Log.d(TAG, "GamingActivity paused");
        }, "GamingActivity onPause");
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        ExceptionHandler.safeExecute(() -> {
            // 恢復動畫（如果遊戲沒有結束）
            Boolean isGameOver = gamingViewModel != null ? gamingViewModel.isGameOver().getValue() : null;
            if (isGameOver == null || !isGameOver) {
                if (animationManager != null) {
                    animationManager.resume();
                }
                
                // 恢復跑步動畫
                if (dino != null && dino.getDinoImageView() != null && actionTimerManager != null) {
                    RunTask runTask = new RunTask(dino.getDinoImageView());
                    actionTimerManager.startRun(runTask);
                }
            }
            
            Log.d(TAG, "GamingActivity resumed");
        }, "GamingActivity onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        ExceptionHandler.safeExecute(() -> {
            // 停止分數計時器
            if (scoreTimer != null) {
                scoreTimer.cancel();
                scoreTimer = null;
            }
            
            // 清理音效管理器
            if (soundManager != null) {
                soundManager.release();
            }
            
            // 停止動畫管理器
            if (animationManager != null) {
                animationManager.stop();
            }
            
            // 清理遊戲管理器
            if (gameManager != null) {
                gameManager.cleanup();
                gameManager = null;
            }
            
            // 清理依賴注入容器
            if (gameContainer != null) {
                gameContainer.cleanup();
            }
            
            Log.d(TAG, "GamingActivity destroyed");
        }, "GamingActivity onDestroy");
    }
}


