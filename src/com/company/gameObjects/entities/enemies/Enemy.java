package com.company.gameObjects.entities.enemies;

import com.company.Main;
import com.company.gameObjects.entities.Entity;

public abstract class Enemy extends Entity {
    private int timer = getDamageInterval();
    private boolean leftPlayer, inPlayer;

    public Enemy(int x, int y) {
        super(x, y);
    }

    protected abstract int getDamage();

    protected abstract int getDamageInterval();

    protected void checkPlayerCollision() {
        //fix
        if (getBounds().intersects(Main.player.getPartialBounds()) || leftPlayer) {
            if (!leftPlayer) {
                inPlayer = true;
            }
            timer++;
            if (timer > getDamageInterval()) {
                Main.player.hit(getDamage());
                leftPlayer = false;
                timer = 0;
            }
        } else {
            if (inPlayer) {
                leftPlayer = true;
                inPlayer = false;
            }
        }
    }

}
