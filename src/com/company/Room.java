package com.company;

import com.company.gameArt.*;
import com.company.gameObjects.Gold;
import com.company.gameObjects.entities.Chicken;
import com.company.gameObjects.entities.Rat;
import com.company.gameObjects.entities.Slime;
import com.company.gameObjects.GameObject;
import com.company.gameObjects.Particle;
import com.company.gameObjects.entities.Player;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Room {
    public static Player player;
    public static final int tw = 48, imageMult = 3;
    public int xTiles, yTiles;
    public int mapX, mapY;

    public int arrowCount;

    public ArrayList<GameObject> objects = new ArrayList<>();
    public ArrayList<GameObject> frontObjects = new ArrayList<>();
    public ArrayList<GameArt> art = new ArrayList<>();
    public ArrayList<GameArt> frontArt = new ArrayList<>();
    public ArrayList<Slime> enemies = new ArrayList<>();
    public ArrayList<Point> rockSpots = new ArrayList<>();
    public ArrayList<Tile> walls = new ArrayList<>();
    public ArrayList<Door> doors = new ArrayList<>();
    public ArrayList<Tile> rocks = new ArrayList<>();
    public ArrayList<ItemSpot> itemSpots = new ArrayList<>();
    public ArrayList<Chicken> chickens = new ArrayList<>();

    public Room(int xTiles, int yTiles) {
        this.xTiles = xTiles;
        this.yTiles = yTiles;
        mapX = xTiles * tw;
        mapY = yTiles * tw;
    }

    public Room(Room room) {
        xTiles = room.xTiles;
        yTiles = room.yTiles;
        mapX = room.mapX;
        mapY = room.mapY;
        objects = new ArrayList<>(room.objects);
        art = new ArrayList<>(room.art);
        enemies = new ArrayList<>(room.enemies);
        walls = new ArrayList<>(room.walls);
        doors = new ArrayList<>(room.doors);
        rocks = new ArrayList<>(room.rocks);
        rockSpots = new ArrayList<>(room.rockSpots);
        frontArt = new ArrayList<>(room.frontArt);
        //not carrying over itemSpots, chickens, zOrderSwitchTiles because room 0 never cloned
    }

    void tick() {
        ArrayList<GameObject> objectsThisTick = new ArrayList<>(objects);
        for (GameObject gameObject : objectsThisTick) {
            gameObject.tick();
        }
        ArrayList<GameObject> frontObjectsThisTick = new ArrayList<>(frontObjects);
        for (GameObject gameObject : frontObjectsThisTick) {
            gameObject.tick();
        }
    }

    void render(Graphics2D g) {
        for (GameArt art : art) {
            art.render(g);
        }
        g.setColor(new Color(0, 0, 0, 60));
        for (GameObject obj : objects) {
            if (obj.hasShadow()) {
                g.fill(new Ellipse2D.Double(obj.getX() + obj.getBounds().width * (1 - obj.getShadowMultiplier()) / 2, obj.getY() + 3 * obj.getBounds().height / 4f + 3 * obj.getBounds().height / 4f * (1 - obj.getShadowMultiplier()) / 4, obj.getBounds().width * obj.getShadowMultiplier(), obj.getBounds().getHeight() / 2 * obj.getShadowMultiplier()));
            }
        }
        for (GameObject obj : objects) {
            obj.render(g);
        }
        for (GameArt art : frontArt) {
            art.render(g);
        }
        for (GameObject obj : frontObjects) {
            obj.render(g);
        }
    }



    public void addEnemy(Slime obj) {
        enemies.add(obj);
        objects.add(0, obj);
    }

    void addWall(Tile wall) {
        art.add(wall);
        walls.add(wall);
    }

    void addRockSpot(int x, int y, BufferedImage img) {
        art.add(new Tile(x, y, img));
        rockSpots.add(new Point(x, y));
    }

    void addDoor(Door door) {
        art.add(door);
        doors.add(door);
    }

    public void addRock(Tile rock) {
        art.add(rock);
        walls.add(rock);
        rocks.add(rock);
    }

    void addArt(GameArt obj) {
        art.add(obj);
    }

    public void addGold(int x, int y, int num) {
        for (int i = 0; i < num; i++)
            objects.add(new Gold(x + Loader.randomInt(-50, 50), y + Loader.randomInt(-50, 50)));
    }

    public void addChicken(Chicken chicken) {
        objects.add(chicken);
        chickens.add(chicken);
    }

    public void addRat(Rat rat) {
        objects.add(rat);
    }

    void addItemSpot(ItemSpot itemSpot) {
        itemSpots.add(itemSpot);
        frontArt.add(0, itemSpot);
    }

    public void addParticle(int x, int y, int num, int radius, float sizeMin, float sizeMax, float speedMin, float speedMax, float maxLifeTime, boolean particlesMove, boolean front, boolean randomAngle, double angle, double coneRadians) {
        if (front) {
            for (int i = 0; i < num; i++) {
                frontObjects.add(new Particle((int) (x + Loader.randomFloat(-1, 1) * radius), (int) (y + Loader.randomFloat(-1, 1) * radius), Loader.randomFloat(sizeMin, sizeMax), randomAngle ? Loader.randomInt(0, 360) : angle + Loader.randomDouble(-coneRadians, coneRadians), Loader.randomDouble(speedMin, speedMax), particlesMove, maxLifeTime, true));
            }
        } else {
            for (int i = 0; i < num; i++) {
                objects.add(0, new Particle((int) (x + Loader.randomFloat(-1, 1) * radius), (int) (y + Loader.randomFloat(-1, 1) * radius), Loader.randomFloat(sizeMin, sizeMax), randomAngle ? Loader.randomInt(0, 360) : angle + Loader.randomDouble(-coneRadians, coneRadians), Loader.randomDouble(speedMin, speedMax), particlesMove, maxLifeTime, false));
            }
        }
    }

    public void addParticle(int x, int y, int num, int radius, float sizeMin, float sizeMax, float speedMin, float speedMax, float maxLifeTime, boolean particlesMove, boolean gravity, boolean shadow, boolean randomAngle, boolean front, double angle, double coneRadians, BufferedImage img) {
        if (front) {
            for (int i = 0; i < num; i++) {
                frontObjects.add(new Particle((int) (x + Loader.randomFloat(-1, 1) * radius), (int) (y + Loader.randomFloat(-1, 1) * radius), Loader.randomFloat(sizeMin, sizeMax), randomAngle ? Loader.randomInt(0, 360) : angle + Loader.randomDouble(-coneRadians, coneRadians), Loader.randomDouble(speedMin, speedMax), particlesMove, gravity, shadow, maxLifeTime, true, img));
            }
        } else {
            for (int i = 0; i < num; i++) {
                objects.add(0, new Particle((int) (x + Loader.randomFloat(-1, 1) * radius), (int) (y + Loader.randomFloat(-1, 1) * radius), Loader.randomFloat(sizeMin, sizeMax), randomAngle ? Loader.randomInt(0, 360) : angle + Loader.randomDouble(-coneRadians, coneRadians), Loader.randomDouble(speedMin, speedMax), particlesMove, gravity, shadow, maxLifeTime, false, img));
            }
        }
    }
}
