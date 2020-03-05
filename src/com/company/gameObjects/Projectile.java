package com.company.gameObjects;

import com.company.Loader;
import com.company.Main;
import com.company.gameArt.Door;
import com.company.gameArt.Tile;
import com.company.gameObjects.entities.Chicken;
import com.company.gameObjects.entities.Entity;
import com.company.gameObjects.entities.Slime;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;

public class Projectile extends GameObject {
    private double angle;
    private int speed;
    private float x, y;
    private int hitX, hitY;
    private int wallTimer, damage, bowPower;
    private Slime enemyToFollow;
    private boolean inWall, inEnemy, shouldTick = true;

    public Projectile(float x, float y, double angle, int speed, int damage, int bowPower, BufferedImage img) {
        super(0, 0);
        this.x = x;
        this.y = y;
        this.img = img;
        this.angle = angle;
        this.speed = speed;
        this.damage = damage * bowPower;
        this.bowPower = bowPower;
        shadow = false;
    }

    public void tick() {
        if (shouldTick) {
            if (inEnemy) {
                x = enemyToFollow.x - hitX;
                y = enemyToFollow.y - hitY - enemyToFollow.getHeight();
                if (enemyToFollow.isDead()) {
                    Main.room.objects.remove(this);
                    Main.room.addParticle((int)x, (int)y, 3, 10, .5f, .7f, .5f, 3, 1, true, false, true, 0, 0);
                }
            } else {
                xVel = (float) (Math.cos(angle) * speed);
                yVel = (float) (Math.sin(angle) * speed);
                checkCollision();
                for (Slime enemy : Main.room.enemies) {
                    if (getRotatedBounds().intersects(enemy.getBounds()) && enemy.getHeight() == 0) {
                        enemy.hit(Math.PI/2 - angle, damage, (bowPower - 1)/2f);
                    }
                }
                for (Chicken chicken : Main.room.chickens) {
                    if (getRotatedBounds().intersects(chicken.getBounds()) && chicken.getHeight() == 0) {
                        chicken.hit(0);
                    }
                }
                if (inWall) {
                    wallTimer++;
                    if (wallTimer > 2) {
                        xVel = 0;
                        yVel = 0;
                        shouldTick = false;
                    }
                }
                x += xVel;
                y += yVel;
            }
        }
    }

    public void render(Graphics2D g) {
        Loader.renderRotatedImage(g, g.getTransform(), angle + 3 * Math.PI / 4, (int)x, (int)y, img, (int)x, (int)y);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x - 8, (int)y, img.getWidth() - 20, img.getHeight());
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
                intersect();
            }
        }
        for (Door door : Main.room.doors) {
            if (getRotatedBounds().intersects(door.getBounds())) {
                intersect();
            }
        }
        for (Slime slime : Main.room.enemies) {
            if (getRotatedBounds().intersects(slime.getBounds()) && slime.getHeight() == 0) {
                if (speed > 25) {
                    Main.room.addParticle((int)x, (int)y, Loader.randomInt(6, 10), 3, .7f, 1, .5f, 4, 2, true, true, true, false, false, -Math.PI / 2 - angle, .5, Main.particles[3]);
                    Main.room.objects.remove(this);
                }
                inEnemy = true;
                hitX = slime.x - (int)x;
                hitY = slime.y - (int)y;
                enemyToFollow = slime;
            }
        }
        if (x < 0 || x > Main.room.mapX + 48 || y < 0 || y > Main.room.mapY + 48) {
            intersect();
        }
    }

    private void intersect() {
        if (speed > 25) {
            Main.room.addParticle((int)x, (int)y, Loader.randomInt(6, 10), 3, .7f, 1, .5f, 4, 2, true, true, true, false, false, -Math.PI / 2 - angle, .5, Main.particles[3]);
            Main.room.objects.remove(this);
        }
        inWall = true;
    }
}
