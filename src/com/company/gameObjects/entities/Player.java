package com.company.gameObjects.entities;

import com.company.Main;
import com.company.Loader;
import com.company.Room;
import com.company.Weapon;
import com.company.gameArt.Door;
import com.company.gameArt.Tile;
import com.company.gameObjects.Projectile;
import com.company.rendering.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;

import static com.company.rendering.Display.mouseDown;

public class Player extends Entity {
    private float attackTime = 0;
    private int wx, wy, cx, cy;
    private final BufferedImage pickaxe = Loader.loadImage("pickaxe", Room.imageMult), pickaxeFlipped = Loader.flipped(pickaxe, true);
    private final BufferedImage rock = Loader.loadImage("rock", Room.imageMult);
    private final BufferedImage[] swordSliceImgs = Loader.cutSpriteSheet("swordSlice", 5, 1, 5, 32, 32);
    public final Animation swordSlice = new Animation(2, swordSliceImgs, false);
    private final PlayerAnimation playerAnimation;
    private double angle, sliceAngle, swordAngle, selectionAngle;
    private boolean mousePressed, attackLeft, mouseLeft, buffer;
    public boolean holdingWeapon = false;
    public int hpUpgrades = 1, manaUpgrades = 1, maxHP = 100, hp = maxHP, maxMP = 100, mp = maxMP, gold;
    public BufferedImage[] imgs = Loader.cutSpriteSheet("player", 4, 1, Room.imageMult, 16, 20);
    public Weapon currentWeapon = new Weapon(Main.items[0], 1, 1, 2, 1, 1);

    public Player(int x, int y) {
        super(x, y);
        img = imgs[0];
        playerAnimation = new PlayerAnimation(this);
        if (!holdingWeapon) {
            swordSlice.setScale(.6f, 1);
        }
    }

    public void tick() {
        final float spd = 4;
        xVel = Display.horizontalAxis() * spd;
        yVel = Display.verticalAxis() * spd;
        checkCollision();
        for (Door door : Main.room.doors) {
            if (getIntersectionBounds().intersects(door.getBounds())) {
                Main.onFirstFloor = false;
                Main.roomNum++;
                x = (Main.room.xTiles + 1) / 2 * Room.tw;
                y = (Main.room.yTiles + 1) / 2 * Room.tw;
                Main.room = Main.rooms.get(Main.roomNum);
                UI.doRoomTransition();
                Room room = new Room(Main.copyRooms[Loader.randomInt(0, 1)]);
                double prob = Main.roomNum * 20;
                if (prob > 75) {
                    prob = 75;
                }
                for (int i = 0; i < room.rockSpots.size(); i++) {
                    Point p = room.rockSpots.get(i);
                    if (Math.random() * 100 < prob) {
                        room.addRock(new Tile(p.x, p.y, rock));
                        room.rockSpots.remove(p);
                        i--;
                    }
                }
                if (room.rockSpots.size() > 0) {
                    Point p = room.rockSpots.get(Loader.randomInt(0, room.rockSpots.size() - 1));
                    room.addEnemy(new Slime(p.x, p.y));
                }
                Main.rooms.add(room);
            }
        }
        x += xVel;
        y += yVel;
        cx = x + getBounds().width / 2;
        cy = y + getBounds().height / 2;
        selectionAngle = Math.atan2(Display.mousePos.x - cx, Display.mousePos.y - cy);
        if (!currentWeapon.melee) {
            double angle = Math.PI / 2 - selectionAngle;
            swordAngle = -Math.PI / 4 - selectionAngle;
            wx = cx + (int) (30 * Math.cos(angle));
            wy = cy + (int) (30 * Math.sin(angle));
            if (mouseDown && mousePressed || buffer) {
                Main.room.objects.add(new Projectile(cx + (float) ((30 + Main.projectiles[0].getWidth()) * Math.cos(angle)), cy + (float) ((30 + Main.projectiles[0].getWidth()) * Math.sin(angle)), angle, currentWeapon.speed, Main.projectiles[0]));
            }
        }
        if (currentWeapon.melee || !holdingWeapon) {
            calculateSword();
            if (mouseDown && mousePressed || buffer) {
                if (!swordSlice.isRunning()) {
                    attackLeft = !attackLeft;
                    if (attackLeft) {
                        attackTime = 0;
                    }
                    swordSlice.flip();
                    swordSlice.restart();
                    if (buffer) {
                        buffer = false;
                        angle = selectionAngle;
                    }
                    if (holdingWeapon) {
                        for (Slime enemy : Main.room.enemies) {
                            if (getSliceBounds().intersects(enemy.getBounds()) && enemy.getHeight() == 0) {
                                enemy.hit(angle, currentWeapon.damage, currentWeapon.KBMultiplier);
                            }
                        }
                        for (Chicken chicken : Main.room.chickens) {
                            if (getSliceBounds().intersects(chicken.getBounds()) && chicken.getHeight() == 0) {
                                chicken.hit(angle);
                            }
                        }
                    } else {
                        for (int i = 0; i < Main.room.rocks.size(); i++) {
                            if (getSliceBounds().intersects(Main.room.rocks.get(i).getBounds())) {
                                Main.room.addParticle(Main.room.rocks.get(i).x + Room.tw / 2, Main.room.rocks.get(i).y + Room.tw / 2, Loader.randomInt(4, 8), 5, .4f, .8f, .5f, 3, 3, true, true, true, true, false, 0, Main.particles[1]);
                                Main.room.art.remove(Main.room.rocks.get(i));
                                Main.room.walls.remove(Main.room.rocks.get(i));
                                Main.room.rocks.remove(Main.room.rocks.get(i));
                                i--;
                            }
                        }
                    }
                } else {
                    buffer = true;
                }
            }
        }
        playerAnimation.repeatAnimation();
        mousePressed = !mouseDown;
    }

