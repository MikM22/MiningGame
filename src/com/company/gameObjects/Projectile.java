package com.company.gameObjects;

import com.company.Loader;
import com.company.Main;
import com.company.gameArt.Door;
import com.company.gameArt.Tile;
import com.company.gameObjects.entities.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Projectile extends GameObject {
    private double angle;
    private int speed;
    private float x, y;

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
        xVel = (float) (Math.cos(angle) * speed);
        yVel = (float) (Math.sin(angle) * speed);
        checkCollision();
        x += xVel;
        y += yVel;
    }

    public void render(Graphics2D g) {
        Loader.renderRotatedImage(g, g.getTransform(), angle + 3 * Math.PI / 4, (int)x, (int)y, img, (int)x, (int)y);
        g.setColor(Color.red);
        g.fill(getBounds());
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, img.getWidth(), img.getHeight());
    }

    public Rectangle getOffsetBoundsH() {
        return new Rectangle((int)(x + xVel), (int)y, getBounds().width, getBounds().height);
    }

    public Rectangle getOffsetBoundsV() {
        return new Rectangle((int)x, (int)(y + yVel), getBounds().width, getBounds().height);
    }

    public void checkCollision() {
        for (Tile obj : Main.room.walls) {
            if (getOffsetBoundsH().intersects(obj.getBounds())) {
                if (xVel > 0) {
                    x = obj.x - getBounds().width;
                } else if (xVel < 0) {
                    x = obj.x + obj.getBounds().width;
                }
                xVel = 0;
                yVel = 0;
            }
            if (getOffsetBoundsV().intersects(obj.getBounds())) {
                if (yVel > 0) {
                    y = obj.y - getOffsetBoundsV().height;
                } else if (yVel < 0) {
                    y = obj.y + obj.getBounds().height;
                }
                xVel = 0;
                yVel = 0;
            }
        }
        for (Door obj : Main.room.doors) {
            if (getOffsetBoundsH().intersects(obj.getBounds())) {
                if (xVel > 0) {
                    x = obj.x - getBounds().width;
                } else if (xVel < 0) {
                    x = obj.x + obj.getBounds().width;
                }
                xVel = 0;
                yVel = 0;
            }
            if (getOffsetBoundsV().intersects(obj.getBounds())) {
                if (yVel > 0) {
                    y = obj.y - getOffsetBoundsV().height;
                } else if (yVel < 0) {
                    y = obj.y + obj.getBounds().height;
                }
                xVel = 0;
                yVel = 0;
            }
        }
        if (getOffsetBoundsH().x < 0) {
            x = 0;
            xVel = 0;
            yVel = 0;
        }
        if (getOffsetBoundsH().x > Main.room.mapX) {
            x = Main.room.mapX;
            xVel = 0;
            yVel = 0;
        }

        if (getOffsetBoundsV().y < 0) {
            y = 0;
            xVel = 0;
            yVel = 0;
        }
        if (getOffsetBoundsV().y > Main.room.mapY) {
            y = Main.room.mapY;
            xVel = 0;
            yVel = 0;
        }
    }
}
