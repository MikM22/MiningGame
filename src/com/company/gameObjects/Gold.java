package com.company.gameObjects;

import com.company.Loader;
import com.company.Main;
import com.company.Room;
import com.company.rendering.Animation;
import com.company.rendering.UI;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class Gold extends GameObject {
    private static BufferedImage img = Loader.loadImage("gold", Room.imageMult);

    private double angle;
    private double x, y;
    private float time;
    private boolean following;

    private final int radius = Room.tw * 8;
    private Ellipse2D circle = new Ellipse2D.Float((int)x - radius / 2f, (int)y - radius / 2f, radius, radius);
    private double randAngle;
    private float acceleration = 5, speed = 1;
    private int height;
    private boolean falling = true;

    public Gold(int x, int y) {
        super(0, 0);
        this.x = x;
        this.y = y;
        shadow = false;
        randAngle = Loader.randomDouble(0, 2 * Math.PI);
    }

    public void tick() {
        final int spd = 15;
        if (!following && circle.intersects(Main.player.getBounds())) {
            following = true;
        }
        if (following) {
            time++;
            xScale = Loader.lerp(1, .3f, time / 5f);
            double dd = angleDifference(Math.atan2(Main.player.cy - y, Main.player.cx - x), angle);
            angle -= dd;
            xVel = (int) (Math.cos(angle) * spd);
            yVel = (int) (Math.sin(angle) * spd);
            x += xVel;
            y += yVel;
            if (getBounds().intersects(Main.player.getPartialBounds())) {
                Main.player.gold++;
                Main.room.objects.add(new DamageIndicator((int)x, (int)y, true));
                Main.room.addParticle((int)x, (int)y, 4, 10, .4f, .7f, 0, 0, 4, false, false, false, false, true, 0, 0, Main.particles[2]);
                Main.room.objects.remove(this);
                if (Main.player.gold < 1000) {
                    UI.goldString = "" + Main.player.gold;
                } else {
                    float n = Math.round(Main.player.gold / 100f) / 10f;
                    if ((int)n != n) {
                        UI.goldString = n + "k";
                    } else {
                        UI.goldString = (int)n + "k";
                    }
                }
            }
        } else if (falling) {
            x += speed * Math.cos(randAngle);
            y += speed * Math.sin(randAngle);
            circle = new Ellipse2D.Float((int)x - radius / 2f, (int)y - radius / 2f, radius, radius);
            height -= acceleration;
            acceleration -= .3f;
            if (height > 6 && acceleration < 0) {
                acceleration = 0;
                speed *= .95f;
            }
            if (speed < .05f) {
                falling = false;
            }
        }
    }

    public void render(Graphics2D g) {
        Loader.renderScaledImage(g, g.getTransform(), (int)x, (int)y + height, this, img);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, 4 * Room.imageMult, 4 * Room.imageMult);
    }

    public int getX() {
        return (int)x;
    }

    public int getY() {
        return (int)y;
    }

    private double angleDifference(double a1, double a2) {
        double diff = a2 - a1;
        if (Math.abs(diff) < 180) {
            return diff;
        } else {
            return diff + 360 * -diff;
        }
    }
}
