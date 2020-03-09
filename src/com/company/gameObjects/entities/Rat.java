package com.company.gameObjects.entities;

import com.company.Loader;
import com.company.Main;
import com.company.Room;
import com.company.gameArt.Door;
import com.company.gameArt.Tile;
import com.company.rendering.Animation;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Rat extends Entity {
    private static BufferedImage[] imgs = Loader.cutSpriteSheet("rat", 3, 1, Room.imageMult, 16, 10);
    private Animation walkAnim = new Animation(10, new BufferedImage[]{imgs[1],imgs[2]}, true), idleAnim = new Animation(10, new BufferedImage[]{imgs[0]}, true), anim = idleAnim;
    private int roamTimer, roamTimerMax = Loader.randomInt(60, 300), timeRoaming, timeRoamingMax = Loader.randomInt(20, 70);
    private boolean roaming;
    private double angle;
    private float x, y;

    public Rat(int x, int y) {
        super(0, 0);
        this.x = x * Room.tw;
        this.y = y * Room.tw;
        img = idleAnim.getFirstImage();
    }

    public void tick() {
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
        anim.repeatAnimation();
    }

    public void render(Graphics2D g) {
        anim.drawAnimation(g, x, y, 0);
    }

    private Rectangle getIntersectionBounds() {
        return new Rectangle((int)x, (int) y, img.getWidth(), img.getHeight());
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x + 9, (int)y, 30, img.getHeight());
    }

    public Rectangle getOffsetBoundsH() {
        return new Rectangle((int)(x + xVel), (int)y, getIntersectionBounds().width, getIntersectionBounds().height);
    }

    public Rectangle getOffsetBoundsV() {
        return new Rectangle((int)x, (int)(y + yVel), getIntersectionBounds().width, getIntersectionBounds().height);
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
