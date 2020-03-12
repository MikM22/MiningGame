package com.company.gameObjects;

import com.company.Loader;
import com.company.Main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Projectile extends GameObject {
    private double angle;
    private boolean fire;
    private int speed;
    private double x, y;

    public Projectile(int x, int y, double angle, BufferedImage img, boolean fire, int speed) {
        super(0,0);
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.fire = fire;
        this.img = img;
        this.speed = speed;
        shadow = false;
    }

    public void tick() {
        xVel = (float) (Math.cos(angle) * speed);
        yVel = (float) (Math.sin(angle) * speed);
        if (fire) {
            Main.room.addParticle((int)x + getBounds().width / 2, (int)y + getBounds().height / 2, 1, 1, 1, 1, .3f, .5f, 1f, false, false, false, 0, 0, 40, 80, Color.orange, Color.yellow, 5f);
            Main.room.addParticle((int)x + getBounds().width / 2, (int)y + getBounds().height / 2, 1, 1, 1, 1, .3f, .5f, 4, false, false, false, 0, 0, 6, 10, Color.red, Color.orange, .1f);
        }
        x += xVel;
        y += yVel;
    }

    public int getX() {
        return (int)x;
    }

    public int getY() {
        return (int)y;
    }

    public void render(Graphics2D g) {
        Loader.renderRotatedImage(g, g.getTransform(), angle, (int)x + getBounds().width / 2, (int)y + getBounds().height / 2, img, (int)x, (int)y);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, img.getWidth(), img.getHeight());
    }
}
