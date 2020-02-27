package com.company.gameObjects.entities;

import com.company.Loader;
import com.company.Main;
import com.company.Room;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Slime extends Entity {
    private final int maxHealth = 5;
    private int height, jumpTimer, flashTimer, knockbackTimer, health = maxHealth, KBMultiplier;
    private float scaleTime;
    private double sinCount, knockbackAngle, angle;
    private boolean jumping, flash, knockback;
    private static final BufferedImage[] slimeFrames = Loader.cutSpriteSheet("slime", 3, 1, Room.imageMult, 16, 16);
    private static final BufferedImage flashImg = Loader.colorImage(slimeFrames[2], Color.white), redImg = Loader.colorImage(slimeFrames[0], Color.red);

    public Slime(int x, int y) {
        super(x * Room.tw, y * Room.tw);
        img = slimeFrames[0];
    }

    public void tick() {
        checkCollision();
        if (flash) {
            if (health <= 0) {
                img = redImg;
            } else {
                img = flashImg;
            }
            if (flashTimer >= 5) {
                img = slimeFrames[2];
                flash = false;
                flashTimer = 0;
            }
            flashTimer++;
        }
        if (knockback) {
            if (health <= 0) {
                angle += (knockbackAngle > 0 ? 1 : -1) * .03;
                xVel = (int) (Math.sin(knockbackAngle) * 10 * KBMultiplier);
                yVel = (int) (Math.cos(knockbackAngle) * 10 * KBMultiplier);
                if (sinCount < Math.PI) {
                    sinCount += .1;
                    shadowMultiplier = (float) (-0.3 * Math.sin(sinCount) + 1);
                    height = (int) (Math.sin(sinCount) * 50);
                } else {
                    Main.room.enemies.remove(this);
                    Main.room.objects.remove(this);
                    Main.room.addParticle(x, y, 6, 20, .5f, 2f, .5f, 3, 1, true, false, knockbackAngle, Math.PI / 2);
                }
            } else {
                xVel = (int) (Math.sin(knockbackAngle) * 6);
                yVel = (int) (Math.cos(knockbackAngle) * 6);
            }
            if (knockbackTimer >= (health <= 0 ? 40 : 15)) {
                knockback = false;
                knockbackTimer = 0;
                img = slimeFrames[0];
            }
            knockbackTimer++;
        } else if (jumping) {
            sinCount += .1;
            shadowMultiplier = (float) (-0.3 * Math.sin(sinCount) + 1);
            height = (int) (Math.sin(sinCount) * 80);
            if (sinCount >= Math.PI) {
                sinCount = 0;
                height = 0;
                jumping = false;
                shadowMultiplier = 1;
                img = slimeFrames[0];
                xScale = 1.3f;
                yScale = 1 / 1.3f;
            }
            double angle = Math.atan2(Main.player.getY() - y, Main.player.getX() - x);
            xVel = (int) (Math.cos(angle) * 3);
            yVel = (int) (Math.sin(angle) * 3);
        } else {
            jumpTimer++;
            xVel = 0;
            yVel = 0;
            xScale = Loader.lerp(xScale, 1, .5f);
            yScale = Loader.lerp(yScale, 1, .5f);
            if (jumpTimer > 120) {
                xScale = 1;
                yScale = 1;
                scaleTime = 0;
                jumping = true;
                img = slimeFrames[1];
                jumpTimer = 0;
            } else if (jumpTimer > 100) {
                xScale = Loader.lerp(1, 1.3f, scaleTime);
                yScale = Loader.lerp(1, 1 / 1.3f, scaleTime);
                if (scaleTime < 1) {
                    scaleTime += 0.06f;
                } else {
                    scaleTime = 1;
                }
            }
        }
        checkCollision();
        x += xVel;
        y += yVel;
    }

    void hit(double angle, int damage, int mult) {
        xScale = 1/1.3f;
        yScale = 1.3f;
        jumpTimer = 0;
        health -= damage;
        KBMultiplier = mult;
        flash = true;
        knockback = true;
        knockbackTimer = 0;
        knockbackAngle = angle;
    }

    public void render(Graphics2D g) {
        Loader.renderRotatedScaledImage(g, g.getTransform(), x, y - height, this, img, angle, x + getBounds().width / 2, y + getBounds().height / 2);
    }

    public Rectangle getOffsetBoundsH() {
        return new Rectangle((int)(x + xVel), y, getBounds().width, getBounds().height);
    }

    public Rectangle getOffsetBoundsV() {
        return new Rectangle(x, (int)(y + yVel), getBounds().width, getBounds().height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, img.getWidth(), img.getHeight());
    }

    int getHeight() {
        return height;
    }
}