    public void render(Graphics2D g) {
        AffineTransform old = g.getTransform();
        if (holdingWeapon) {
            g.drawImage((playerAnimation.isFlipped() ? pickaxe : pickaxeFlipped), x + 15 * (playerAnimation.isFlipped() ? 1 : -1), y - 15, null);
        } else {
            g.drawImage((playerAnimation.isFlipped() ? currentWeapon.img : currentWeapon.imgFlipped), x + 15 * (playerAnimation.isFlipped() ? 1 : -1), y - 15, null);
        }
        BufferedImage img = holdingWeapon ? (angle > 0 ? currentWeapon.img : currentWeapon.imgFlipped) : (angle > 0 ? pickaxe : pickaxeFlipped);
        if (wy > y + getBounds().width / 2) {
            Loader.renderScaledAnimationTree(g, old, this, playerAnimation);
            Loader.renderRotatedScaledAnimation(g, old, sliceAngle, cx, cy, swordSlice, holdingWeapon ? x + 30 : x, y - 40);
            Loader.renderRotatedImage(g, old, swordAngle - attackTime, wx, wy, img, wx - 25, wy - 20);
        } else {
            Loader.renderRotatedScaledAnimation(g, old, sliceAngle, cx, cy, swordSlice, holdingWeapon ? x + 30 : x, y - 40);
            Loader.renderRotatedImage(g, old, swordAngle - attackTime, wx, wy, img, wx - 25, wy - 20);
            Loader.renderScaledAnimationTree(g, old, this, playerAnimation);
        }
    }

    public void setWeapon(Weapon weapon) {
        currentWeapon = weapon;
        swordSlice.setScale(weapon.xRangeScale, weapon.yRangeScale);
        swordSlice.setSpeed(weapon.attackTime);
    }

    public Rectangle getOffsetBoundsH() {
        return new Rectangle((int)(x + xVel), y, getIntersectionBounds().width, getIntersectionBounds().height);
    }

    public Rectangle getOffsetBoundsV() {
        return new Rectangle(x, (int)(y + yVel), getIntersectionBounds().width, getIntersectionBounds().height);
    }

    private Area getSliceBounds() {
        Area a = new Area(new Rectangle(x + 30, y - 20, (int)(32 * 5 * swordSlice.getxScale()), 20 * 5));
        AffineTransform af = new AffineTransform();
        af.rotate(sliceAngle, cx, cy);
        return a.createTransformedArea(af);
    }

    public boolean isAttacking() {
        return swordSlice.isRunning();
    }

    public Rectangle getIntersectionBounds() {
        return new Rectangle(x, y + 20, img.getWidth(), 40);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, img.getWidth(), img.getHeight());
    }

    public int getCx() {
        return cx;
    }

    public int getCy() {
        return cy;
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
        }
        if (getOffsetBoundsH().x > Main.room.mapX) {
            x = Main.room.mapX;
            xVel = 0;
        }

        if (getOffsetBoundsV().y < 0) {
            y = 0;
            yVel = 0;
        }
        if (getOffsetBoundsV().y > Main.room.mapY) {
            y = Main.room.mapY;
            yVel = 0;
        }
    }

    private void calculateSword() {
        if (!swordSlice.isRunning()) {
            angle = selectionAngle;
        }
        sliceAngle = Math.PI / 2 - angle;
        if (angle > 0) {
            swordAngle = 3 * Math.PI / 4 - angle;
            if (mouseLeft) {
                attackTime = -attackTime;
            }
            mouseLeft = false;
            swordSlice.setFlipped(!attackLeft);
        } else {
            swordAngle = 5 * Math.PI / 4 - angle;
            if (!mouseLeft) {
                attackTime = -attackTime;
            }
            mouseLeft = true;
            swordSlice.setFlipped(attackLeft);
        }
        if (swordSlice.isRunning()) {
            attackTime += (swordSlice.isFlipped() ? 1 : -1) * .75f;
            if (mouseLeft) {
                if (attackTime < 0) {
                    attackTime = 0;
                }
                if (attackTime > 3) {
                    attackTime = 3;
                }
            } else {
                if (attackTime < -3) {
                    attackTime = -3;
                }
                if (attackTime > 0) {
                    attackTime = 0;
                }
            }
        }
        wx = cx + (int) (40 * Math.sin((mouseLeft ? angle - Math.PI : angle) + attackTime + Math.PI / 2));
        wy = cy + (int) (40 * Math.cos((mouseLeft ? angle - Math.PI : angle) + attackTime + Math.PI / 2)) + 10;
        attackTime += (swordSlice.isFlipped() ? 1 : -1) * .75f;
        if (mouseLeft) {
            if (attackTime < 0) {
                attackTime = 0;
            }
            if (attackTime > 3) {
                attackTime = 3;
            }
        } else {
            if (attackTime < -3) {
                attackTime = -3;
            }
            if (attackTime > 0) {
                attackTime = 0;
            }
        }
        swordSlice.runAnimation();
    }
}
