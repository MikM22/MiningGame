package com.company.gameObjects;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {
    public int x,y;
    public float xVel = 0, yVel = 0;
    public float xScale = 1, yScale = 1;
    public BufferedImage img, flashImg, redImg;
    public float shadowMultiplier = 1;
    public boolean shadow = true;

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void tick();
    public abstract void render(Graphics2D g);
    public abstract Rectangle getBounds();

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public float getxScale() {
        return xScale;
    }
    public float getyScale() {
        return yScale;
    }
    public float getxVel() { return xVel; }
    public float getyVel() { return yVel; }
    public float getShadowMultiplier() { return shadowMultiplier; }
    public boolean hasShadow() {
        return shadow;
    }
}
