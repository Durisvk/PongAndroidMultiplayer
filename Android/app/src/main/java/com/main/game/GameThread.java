package com.main.game;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by juraj on 26.04.16.
 */
public class GameThread extends Thread {

    public static final int FPS = 30;
    private static final int LOG_FPS_AFTER_FRAMES = 30;

    public static Canvas canvas;

    private double averageFPS;

    private SurfaceHolder holder;
    private GamePanel gamePanel;

    private boolean running;

    public GameThread(SurfaceHolder holder, GamePanel gamePanel) {
        this.holder = holder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        super.run();

        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = 1000/FPS;

        while(running) {
            startTime = System.nanoTime();
            canvas = null;

            try {
                canvas = holder.lockCanvas();

                synchronized (holder) {
                    gamePanel.update();
                    gamePanel.draw(canvas);
                }


            } catch(Exception e) {}
            finally {
                if(canvas != null) {
                    try {
                        holder.unlockCanvasAndPost(canvas);
                    } catch(Exception e) {}
                }
            }


            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;

            try {
                this.sleep(waitTime);
            } catch(Exception e) {}

            totalTime += System.nanoTime() - startTime;
            frameCount++;

            if(frameCount == LOG_FPS_AFTER_FRAMES) {
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
                Log.v("FPS", Double.toString(averageFPS));
            }

        }

    }

    @Override
    public void interrupt() {
        super.interrupt();

        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean value) {
        this.running = value;
    }
}
