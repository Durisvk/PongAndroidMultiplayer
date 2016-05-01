package com.main.game;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import com.main.game.objects.multiplayer.Player;
import com.main.game.objects.singleplayer.Ball;
import com.main.game.objects.singleplayer.Enemy;
import com.main.game.objects.singleplayer.Score;
import com.main.game.objects.singleplayer.You;


public class SinglePlayerGamePanel extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener, GamePanel {

    private static final int MOVEMENT_SPEED = 4;

    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;

    private static final int WAIT_TIME_AFTER_SCORE = 100;

    private static final int BACKGROUND_SPEED = -5;
    private static final String TAG = "GamePanel";

    private Background bg;
    private Ball ball;
    private You you;
    private Enemy enemy;
    private Score score;

    private int waitingTime;



    private GameThread thread;

    public SinglePlayerGamePanel(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new GameThread(getHolder(), this);

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.stars));
        bg.setSpeed(BACKGROUND_SPEED);

        ball = new Ball();

        ball.generateRandomMovement();

        you = new You();
        enemy = new Enemy(ball);

        score = new Score();

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        double dy = event.values[1];
        if(you != null)
            you.setDx((float)dy * MOVEMENT_SPEED);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void update() {

        bg.update();

        if(waitingTime > 0) {
            waitingTime--;
            return;
        }

        ball.update();
        you.update();
        enemy.update();

        ball.handleCollisionWithPlayer(you, false);
        ball.handleCollisionWithPlayer(enemy, true);

        if(!ball.isInVerticalBounds()) {
            handleScoreChange();
        }

    }

    public void handleScoreChange() {
        if(ball.getY() <= 0) {
            score.setScore(score.getScoreForYou(), score.getScoreForEnemy() + 1);
        } else if(ball.getY() >= HEIGHT) {
            score.setScore(score.getScoreForYou() + 1, score.getScoreForEnemy());
        }

        ball.setPosition(WIDTH/2, HEIGHT/2);
        you.setPosition(WIDTH / 2, HEIGHT - Player.OFFSET);
        enemy.setPosition(WIDTH / 2, Player.OFFSET);
        setWaitingTimeTo(WAIT_TIME_AFTER_SCORE);
    }

    public void setWaitingTimeTo(int gameCycles) {
        waitingTime = gameCycles;
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(canvas != null) {
            final int savedState = canvas.save();
            canvas.scale((float)getWidth()/WIDTH, (float)getHeight()/HEIGHT);

            bg.draw(canvas);
            ball.draw(canvas);
            you.draw(canvas);
            enemy.draw(canvas);
            score.draw(canvas);

            canvas.restoreToCount(savedState);
        }
    }
}
