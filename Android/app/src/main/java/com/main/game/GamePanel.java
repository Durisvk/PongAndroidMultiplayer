package com.main.game;


import android.graphics.Canvas;

public interface GamePanel {

    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;

    void draw(Canvas canvas);
    void update();

}
