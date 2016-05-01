package com.main.game.objects.singleplayer;

import com.main.game.GamePanel;

public class You extends Player {

    private float dx = 0;

    public You() {
        setPosition(GamePanel.WIDTH / 2, GamePanel.HEIGHT - OFFSET);
    }

    public void update() {
        setPosition(getX() + dx, getY());
    }

    public void setDx(float dx) {
        this.dx = dx;
    }
}
