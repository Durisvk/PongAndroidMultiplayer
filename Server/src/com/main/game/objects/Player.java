package com.main.game.objects;

import com.main.game.Game;

public class Player extends LimitedActor {

    public static final int OFFSET = 20;
    public static final int WIDTH = 200;
    public static final int HEIGHT = 20;

    private float dx;

    public Player() {
        setPosition(Game.WIDTH / 2);
    }

    public void update() {
        if(getX() <= Game.WIDTH && getX() >= 0)
            this.setPosition(getX() + dx);
        else {
            if(getX() >= Game.WIDTH)
                setPosition(getX() - 5);
            else if(getX() <= 0)
                setPosition(getX() + 5);
        }
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public float getTransformedX() {
        float dis;

        if(getX() >= Game.WIDTH/2) {
            dis = getX() - Game.WIDTH/2;
            return Game.WIDTH/2 - dis;
        } else {
            dis = Game.WIDTH/2 - getX();
            return dis + Game.WIDTH/2;
        }

    }

}
