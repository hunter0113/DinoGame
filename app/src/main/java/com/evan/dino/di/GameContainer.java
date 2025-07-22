package com.evan.dino.di;

import android.content.Context;

import com.evan.dino.manager.ActionTimerManager;
import com.evan.dino.manager.AnimationManager;
import com.evan.dino.manager.BackgroundManager;
import com.evan.dino.manager.GameManager;
import com.evan.dino.manager.GameStateManager;
import com.evan.dino.manager.ObstacleManager;
import com.evan.dino.manager.SoundManager;
import com.evan.dino.repository.GameRepository;
import com.evan.dino.utils.ExceptionHandler;
import com.evan.dino.viewmodel.GamingViewModel;
import com.evan.dino.model.Dino;
import android.widget.ImageView;
import android.util.Log;

/**
 * 遊戲依賴注入容器
 * 負責管理所有遊戲組件的生命週期和依賴關係
 */
public class GameContainer {
    private static GameContainer instance;
    private final Context context;
    
    // 管理器實例
    private GameRepository gameRepository;
    private SoundManager soundManager;
    private AnimationManager animationManager;
    private ObstacleManager obstacleManager;
    private BackgroundManager backgroundManager;
    private ActionTimerManager actionTimerManager;
    private GameStateManager gameStateManager;
    
    private GameContainer(Context context) {
        this.context = context.getApplicationContext();
    }
    
    public static synchronized GameContainer getInstance(Context context) {
        if (instance == null) {
            instance = new GameContainer(context);
        }
        return instance;
    }
    
    /**
     * 獲取遊戲數據倉庫
     */
    public GameRepository getGameRepository() {
        if (gameRepository == null) {
            gameRepository = new GameRepository(context);
        }
        return gameRepository;
    }
    
    /**
     * 獲取音效管理器
     */
    public SoundManager getSoundManager() {
        if (soundManager == null) {
            soundManager = new SoundManager(context);
        }
        return soundManager;
    }
    
    /**
     * 獲取動畫管理器
     * 注意：這個管理器需要screenWidth參數，應該使用createAnimationManager方法
     */
    public AnimationManager getAnimationManager() {
        return animationManager;
    }
    
    /**
     * 創建動畫管理器
     */
    public AnimationManager createAnimationManager(int screenWidth) {
        if (animationManager == null) {
            animationManager = new AnimationManager(screenWidth);
        }
        return animationManager;
    }
    
    /**
     * 獲取障礙物管理器
     * 注意：這個管理器需要特定的參數，應該使用createObstacleManager方法
     */
    public ObstacleManager getObstacleManager() {
        return obstacleManager;
    }
    
    /**
     * 創建障礙物管理器
     */
    public ObstacleManager createObstacleManager(Dino dino, ImageView tree1, ImageView tree2, ImageView tree3, 
                                                GamingViewModel viewModel, GameManager gameManager) {
        if (obstacleManager == null) {
            obstacleManager = new ObstacleManager(dino, tree1, tree2, tree3, viewModel, gameManager);
        }
        return obstacleManager;
    }
    
    /**
     * 獲取背景管理器
     * 注意：這個管理器需要特定的參數，應該使用createBackgroundManager方法
     */
    public BackgroundManager getBackgroundManager() {
        return backgroundManager;
    }
    
    /**
     * 創建背景管理器
     */
    public BackgroundManager createBackgroundManager(ImageView groundOne, ImageView groundTwo, 
                                                    ImageView cloudOne, ImageView cloudTwo, 
                                                    GamingViewModel viewModel) {
        if (backgroundManager == null) {
            backgroundManager = new BackgroundManager(groundOne, groundTwo, cloudOne, cloudTwo, viewModel);
        }
        return backgroundManager;
    }
    
    /**
     * 獲取動作計時器管理器
     */
    public ActionTimerManager getActionTimerManager() {
        if (actionTimerManager == null) {
            actionTimerManager = new ActionTimerManager();
        }
        return actionTimerManager;
    }
    
    /**
     * 獲取遊戲狀態管理器
     */
    public GameStateManager getGameStateManager() {
        if (gameStateManager == null) {
            gameStateManager = new GameStateManager(context);
        }
        return gameStateManager;
    }
    
    /**
     * 創建遊戲管理器
     */
    public GameManager createGameManager(GamingViewModel viewModel) {
        return new GameManager(viewModel);
    }
    
    /**
     * 清理資源
     */
    public void cleanup() {
        ExceptionHandler.safeExecute(() -> {
            if (soundManager != null) {
                soundManager.release();
                soundManager = null;
            }
            
            if (animationManager != null) {
                animationManager.cleanup();
                animationManager = null;
            }
            
            if (obstacleManager != null) {
                obstacleManager.cleanup();
                obstacleManager = null;
            }
            
            if (backgroundManager != null) {
                backgroundManager.cleanup();
                backgroundManager = null;
            }
            
            if (actionTimerManager != null) {
                actionTimerManager.cleanup();
                actionTimerManager = null;
            }
            
            if (gameStateManager != null) {
                gameStateManager.cleanup();
                gameStateManager = null;
            }
            
            Log.d("GameContainer", "All managers cleaned up");
        }, "GameContainer cleanup");
    }
} 