package com.evan.dino.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.evan.dino.R;
import com.evan.dino.utils.ExceptionHandler;

/**
 * 主活動
 * 遊戲的入口點，提供開始遊戲的功能
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    
    private Button startGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExceptionHandler.safeExecute(() -> {
            initializeViews();
            setupClickListeners();
            Log.d(TAG, "MainActivity initialized successfully");
        }, "MainActivity onCreate");
    }
    
    /**
     * 初始化視圖元件
     */
    private void initializeViews() {
        startGameButton = findViewById(R.id.start_light);
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
}