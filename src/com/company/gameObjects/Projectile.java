package com.company.gameObjects;

import com.company.Loader;
import com.company.Main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Projectile extends GameObject {
    private double angle;
    private boolean fire;
    private int speed;

    public Projectile(int x, int y, double angle, BufferedImage img, boolean fire, int speed) {
        super(x, y);
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
            Main.room.addParticle(x, y, 2, 1, .6f, 1, .3f, .5f, 4, true, false, true, 0, 0, 10, Color.red);
        }
        x += xVel;
        y += yVel;
    }

    public void render(Graphics2D g) {
        Loader.renderRotatedImage(g, g.getTransform(), angle, x, y, img, x, y);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, img.getWidth(), img.getHeight());
    }
}
