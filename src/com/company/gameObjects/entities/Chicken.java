package com.company.gameObjects.entities;

import com.company.Loader;
import com.company.Main;
import com.company.Room;
import com.company.gameArt.Door;
import com.company.gameArt.Tile;
import com.company.rendering.Animation;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Chicken extends Entity {
    private static BufferedImage[] imgs = Loader.cutSpriteSheet("chicken", 2, 1, Room.imageMult, 16, 16);
    private Animation walkAnim = new Animation(4, imgs, true), idleAnim = new Animation(4, new BufferedImage[]{imgs[0]}, true);
    private Animation anim = idleAnim;
    private int roamTimer, roamTimerMax = Loader.randomInt(60, 300), timeRoaming, timeRoamingMax = Loader.randomInt(20, 70), knockbackTimer, height;
    private boolean roaming, knockback;
    private float x, y;
    private double angle, knockbackAngle, sinCount, spd = 8;

    public Chicken(int x, int y) {
        super(0, 0);
        this.x = x * Room.tw;
        this.y = y * Room.tw;
        img = anim.getFirstImage();
    }

    public void tick() {
        if (knockback) {
            anim = walkAnim;
            knockbackTimer++;
            xVel = (float) (Math.sin(knockbackAngle) * spd * Main.player.currentWeapon.KBMultiplier);
            yVel = (float) (Math.cos(knockbackAngle) * spd * Main.player.currentWeapon.KBMultiplier);
            spd -= 8/31d;
            checkCollision();
            x += xVel;
            y += yVel;
            if (sinCount < Math.PI) {
                sinCount += .1;
                shadowMultiplier = (float) (-0.3 * Math.sin(sinCount) + 1);
                height = (int) (Math.sin(sinCount) * 50);
            }
            if (knockbackTimer >= 31) {
                knockback = false;
                knockbackTimer = 0;
                sinCount = 0;
                height = 0;
                spd = 8;
            }
        } else {
            final int spd = 2;
            roamTimer++;
            if (roamTimer > roamTimerMax) {
                roamTimer = 0;
                roamTimerMax = Loader.randomInt(60, 300);
                roaming = true;
                angle = Loader.randomDouble(-Math.PI, Math.PI);
                if (-Math.PI / 2 < angle && angle < Math.PI / 2) {
                    walkAnim.setFlipped(true);
                    idleAnim.setFlipped(true);
                } else {
                    walkAnim.setFlipped(false);
                    idleAnim.setFlipped(false);
                }
                xVel = (float) (Math.cos(angle) * spd);
                yVel = (float) (Math.sin(angle) * spd);
            }
            if (roaming) {
                anim = walkAnim;
                timeRoaming++;
                checkCollision();
                x += xVel;
                y += yVel;
                if (timeRoaming > timeRoamingMax) {
                    timeRoaming = 0;
                    timeRoamingMax = Loader.randomInt(20, 70);
                    roaming = false;
                }
            } else {
                anim = idleAnim;
            }
        }
        anim.repeatAnimation();
    }

    public void render(Graphics2D g) {
        anim.drawAnimation(g, x, y - height, 0);
    }

    void hit(double angle) {
        knockback = true;
        knockbackTimer = 0;
        knockbackAngle = angle;
    }

    int getHeight() {
        return height;
    }

    public Rectangle getOffsetBoundsH() {
        return new Rectangle((int)(x + xVel), (int)y, getIntersectionBounds().width, getIntersectionBounds().height);
    }

    public Rectangle getOffsetBoundsV() {
        return new Rectangle((int)x, (int)(y + yVel), getIntersectionBounds().width, getIntersectionBounds().height);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x + (walkAnim.isFlipped() ? 3 : 9), (int)y + 12, img.getWidth() - 12, img.getHeight() - 12);
    }

    private Rectangle getIntersectionBounds() {
        return new Rectangle((int)x, (int)y, img.getWidth(), img.getHeight());
    }

    public int getX() {
        return getBounds().x;
    }

    public int getY() {
        return getBounds().y;
    }

    public void checkCollision() {
        for (Tile obj : Main.room.walls) {
            if (getOffsetBoundsH().intersects(obj.getBounds())) {
                if (xVel > 0) {
                    x = obj.x - getIntersectionBounds().width;
                } else if (xVel < 0) {
                    x = obj.x + obj.getBounds().width;
                }
                xVel = 0;
                angle = Math.PI - angle;
            }
            if (getOffsetBoundsV().intersects(obj.getBounds())) {
                if (yVel > 0) {
                    y = obj.y - getOffsetBoundsV().height;
                } else if (yVel < 0) {
                    y = obj.y + obj.getBounds().height;
                }
                yVel = 0;
                angle = Math.PI - angle;
            }
        }
        for (Door obj : Main.room.doors) {
            if (getOffsetBoundsH().intersects(obj.getBounds())) {
                if (xVel > 0) {
                    x = obj.x - getIntersectionBounds().width;
                } else if (xVel < 0) {
                    x = obj.x + obj.getBounds().width;
                }
                xVel = 0;
            }
            if (getOffsetBoundsV().intersects(obj.getBounds())) {
                if (yVel > 0) {
                    y = obj.y - getOffsetBoundsV().height;
                } else if (yVel < 0) {
                    y = obj.y + obj.getBounds().height;
                }
                yVel = 0;
            }
        }
        if (getOffsetBoundsH().x < 0) {
            x = 0;
            xVel = 0;
            angle = Math.PI - angle;
        }
        if (getOffsetBoundsH().x > Main.room.mapX) {
            x = Main.room.mapX;
            xVel = 0;
            angle = Math.PI - angle;
        }
        if (getOffsetBoundsV().y < 0) {
            y = 0;
            yVel = 0;
            angle = Math.PI - angle;
        }
        if (getOffsetBoundsV().y > Main.room.mapY) {
            y = Main.room.mapY;
            yVel = 0;
            angle = Math.PI - angle;
        }
    }
}
