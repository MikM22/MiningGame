package com.company.gameArt;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameArt {
    public int x,y;
    BufferedImage img;

    GameArt(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void render(Graphics g);

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
