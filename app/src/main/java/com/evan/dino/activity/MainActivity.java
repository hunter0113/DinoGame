package com.evan.dino.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.evan.dino.R;
import com.evan.dino.di.GameContainer;
import com.evan.dino.repository.GameRepository;
import com.evan.dino.utils.ExceptionHandler;

/**
 * 主活動
 * 遊戲的入口點，提供開始遊戲的功能
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    
    private Button startGameButton;
    private TextView highScoreTextView;
    private GameRepository gameRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExceptionHandler.safeExecute(() -> {
            initializeViews();
            initializeHighScore();
            setupClickListeners();
            Log.d(TAG, "MainActivity initialized successfully");
        }, "MainActivity onCreate");
    }
    
    /**
     * 初始化視圖元件
     */
    private void initializeViews() {
        startGameButton = findViewById(R.id.start_light);
        highScoreTextView = findViewById(R.id.high_score);
    }
    
    /**
     * 初始化最高分顯示
     */
    private void initializeHighScore() {
        ExceptionHandler.safeExecute(() -> {
            // 從依賴注入容器獲取 GameRepository
            GameContainer gameContainer = GameContainer.getInstance(this);
            gameRepository = gameContainer.getGameRepository();
            
            // 讀取並顯示最高分
            if (gameRepository != null && highScoreTextView != null) {
                long highScore = gameRepository.getHighScore();
                highScoreTextView.setText("歷史最高分: " + highScore);
                Log.d(TAG, "High score displayed: " + highScore);
            }
        }, "Initialize high score");
    }
    
    /**
     * 設置點擊監聽器
     */
    private void setupClickListeners() {
        if (startGameButton != null) {
            startGameButton.setOnClickListener(view -> {
                startGamingActivity();
            });
        }
    }
    
    /**
     * 開始遊戲活動
     */
    private void startGamingActivity() {
        ExceptionHandler.safeExecute(() -> {
            Intent intent = new Intent(this, GamingActivity.class);
            startActivity(intent);
            Log.d(TAG, "GamingActivity started");
        }, "Start gaming activity");
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // 當從遊戲返回時，更新最高分顯示
        ExceptionHandler.safeExecute(() -> {
            updateHighScoreDisplay();
        }, "Update high score on resume");
    }
    
    /**
     * 更新最高分顯示
     */
    private void updateHighScoreDisplay() {
        if (gameRepository != null && highScoreTextView != null) {
            long highScore = gameRepository.getHighScore();
            highScoreTextView.setText("歷史最高分: " + highScore);
            Log.d(TAG, "High score updated: " + highScore);
        }
    }
}