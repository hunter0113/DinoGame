package com.evan.dino.activiy;


import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;
import static com.evan.dino.GameManager.DEFAULT_DURATION;
import static com.evan.dino.GameManager.isGameOver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.transition.Scene;
import androidx.transition.Transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.airbnb.lottie.LottieAnimationView;
import com.evan.dino.Dino;
import com.evan.dino.GameManager;
import com.evan.dino.Point;
import com.evan.dino.R;
import com.evan.dino.Scope;
import com.evan.dino.TimerManager;
import com.evan.dino.manager.AnimationManager;
import com.evan.dino.manager.SoundManager;
import com.evan.dino.task.RunTask;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GamingActivity extends AppCompatActivity {
    private ImageView backgroundOne;
    private ImageView backgroundTwo;

    private ValueAnimator obs_ani;
    private ConstraintLayout constraintLayout;
    private FrameLayout frameLayout;

    private ImageView dino_Img;

    private ImageView tree1, tree2, tree3;

    private ImageView heart1, heart2, heart3;

    private ImageView cloud1, cloud2;

    private LottieAnimationView chrome;

    private SoundPool soundPool;

    private int jumpID, deathID, scoreID;

    private boolean flag1 = false;
    private boolean flag2 = false;
    private boolean flag3 = false;

    private Timer treeTimer;

    private Dino dino;

    private int duration = DEFAULT_DURATION;

    private TextView tv_score, tv_gameOver;

    private final ArrayList<ImageView> imageViews = new ArrayList<>();

    private int width, height = 0;

    private CountDownTimer cdt;

    private final int HUNDRED_THOUSAND = 100000;

    private ArrayList<Scope> InjuryRangeList;

    private final boolean testMode = false;
    private boolean isFirstJump = false;

    private TimerTask obstacleTask;

    private SoundManager soundManager;
    private AnimationManager animationManager;


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
        height = metric.heightPixels;

        // Music //
        // 初始化 SoundManager
        soundManager = new SoundManager(this);

        constraintLayout = findViewById(R.id.constraint_layout);
        frameLayout = findViewById(R.id.main_frame_layout);
        backgroundOne = findViewById(R.id.background_one);
        backgroundTwo = findViewById(R.id.background_two);
        dino_Img = findViewById(R.id.dino);
        tree1 = findViewById(R.id.tree_one);
        tree2 = findViewById(R.id.tree_two);
        tree3 = findViewById(R.id.tree_three);
        tv_score = findViewById(R.id.score);
        tv_gameOver = findViewById(R.id.game_over);

        heart1 = findViewById(R.id.heart1);
        heart2 = findViewById(R.id.heart2);
        heart3 = findViewById(R.id.heart3);

        cloud1 = findViewById(R.id.cloud1);
        cloud2 = findViewById(R.id.cloud2);
        chrome = findViewById(R.id.animation_chrome_view);

        GameManager.setConstraintLayout(constraintLayout);

        imageViews.add(heart1);
        imageViews.add(heart2);
        imageViews.add(heart3);

        dino = new Dino(dino_Img, imageViews);
        dino.init();
        dino.setHeart(3);

        RunTask runTask = new RunTask(dino_Img);
        TimerManager.startRun(runTask);

        // Point //
        Point point1 = new Point(5, 7);
        Point point2 = new Point(9, 9);
        Scope scope1 = new Scope(point1, point2);

        Point point3 = new Point(4, 1);
        Point point4 = new Point(7, 6);
        Scope scope2 = new Scope(point3, point4);

        InjuryRangeList = new ArrayList<>();
        InjuryRangeList.add(scope1);
        InjuryRangeList.add(scope2);


        // 小恐龍點擊事件 //
        jumpClick(this);


        animationManager = new AnimationManager(width);
        // 背景移動 //
        animationManager.startGroundAnimation(backgroundOne, backgroundTwo, 3000); // 設定地面動畫的持續時間，例如3000毫秒
        animationManager.startCloudAnimation(cloud1, cloud2);

        // 障礙移動與判定 //
        makeObstacle();

        // 分數計算與加速 //
        countScore();

    }


