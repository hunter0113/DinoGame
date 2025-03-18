package com.evan.dino.manager;

import static android.view.View.VISIBLE;
import static com.evan.dino.constants.Constants.DEFAULT_DURATION;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.evan.dino.Dino;
import com.evan.dino.model.Point;
import com.evan.dino.model.Scope;
import com.evan.dino.viewmodel.GamingViewModel;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ObstacleManager {
    private Timer treeTimer;
    private ValueAnimator obstacleAnimation;
    private boolean flag1, flag2, flag3;

    private final ImageView tree1;
    private final ImageView tree2;
    private final ImageView tree3;

    private final Dino dino;

    private final ArrayList<Scope> InjuryRangeList;

    private final GamingViewModel viewModel;  // 替換GameStatusListener為ViewModel


    // Point //
    private final Point point1 = new Point(5, 7);
    private final Point point2 = new Point(9, 9);
    private final Scope scope1 = new Scope(point1, point2);

    private final Point point3 = new Point(4, 1);
    private final Point point4 = new Point(7, 6);
    private final Scope scope2 = new Scope(point3, point4);

    public void reSetTree() {
        tree1.setVisibility(VISIBLE);
        tree1.setTranslationX(0);
        tree2.setVisibility(VISIBLE);
        tree2.setTranslationX(0);
        tree3.setVisibility(VISIBLE);
        tree3.setTranslationX(0);
    }


    public ObstacleManager(Dino dino, ImageView tree1, ImageView tree2, ImageView tree3, GamingViewModel viewModel) {
        this.dino = dino;
        this.tree1 = tree1;
        this.tree2 = tree2;
        this.tree3 = tree3;
        this.viewModel = viewModel;

        InjuryRangeList = new ArrayList<>();
        InjuryRangeList.add(scope1);
        InjuryRangeList.add(scope2);
    }

    public void startObstacleGeneration(int width) {
        TimerTask obstacleTask = new TimerTask() {
            @Override
            public void run() {
                int number = new Random().nextInt(3) + 1;
                switch (number) {
                    case 1:
                        if (!flag1) moveObstacle(tree1, width, 1);
                        break;
                    case 2:
                        if (!flag2) moveObstacle(tree2, width, 2);
                        break;
                    case 3:
                        if (!flag3) moveObstacle(tree3, width, 3);
                        break;
                }
            }
        };

        treeTimer = new Timer();
        treeTimer.schedule(obstacleTask, 1000, 2000);
    }

    private void moveObstacle(final ImageView childImage, int width, int flag) {

        Rect obsRect = new Rect();
        Rect dinoRect = new Rect();

        // 障礙物移動
        obstacleAnimation = ValueAnimator.ofInt(0, -width - childImage.getWidth());


        // 觸碰判定
        obstacleAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (Integer) animation.getAnimatedValue();
                childImage.setTranslationX(currentValue);

                // 無敵 //
                if (dino.getInvincible()) {
                    return;
                }

                childImage.getHitRect(obsRect);
                dino.getDinoImageView().getHitRect(dinoRect);

                for (int i = 0; i < InjuryRangeList.size(); i++) {
                    int x1 = InjuryRangeList.get(i).getBLPoint().getX();
                    int y1 = InjuryRangeList.get(i).getBLPoint().getY();
                    int x2 = InjuryRangeList.get(i).getTRPoint().getX();
                    int y2 = InjuryRangeList.get(i).getTRPoint().getY();

                    int t1 = (int) (dinoRect.width() * x1 * 0.1);
                    int t2 = (int) (dinoRect.height() * y1 * 0.1);
                    int t3 = (int) (dinoRect.width() * (1 - (x2 * 0.1)));
                    int t4 = (int) (dinoRect.height() * (1 - (y2 * 0.1)));
                    dinoRect.set(dinoRect.left + t1,
                            dinoRect.top - t4,
                            dinoRect.right - t3,
                            dinoRect.bottom - t2);


                    if (Rect.intersects(obsRect, dinoRect)) {
                        viewModel.decreaseHeart();
                    }

                    dino.getDinoImageView().getHitRect(dinoRect);
                }


                // 使用ViewModel的遊戲狀態而不是靜態變數
                if (viewModel.isGameOver().getValue() != null && viewModel.isGameOver().getValue()) {
                    // 取消動畫
                    animation.cancel();
                    if (null != treeTimer) {
                        treeTimer.cancel();
                    }
                    
                    // 設置ViewModel的重啟標誌
                    viewModel.setNeedRestart(true);
                }
            }
        });

        obstacleAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                // 使用ViewModel增加障礙物計數
                viewModel.increaseObstacleCount();
                
                switch (flag) {
                    case 1:
                        tree1.setVisibility(VISIBLE);
                        flag1 = true;
                        break;

                    case 2:
                        tree2.setVisibility(VISIBLE);
                        flag2 = true;
                        break;

                    case 3:
                        tree3.setVisibility(VISIBLE);
                        flag3 = true;
                        break;
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                // 使用ViewModel減少障礙物計數
                viewModel.decreaseObstacleCount();
                
                switch (flag) {
                    case 1:
                        flag1 = false;
                        break;

                    case 2:
                        flag2 = false;
                        break;

                    case 3:
                        flag3 = false;
                        break;
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                switch (flag) {
                    case 1:
                        flag1 = false;
                        break;

                    case 2:
                        flag2 = false;
                        break;

                    case 3:
                        flag3 = false;
                        break;
                }
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        // 障礙物移動
        new Handler(Looper.getMainLooper()).post(() -> {
            double real_width = (double) childImage.getWidth() / (double) width;
            obstacleAnimation.setInterpolator(new LinearInterpolator());
            obstacleAnimation.setDuration(Double.valueOf(DEFAULT_DURATION * (1 + real_width)).longValue());
            obstacleAnimation.start();
        });
    }
}
