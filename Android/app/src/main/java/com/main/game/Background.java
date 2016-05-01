package com.main.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;


public class Background {

    private Bitmap image;
    private int x = 0, y = 0, dy = 0;

    public Background(Bitmap img) {
        image = img;
    }

    public void update() {
        y += dy;
        if (y == MultiPlayerGamePanel.HEIGHT || y == -MultiPlayerGamePanel.HEIGHT)
            y = 0;
    }


    public void draw(Canvas canvas) {

        canvas.drawBitmap(image, x, y, null);
        if(y < 0) {
            canvas.drawBitmap(image, x, y + MultiPlayerGamePanel.HEIGHT, null);
        }

    }

    public void setSpeed(int dy) {
        this.dy = dy;
    }
}