//    private void showBoss() {
//        boss_ani = ValueAnimator.ofInt(0, - ((width + 300) / 2) );
//        boss_ani.setInterpolator(new LinearInterpolator());
//        boss_ani.setDuration(6000);
//
//        boss_ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int currentValue = (Integer) animation.getAnimatedValue();
//                chrome.setTranslationX(currentValue);
//            }
//        });
//
//        boss_ani.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//
//                // 啟動攻擊
//                LottieAnimationView animationView;
//                animationView = findViewById(R.id.rocket);
//                animationView.setBackgroundColor(Color.parseColor("#00000000"));
//                animationView.setTranslationX(chrome.getX() + 200);
//                animationView.setTranslationY(chrome.getY() + 200);
//                animationView.setVisibility(VISIBLE);
//                animationView.playAnimation();
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {
//
//            }
//        });
//        boss_ani.start();
//    }

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

//                if (HUNDRED_THOUSAND - millisUntilFinished > 10000 && GameManager.step == 0) {
//                    speedUp();
//                    soundManager.playScoreSound();
//                }
//
//                if (HUNDRED_THOUSAND - millisUntilFinished > 20000 && GameManager.step == 1) {
//                    speedUp();
//                    soundManager.playScoreSound();
//                }
//
//                if (HUNDRED_THOUSAND - millisUntilFinished > 30000 && GameManager.step == 2) {
//                    speedUp();
//                    soundManager.playScoreSound();
//                }
//
//                if (HUNDRED_THOUSAND - millisUntilFinished > 40000 && GameManager.step == 3) {
//                    speedUp();
//                    soundManager.playScoreSound();
//                }
//
//                if (HUNDRED_THOUSAND - millisUntilFinished > 50000 && GameManager.step == 4) {
//                    speedUp();
//                    soundManager.playScoreSound();
//                }

            }

            @Override
            public void onFinish() {
                // 破關
                tv_score.setText("" + (1000000));
            }
        };
        cdt.start();
    }

    // TODO 障礙物移動
    private void makeObstacle() {
        obstacleTask = new TimerTask() {
            @Override
            public void run() {
                int number = new Random().nextInt(3) + 1;

                switch (number) {
                    case 1:
                        if (flag1) {
                            break;
                        }
                        moveObs(tree1, 1);
                        break;

                    case 2:
                        if (flag2) {
                            break;
                        }
                        moveObs(tree2, 2);
                        break;

                    case 3:
                        if (flag3) {
                            break;
                        }

                        moveObs(tree3, 3);
                        break;

                    default:
                        break;
                }
            }
        };

        treeTimer = new Timer();
        treeTimer.schedule(obstacleTask, 1000, 2000);
    }


    private void moveObstacle(final ImageView childImage, int flag) {

        Rect obsRect = new Rect();
        Rect dinoRect = new Rect();

        // TODO 障礙物移動
        obs_ani = ValueAnimator.ofInt(0, -width - childImage.getWidth());


        // TODO 觸碰判定
        obs_ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (Integer) animation.getAnimatedValue();
                childImage.setTranslationX(currentValue);

                // 無敵 //
                if (dino.getInvincible()) {
                    return;
                }

                childImage.getHitRect(obsRect);
                dino_Img.getHitRect(dinoRect);

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


                    if (Rect.intersects(obsRect, dinoRect) && !testMode) {
                        dino.hurtAnimation();
                    }

                    dino_Img.getHitRect(dinoRect);
                }


                if (isGameOver) {
                    animation.cancel();
                    GameOver();
                }
            }
        });

        obs_ani.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                GameManager.obstacle++;

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
                GameManager.obstacle--;

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

        // TODO 障礙物移動
        double real_width = (double) childImage.getWidth() / (double) width;
        obs_ani.setInterpolator(new LinearInterpolator());
        obs_ani.setDuration(Double.valueOf(duration * (1 + real_width)).longValue());
        obs_ani.start();

    }

    private void GameOver() {
        soundManager.playDeathSound();

        dino_Img.setImageResource(R.drawable.dino_6);
        tv_gameOver.setVisibility(VISIBLE);

        if (GameManager.getJump_ani().isRunning()) {
            GameManager.getJump_ani().cancel();
        }

        animationManager.pause();
        TimerManager.stopRun();

        if (null != treeTimer) {
            treeTimer.cancel();
        }
    }

    // TODO 跳耀
    private void jumpClick(Context context) {
        dino_Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Evan", "jumpClick");
                if (!isFirstJump) {
                    isFirstJump = true;
                    animationManager.pause();
//                    Scene secondScene = Scene.getSceneForLayout(frameLayout, R.layout.layout_second_scene, context);

//                    Transition.TransitionListener transitionListener = new Transition.TransitionListener() {
//                        @Override
//                        public void onTransitionStart(@NonNull Transition transition) {
//                            obstacleTask.cancel();
//
//                            constraintLayout = findViewById(R.id.constraint_layout);
//                            frameLayout = findViewById(R.id.main_frame_layout);
//                            backgroundOne = findViewById(R.id.background_one);
//                            backgroundTwo = findViewById(R.id.background_two);
//                            dino_Img = findViewById(R.id.dino);
//                            tv_score = findViewById(R.id.score);
//                            tv_gameOver = findViewById(R.id.game_over);
//
//                            heart1 = findViewById(R.id.heart1);
//                            heart2 = findViewById(R.id.heart2);
//                            heart3 = findViewById(R.id.heart3);
//
//                            chrome = findViewById(R.id.animation_chrome_view);
//
//                            GameManager.setConstraintLayout(constraintLayout);
//
//                            imageViews.add(heart1);
//                            imageViews.add(heart2);
//                            imageViews.add(heart3);
//
//                            dino = new Dino(dino_Img, imageViews);
//                            dino.init();
//                            dino.setHeart(3);
//
//                            RunTask runTask = new RunTask(dino_Img);
//                            TimerManager.startRun(runTask);
//
//                            // Point //
//                            Point point1 = new Point(5, 7);
//                            Point point2 = new Point(9, 9);
//                            Scope scope1 = new Scope(point1, point2);
//
//                            Point point3 = new Point(4, 1);
//                            Point point4 = new Point(7, 6);
//                            Scope scope2 = new Scope(point3, point4);
//
//                            InjuryRangeList = new ArrayList<>();
//                            InjuryRangeList.add(scope1);
//                            InjuryRangeList.add(scope2);
//
//                            // 小恐龍點擊事件 //
//                            jumpClick(context);
//
//                            // 背景移動 //
//                            groundMove();
//                        }
//
//                        @Override
//                        public void onTransitionEnd(@NonNull Transition transition) {
//                            Log.e("Evan", "onTransitionEnd");
//                            showBoss();
//                        }
//
//                        @Override
//                        public void onTransitionCancel(@NonNull Transition transition) {
//
//                        }
//
//                        @Override
//                        public void onTransitionPause(@NonNull Transition transition) {
//
//                        }
//
//                        @Override
//                        public void onTransitionResume(@NonNull Transition transition) {
//
//                        }
//                    };
                }

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
        ObjectAnimator jumpUp = ObjectAnimator.ofFloat(dino_Img, "translationY", -350f);
        jumpUp.setDuration(300);
        ObjectAnimator jumpDown = ObjectAnimator.ofFloat(dino_Img, "translationY", 0f);
        jumpDown.setDuration(300);
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


    private void moveObs(ImageView obstacleView, int flag) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                moveObstacle(obstacleView, flag);
            }
        });
    }

    private void reStart() {
        GameManager.restart(dino, dino_Img, imageViews);

        duration = DEFAULT_DURATION;
        animationManager.resume();

        tree1.setVisibility(INVISIBLE);
        tree2.setVisibility(INVISIBLE);
        tree3.setVisibility(INVISIBLE);

        tv_gameOver.setVisibility(INVISIBLE);

        makeObstacle();

        cdt.cancel();
        countScore();

        GameManager.getJump_ani().end();

    }

    // TODO 加速
    private void speedUp() {
        GameManager.step++;
        duration = (int) (duration * 0.8);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isGameOver) {
                    return;
                }
                animationManager.stop();
                animationManager.startCloudAnimation(cloud1,cloud2);
                animationManager.startGroundAnimation(backgroundOne, backgroundTwo, duration);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundManager.release();
    }
}


