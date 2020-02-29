package com.company;

import com.company.gameArt.Door;
import com.company.gameArt.ItemSpot;
import com.company.gameArt.Tile;
import com.company.gameObjects.entities.Chicken;
import com.company.gameObjects.entities.Slime;
import com.company.gameObjects.entities.Player;
import com.company.rendering.Display;
import com.company.rendering.UI;
import com.company.rendering.Camera;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Main extends Canvas implements Runnable {
    private int fps, tps;
    private boolean running = false;
    public static boolean onFirstFloor = true;
    public static ArrayList<Integer> keysDown = new ArrayList<>();
    private Thread thread;
    private Display window;
    public static Room room;
    public static int roomNum;
    public static ArrayList<Room> rooms = new ArrayList<>();
    public static Room[] copyRooms = new Room[5];
    private ArrayList<Point> chimneyPoints = new ArrayList<>();
    private Camera camera;
    private UI ui;
    public MouseAdapter mouseAdapter;
    public static Player player;
    private boolean fullscreen;
    private final BufferedImage[] tiles = new BufferedImage[90];
    public static final BufferedImage[] particles = Loader.cutSpriteSheet("particles", 3, 1, Room.imageMult, 8, 8), items = Loader.cutSpriteSheet("items", 8, 2, Room.imageMult, 16, 16), projectiles = Loader.cutSpriteSheet("projectiles", 3, 1, Room.imageMult, 16, 16);
    private Weapon test = new Weapon(items[1], 2, .5f, 17, 5, 5);
    private Weapon rangeTest = new Weapon(items[7], projectiles[0], 17, 5, 5, 10, 10);

    //optimization idea: pass in the Affinetransform old instead of setting it so much

    public static void main(String[] args) {
        new Main();
    }

    private Main() {
        BufferedImage[] temp = Loader.cutSpriteSheet("tiles", 8, 10, Room.imageMult, 16, 16);
        System.arraycopy(temp, 0, tiles, 0, 80);
        tiles[81] = Loader.loadImage("house", Room.imageMult);
        tiles[82] = Loader.loadImage("farm", Room.imageMult);
        tiles[83] = Loader.loadImage("tree", Room.imageMult);
        tiles[84] = Loader.loadImage("wiztower", Room.imageMult);
        tiles[85] = Loader.loadImage("stall", Room.imageMult);
        rooms.add(new Room(25, 20));
        for (int i = 0; i < copyRooms.length; i++) {
            copyRooms[i] = new Room(25, 20);
        }
        window = new Display(1000, 800, this);
        room = new Room(0, 0);
        player = new Player(400, Display.height / 2);
        //Player lol = new Player(350, Display.height / 2);
        camera = new Camera();
        ui = new UI(this);
        Room.player = player;
//        for (int i = 0; i < 1000; i++) {
//            copyRooms[0].addEnemy(new Enemy(Loader.randomInt(0, Display.width), Loader.randomInt(0, Display.height)));
//        }

        loadRoom(rooms.get(0), "48f48f48f48,84f48f48f48f48f48,x49w48f48f48f48f48f40w40f40f40f40f40f40w48f48f48f48f48f48f48f48f48f48f48f48f48f48,x49w48f48,83f48f48f48f40w40f40f40f40f40f40w48f48f48f48f48f48,64f48f48f48f48f48f48f48f48,x49w48f48f48w48f48f40w40f40f40w40f40f40w48,85f48f48f48f48f48f48f48f48f48f48f48f48f48,x49w48f48f48w48f48f40w44f40w42d40w44f40w48f48w48w48w48f51f51f51f51f51f51f51f51f48,x50w48f48f48f48f48f40w40w40w41f40w40w40w48f48i48i48i48f48,54w48,54w48,54w54w54f54w48,54w48,54w48,x53w48f48f48f48f48f48,43f48,43f48,43f48f48,43f48,43f48,43f48f48f48f48f48f48,83f48f48f48w48d48w48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48,48w48f48w48f48w48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48w48frrr72f71fr72f48f48f48f48f48frrr52w51w51w51w51w51w51w51w51w51w51w51w51w51w51w48f48f48frrr72f71fr72f48f48f48f48f48fx55w48,54w48,54w48,54w48,54w48,54w48,54w48,54w48,54w48,54w48,54w48,54w48,54w48,54w48,54w48f48f48frrr72f70fr72f48f48f48f48f48f48,x55w48f48f48f48,81f48f48f48f48f48,81f48f48f48f48f48f48f48f48frrr72f71fr72f48f48f48f48,59f48f48,x55w48f48f48f48c48f48f48f48f48c48f48f48f48f48f51w51w51wrrr72f71fr72f51w51w51w51w51w48,x50w48f48,82f48f48w48w48w48w48f48w48w48w48w48f48f48,54w48,54w48,54wrrr72f71fr72f48,54w48,54w48,54w48,54w48,54w48,x53w48f48w48w48w48f48f48w48f48w48f48f48w48f48f48f48f48frrr72f71fr74f48f48f48f48f48f48f48f48w48w48w48w48f48w48f48w48w48f48w48f48,64w48,83f48f48frr73f71f71f68f67f48f48f48,64w48f48f48w48w48w48d48w48w48f48w48d48w48w48f48f48f48w48frr67frr68f71f71f68f67f48f48f48f48,62w48,63w48,63w48w48f48w48w48f48w48f48w48w48f48f48f48w48f48frr67frr68f71f71f68f67f48f48f48f48f48f48,58f48,65f48,60f48f48f48f48,65f48f48,66w48f48f48f48f48f48f48frr67frr68f71f71f68f72f72f72f72f72f72f72f72f72f72f72f72f72f72f72f72f48f48f48f48f48f48frr67frr68f71f71f71f71f71f71f71f71f71f71f71f70f71f71f71f71f71f71f48f48f48f48f48f48f48frr67frr69frr72frr72frr72frr72frr72frr72frr72frr72frr72frr72frr72frr72frr72frr72frr72frr72frr");
        rooms.get(0).addChicken(new Chicken(8, 8));
        rooms.get(0).objects.add(player);
        //rooms.get(0).addArt(new Tile(5, 1, tiles[31]));
        rooms.get(0).addDoor(new Door(5, 1));
        rooms.get(0).addChicken(new Chicken(5, 8));


        copyRooms[0].objects.add(player);
        loadRoom(copyRooms[0], "29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f35f8f9f10f11f8f9f10f11f30f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f36f12f13f14f15f12f13f14f15f31f8f9f10f11f30f29f29f29f29f29f29f29f29f29f29fx5f37w16w17w18w19w16w17w18w19w32w12f13f14f15f31f29f29f29f29f29f29f29f29f29f29fx6w0,38w0,20f0,21f0,22f0,23f0,20f0,21f0,22f0,23f0,33w16w17w18w19w32w29f29f29f29f29f29f29f29f29f29fx5w0,39f0p0p0p0p0p0p0p0p0,34f0,20f0,21f0,22f0,23f0,33w29w29f29f29f29f29f29f29f29f29fx4w0,x25p0p0p0p0p0p0p0p0p0p0p0p0p0p0,34f29w29f29f29f29f29f29f29f29f29fx3w0,x24p0,r24p0,r25p0,r26p0p0p0p0p0p0p0p0p0p0p0,27p6w29f29f29f29f29f29f29f29f29f29wr6wr3wr4wr5w0,r47f0p0p0p0p0p0p0p0p0p0,26p5w29f29f29f29f29f29f29f29f29f29f29f29f29f29w29w0,r47f0p0p0p0p0p0p0p0p0,25p4w29f29f29f29f29f29f29f29f29f29f29f29f29f29f29wx5w0,x26p0p0p0p0p0p0f0p0,24p3w29f29f29f29f29f29f29f29f29f29f29f29f29f29f29fx4w0,x25p0p0p0p0p0p0p0p0,27p30w29f29f29f29f29f29f29f29f29f29f11f30f29f29f29fx3w0,x24p0p0p0p0p0p0p0p0,26p31w30f29f29f29f29f29f29f29f29fx5f15f31f8f9f10f2w0,x27p0p0p0p0p0p0p0p0,25p32w31f8f9f10f11f30f29f29f29fx4w19w32w12f13f14f15w0,x26p0p0p0p0p0p0p0p0,24p0,33w32w12f13f14f15f31f30f29f29fx3w0,23f0,33w16w17w18w19w0,x25p0p0p0p0p0p0p0p0p0,34f0,33w16w17w18w19w32w31f29f29fx4w0,x25p0,34f0,20f0,21f0,22f0,23f0,x24p0p0p0p0p0p0p0p0p0p0,34f0,20f0,21f0,22f0,23f0,33w32w29f29fx3w0,x24p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0,34f0,33w29w29f29w0,r47f0,r24p0,r25p0,r26p0,r26p0,r27p0,r24p0,r25p0,r26p0,r27p0,r24p0,r25p0,r26p0,r27p0,r27p0p0p0p0p0p0p0p0,34f29w29f29w29wr3wr4wr6wr5wr4wr3wr4wr5wr6wr3wr4wr5wr5wr6w0,r47f0,r24p0,r25p0,r26p0,r27p0,r24p0,r25p0,r28p5w29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29w29wr3wr4wr5wr6wr3wr4wr3w29w29f");
        copyRooms[0].addDoor(new Door(5, 5));

        copyRooms[1].objects.add(player);
        loadRoom(copyRooms[1], "29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f35f8f9f10f11f30f29f29f29f29f29f29f29f29f29f35f8f9f10f11f30f29f29f29f29f35f36f12f13f14f15f31f30f29f29f29f29f29f29f29f35f36f12f13f14f15f31f8f9f10f11f36f37w16w17w18w19w32w31f30f29f29f29f29f29f29f36f37w16w17w18w19w32w12f13f14f15w37w0,38w0,20f0,21f0,22f0,23f0,33w32w31f30f29f29f29f29f29f37w0,38w0,20f0,21f0,22f0,23f0,33w16w17w18w19w0,38w0,39f0p0p0p0p0,34f0,33w32w31f29f29f29f29f29w0,38w0,39f0p0p0p0p0,34f0,20f0,21f0,22f0,23f0,39f0p0p0p0p0p0p0,34f0,33w32w29f29f29f29f29w0,39f0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0,34f0,33w29w29f29f29frr3w0,rr26p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0,34f29w29f29f29frr6w0,rr27p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0,26p3w29f29f29frr5w0,rr26p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0,27p4w29f29f29frr4w0,rr25p0p0p0p0p0p0p0p0p0p0f0p0p0p0p0p0p0p0p0p0,26p5w29f29f29frr3w0,rr24p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0,27p6w29f29f29f29w0,r47f0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0,26p3w29f29f29f29w29w0,r47f0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0,27p4w29f29f29f29f29w29w0,r47f0,r27p0,r26p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0,26p5w29f29f29f29f29f29w29wr4wr3w0,r47f0p0p0p0p0p0p0p0p0p0p0p0p0p0p0,27p6w29f29f29f29f29f29f29f29f29w29w0,r47f0p0p0p0p0p0p0p0p0p0p0p0p0p0,26p3w29f29f29f29f29f29f29f29f29f29wrr4w0,x27p0p0p0p0p0p0p0p0p0p0p0p0p0,47f29w29f29f29f29f29f29f29f29f29f29frr3w0,rr28p0,r26p0,r27p0,r26p0,r27p0,r26p0,r26p0,r26p0,r27p0,r26p0,r27p0,r24p0,47f29w29w29f29f29f29f29f29f29f29f29f29f29wr6wr5wr4wr3wr6wr5wr4wr3wr6wr5wr4wr3w29w29w29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f");
        copyRooms[1].addDoor(new Door(5, 5));

        rooms.add(new Room(copyRooms[Loader.randomInt(0,1)]));
//        rooms.add(new Room(copyRooms[Loader.randomInt(0,1)]));

        room = rooms.get(0);

        addMouseListener(mouseAdapter);
        addMouseWheelListener(e -> Camera.zoom -= e.getWheelRotation() * (1/3d));

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (!keysDown.contains(e.getKeyCode()) && (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_D)) {
                    keysDown.add(e.getKeyCode());
                }
                if (e.getKeyCode() == KeyEvent.VK_F) {
                    System.out.println("fps: " + fps + " ticks: " + tps);
                }
                if (e.getKeyCode() == KeyEvent.VK_H) {
                    Main.player.hp -= 5;
                }
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    Main.player.hpUpgrades++;
                }
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    fullscreen = !fullscreen;
                    window.setFullscreen(fullscreen);
                }
                if (e.getKeyCode() == KeyEvent.VK_E) {
                    for (ItemSpot itemSpot : room.itemSpots) {
                        if (player.getIntersectionBounds().intersects(itemSpot.getBounds())) {
                            player.setWeapon(itemSpot.weapon);
                            break;
                        }
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_V) {
                    player.swordSlice.setSpeed(100);
                }
                if (e.getKeyCode() == KeyEvent.VK_G) {
                    for (int i = 0; i < 1000; i++) {
                        Main.room.addGold(7, 7);
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_Q) {
                    if (player.state == 0) {
                        player.state = player.currentWeapon.type;
                    } else {
                        player.state = 0;
                    }
                    player.swordSlice.setScale(player.state != 0 ? player.currentWeapon.xRangeScale: .6f, player.state != 0 ? player.currentWeapon.yRangeScale: 1);
                    player.swordSlice.setSpeed(player.state != 0 ? player.currentWeapon.attackTime : 2);
                }
            }
            public void keyReleased(KeyEvent e) {
                keysDown.remove(Integer.valueOf(e.getKeyCode()));
            }
        });
        player.setWeapon(rangeTest);
        start();
    }

    private void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    private void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int smokeTimer, smokeTimerMax = Loader.randomInt(20, 40);
    private void tick() {
        room.tick();
        camera.tick();
        ui.tick();
        if (onFirstFloor) {
            smokeTimer++;
            if (smokeTimer > smokeTimerMax) {
                smokeTimer = 0;
                smokeTimerMax = Loader.randomInt(20, 40);
                for (Point p : chimneyPoints) {
                    rooms.get(0).addParticle(p.x * Room.tw + 10, p.y * Room.tw + 5, 2, 4, 1, 1.5f, 1f, 1.5f, 4, true, false, Math.PI, .2);
                }
            }
        }
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics2D g = (Graphics2D) bs.getDrawGraphics();
        Display.mousePos = new Point(MouseInfo.getPointerInfo().getLocation().x - window.frame.getLocationOnScreen().x + camera.x - 8, MouseInfo.getPointerInfo().getLocation().y - window.frame.getLocationOnScreen().y + camera.y - 31);
        AffineTransform old = g.getTransform();
        //DRAW THINGS HERE//
        g.setColor(new Color(20, 19, 39));
        g.fillRect(0, 0, Display.width, Display.height);
        g.translate((Display.width - Display.width * Camera.zoom) / 2, (Display.height - Display.height * Camera.zoom) / 2);
        g.scale(Camera.zoom, Camera.zoom);
        g.translate(-camera.x, -camera.y);
        room.render(g);
        g.setTransform(old);
        ui.render(g);
        g.translate(camera.x, camera.y);
        ////////////////////
        g.dispose();
        bs.show();
    }

    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double ns = 1000000000 / 60d;
        double nsRender = 1000000000 / 144d;
        double delta = 0;
        double renderDelta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0, ticks = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            renderDelta += (now - lastTime) / nsRender;
            lastTime = now;
            while(delta >= 1) {
                tick();
                ticks++;
                delta--;
            }
            while (renderDelta >= 1 && !window.transitioning) {
                render();
                frames++;
                renderDelta--;
            }
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                fps = frames;
                tps = ticks;
                frames = 0;
                ticks = 0;
            }
        }
        stop();
    }

    private void loadRoom(Room room1, String s) {
        StringBuilder tile = new StringBuilder();
        boolean flip = false;
        int numRotations = 0;
        int xPos = 0, yPos = 0;
        ArrayList<Tile> bigTiles = new ArrayList<>();
        Weapon[] shop = new Weapon[]{test, rangeTest, null};
        int tileNum = 0;

        for (int x = 0; x < s.length() ; x++) {
            if (isNumber(s.charAt(x))) {
                if (s.charAt(x) == ',') {
                    if (flip) {
                        room1.addArt(new Tile(xPos, yPos, Loader.flipped(tiles[Integer.parseInt(tile.toString())], true)));
                    } else {
                        room1.addArt(new Tile(xPos, yPos, tiles[Integer.parseInt(tile.toString())]));
                    }
                    flip = false;
                    tile.setLength(0);
                } else {
                    tile.append(s.charAt(x));
                }
            } else {
                if (s.charAt(x) == 'x') {
                    flip = true;
                } else if (s.charAt(x) == 'r') {
                    numRotations++;
                } else if (s.charAt(x) == 'f') {
                    if (flip) {
                        room1.addArt(new Tile(xPos, yPos, Loader.rotateImage(Loader.flipped(tiles[Integer.parseInt(tile.toString())], true), numRotations * 90)));
                    } else {
                        if (Integer.parseInt(tile.toString()) < 80)
                            room1.addArt(new Tile(xPos, yPos, Loader.rotateImage(tiles[Integer.parseInt(tile.toString())], numRotations * 90)));
                        else {
                            bigTiles.add(new Tile(xPos, yPos, Loader.rotateImage(tiles[Integer.parseInt(tile.toString())], numRotations * 90)));
                        }
                    }
                } else if (s.charAt(x) == 'w') {
                    if (flip) {
                        room1.addWall(new Tile(xPos, yPos, Loader.rotateImage(Loader.flipped(tiles[Integer.parseInt(tile.toString())], true), numRotations * 90)));
                    } else {
                        room1.addWall(new Tile(xPos, yPos, Loader.rotateImage(tiles[Integer.parseInt(tile.toString())], numRotations * 90)));
                    }
                } else if (s.charAt(x) == 'p') {
                    if (flip) {
                        room1.addRockSpot(xPos, yPos, Loader.rotateImage(Loader.flipped(tiles[Integer.parseInt(tile.toString())], true), numRotations * 90));
                    } else {
                        room1.addRockSpot(xPos, yPos, Loader.rotateImage(tiles[Integer.parseInt(tile.toString())], numRotations * 90));
                    }
                } else if (s.charAt(x) == 'c') {
                    chimneyPoints.add(new Point(xPos, yPos));
                    if (flip) {
                        room1.addArt(new Tile(xPos, yPos, Loader.rotateImage(Loader.flipped(tiles[Integer.parseInt(tile.toString())], true), numRotations * 90)));
                    } else {
                        room1.addArt(new Tile(xPos, yPos, Loader.rotateImage(tiles[Integer.parseInt(tile.toString())], numRotations * 90)));
                    }
                } else if (s.charAt(x) == 'd') {
                    room1.addDoor(new Door(xPos, yPos));
                    if (flip) {
                        room1.addArt(new Tile(xPos, yPos, Loader.rotateImage(Loader.flipped(tiles[Integer.parseInt(tile.toString())], true), numRotations * 90)));
                    } else {
                        room1.addArt(new Tile(xPos, yPos, Loader.rotateImage(tiles[Integer.parseInt(tile.toString())], numRotations * 90)));
                    }
                } else if (s.charAt(x) == 'b') {
                    if (flip) {
                        room1.addArt(new Tile(xPos, yPos, Loader.rotateImage(Loader.flipped(tiles[Integer.parseInt(tile.toString())], true), numRotations * 90)));
                    } else {
                        room1.addArt(new Tile(xPos, yPos, Loader.rotateImage(tiles[Integer.parseInt(tile.toString())], numRotations * 90)));
                    }
                } else if (s.charAt(x) == 'i') {
                    room1.addWall(new Tile(xPos, yPos, Loader.rotateImage(tiles[Integer.parseInt(tile.toString())], numRotations * 90)));
                    room1.addItemSpot(new ItemSpot(xPos, yPos, shop[tileNum]));
                    tileNum++;
                }
                if (s.charAt(x) != 'x' && s.charAt(x) != 'r') {
                    flip = false;
                    numRotations = 0;
                    xPos++;
                    if (xPos >= room1.xTiles + 1) {
                        xPos = 0;
                        yPos++;
                    }
                }
                tile.setLength(0);
            }
        }
        for (Tile t : bigTiles) {
            room1.addArt(t);
        }
    }

    private boolean isNumber(char c) {
        return !((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));
    }
}