package com.evan.dino.utils;

import android.util.Log;

/**
 * 全局異常處理器
 * 統一處理遊戲中的異常情況
 */
public class ExceptionHandler {
    private static final String TAG = "DinoGame";
    
    /**
     * 處理一般異常
     */
    public static void handleException(Exception e, String context) {
        Log.e(TAG, "Exception in " + context + ": " + e.getMessage(), e);
        // 這裡可以添加錯誤報告機制
    }
    
    /**
     * 處理運行時異常
     */
    public static void handleRuntimeException(RuntimeException e, String context) {
        Log.e(TAG, "RuntimeException in " + context + ": " + e.getMessage(), e);
        // 這裡可以添加錯誤報告機制
    }
    
    /**
     * 安全執行任務，捕獲異常
     */
    public static void safeExecute(Runnable task, String context) {
        try {
            task.run();
        } catch (Exception e) {
            handleException(e, context);
        }
    }
    
    /**
     * 安全執行任務並返回結果
     */
    public static <T> T safeExecute(Supplier<T> supplier, T defaultValue, String context) {
        try {
            return supplier.get();
        } catch (Exception e) {
            handleException(e, context);
            return defaultValue;
        }
    }
    
    /**
     * 檢查對象是否為空
     */
    public static <T> T requireNonNull(T obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
        return obj;
    }
    
    /**
     * 檢查條件是否為真
     */
    public static void requireTrue(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }
    
    /**
     * 函數式接口：供應者
     */
    @FunctionalInterface
    public interface Supplier<T> {
        T get() throws Exception;
    }
} 