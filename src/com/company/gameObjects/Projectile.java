package com.company.gameObjects;

import com.company.Loader;
import com.company.Main;
import com.company.gameArt.Tile;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;

public class Projectile extends GameObject {
    private double angle;
    private int speed;
    private float x, y;
    private int wallTimer;
    private boolean inWall, shouldTick = true;

    public Projectile(float x, float y, double angle, int speed, BufferedImage img) {
        super(0, 0);
        this.x = x;
        this.y = y;
        this.img = img;
        this.angle = angle;
        this.speed = speed;
        shadow = false;
    }

    public void tick() {
        if (shouldTick) {
            xVel = (float) (Math.cos(angle) * speed);
            yVel = (float) (Math.sin(angle) * speed);
            checkCollision();
            if (inWall) {
                wallTimer++;
                if (wallTimer > 5) {
                    xVel = 0;
                    yVel = 0;
                    shouldTick = false;
                }
            }
            x += xVel;
            y += yVel;
        }
    }

    public void render(Graphics2D g) {
        Loader.renderRotatedImage(g, g.getTransform(), angle + 3 * Math.PI / 4, (int)x, (int)y, img, (int)x, (int)y);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x - 12, (int)y, img.getWidth() - 30, img.getHeight());
    }

    private Area getRotatedBounds() {
        Area a = new Area(getBounds());
        AffineTransform af = new AffineTransform();
        af.rotate(angle + 2 * Math.PI / 4, x, y);
        return a.createTransformedArea(af);
    }

    private void checkCollision() {
        for (Tile wall : Main.room.walls) {
            if (getRotatedBounds().intersects(wall.getBounds())) {
                inWall = true;
            }
        }
    }
}
