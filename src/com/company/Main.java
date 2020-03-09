package com.company;

import com.company.gameArt.DestructibleTile;
import com.company.gameArt.Door;
import com.company.gameArt.ItemSpot;
import com.company.gameArt.Tile;
import com.company.gameObjects.Particle;
import com.company.gameObjects.entities.Chicken;
import com.company.gameObjects.entities.Rat;
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
    private final BufferedImage[] tiles = new BufferedImage[210];
    public static final int roomsIveCompleted = 1;
    public static final BufferedImage[] particles = Loader.cutSpriteSheet("particles", 5, 1, Room.imageMult, 8, 8), projectiles = Loader.cutSpriteSheet("projectiles", 3, 1, 2, 16, 16);
    private static final BufferedImage[] items = Loader.cutSpriteSheet("items", 8, 2, Room.imageMult, 16, 16);
    private Weapon test = new Weapon(items[1], 2f, .5f, 1, 5, 5);
    private Weapon og = new Weapon(Main.items[0], 1, 1, 2, 1, 1);
    private Weapon rangeTest = new Weapon(items[7], projectiles[0], 25, 1, 10, 10);
    private Weapon magicTest = new Weapon(items[15], projectiles[1], 25, 3, 10, 10, null);

    private Dimension[] tileSizes = new Dimension[5];

    //optimization idea: pass in the Affinetransform old instead of setting it so much

    public static void main(String[] args) {
        new Main();
    }

    private Main() {
        BufferedImage[] temp = Loader.cutSpriteSheet("tiles", 8, 10, Room.imageMult, 16, 16);
        System.arraycopy(temp, 0, tiles, 0, 80);
        tiles[81] = Loader.loadImage("house", Room.imageMult);;
        tiles[82] = Loader.loadImage("farm", Room.imageMult);
        tiles[83] = Loader.loadImage("tree", Room.imageMult);;
        tiles[84] = Loader.loadImage("wiztower", Room.imageMult);
        tiles[85] = Loader.loadImage("stall", Room.imageMult);

        tiles[86] = Loader.loadImage("houseRoof", Room.imageMult);
        tiles[87] = Loader.loadImage("stallRoof", Room.imageMult);
        tiles[88] = Loader.loadImage("tree", Room.imageMult);
        tiles[89] = Loader.loadImage("tp", Room.imageMult);

        int z = 0;
        tileSizes[0] = new Dimension(4, 4);
        tileSizes[1] = new Dimension(2, 4);
        tileSizes[2] = new Dimension(3, 3);
        tileSizes[3] = new Dimension(3, 8);
        tileSizes[4] = new Dimension(5, 2);

        for (int i = 0; i < tileSizes.length; i++) {
            for (int x = 0; x < tileSizes[i].width; x++) {
                for (int y = 0; y < tileSizes[i].height; y++) {
                    tiles[120 + z] = tiles[81 + i].getSubimage(x * 48, y * 48, 48, 48);
                    z++;
                }
            }
        }

        rooms.add(new Room(25, 20));
        for (int i = 0; i < 2; i++) {
            copyRooms[i] = new Room(25, 20);
        }
//        copyRooms[2] = new Room(40, 35);
//        copyRooms[3] = new Room(25, 20);
//        copyRooms[4] = new Room(25, 20);
        window = new Display(1000, 800, this);
        room = new Room(0, 0);
        player = new Player(700, 300);
        //Player lol = new Player(350, Display.height / 2);
        camera = new Camera();
        ui = new UI(this);
        Room.player = player;
//        for (int i = 0; i < 1000; i++) {
//            rooms.get(0).addEnemy(new Slime(Loader.randomInt(0, 20), Loader.randomInt(0, 20)));
//        }

        //48f48f48f48,165f48,173f48,181f48f48f48,x49w48f48f48f48f48f40w40f40f40f40f40f40w48f48f48f48f48f48f48f48f48,166f48,174f48,182f48f48f48,x49w48f48,156b48,159b48,162b48f40w40f40f40f40f40f40w48f48f48f48f48f48,64f48f48f48,167f48,175f48,183f48f48f48,x49w48f48,157b48,160y48,163b48f40w40f40f40w40f40f40w48,189b48,193b48,197b48,201b48,205b48f48f48f48,168f48,176f48,184f48f48f48,x49w48f48,158b48,161w48,164b48f40w44f40w42d40w44f40w48,190b48,194y48,198y48,202y48,206b51f51f51f51,169f51,177f51,185f51f51f48,x50w48f48f48f48f48f40w40w40w41f40w40w40w48,191f48,195i48,199i48,203i48,207f48,54w48,54w48,54w54,170w54,178w54,186w48,54w48,54w48,x53w48f48f48f48f48f48,43f48,43f48,43f48f48,43f48,43f48,43f48,192f48,196f48,200f48,204f48,208f48,156b48,159b48,162b48,171w48,179d48,187w48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48,157b48,160y48,163b48,172w48,180f48,188w48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48,158b48,161w48,164brrr72f71fr72f48f48f48f48f48frrr52w51w51w51w51w51w51w51w48f51w51w51w51w51w51w48f48f48frrr72f71fr72f48f48f48f48f48fx55w48,54w48,54w48,54w48,54w48,54w48,54w48,54w48f48,54w48,54w48,54w48,54w48,54w48,54w48f48f48frrr72f70fr72f48f48f48f48f48f48,x55w48f48f48f48,120b48,127b48,134b48,141b48f48,120b48,127b48,134b48,141b48f48f48f48f48frrr72f71fr72f48f48f48f48,59f48f48,x55w48f48f48f48,121c48,128b48,135b48,142b48f48,121c48,128b48,135b48,142b48f48f51w51w51wrrr72f71fr72f51w51w51w51w51w48,x50w48f48,148f48,152f48,122y48,129y48,136y48,143y48f48,122y48,129y48,136y48,143y48f48f48,54w48,54w48,54wrrr72f71fr72f48,54w48,54w48,54w48,54w48,54w48,x53w48f48,149w48,153w48,123w48,130f48,137f48,144w48f48,123w48,130f48,137f48,144w48f48f48f48f48frrr72f71fr74f48f48f48f48f48f48f48f48,150w48,154w48,124w48,131w48,138w48,145w48f48,124w48,131w48,138w48,145w48f48,64w48,156b48,159b48,162brr73f71f71f68f67f48f48f48,64w48f48f48,151w48,155w48,125w48,132d48,139w48,146w48f48,125wx48,132d48,139w48,146w48f48f48,157b48,160y48,163brr67frr68f71f71f68f67f48f48f48f48,62w48,63w48,63w48,126w48,133f48,140w48,147w48f48,126w48,133f48,140w48,147w48f48f48,158b48,161w48,164b48frr67frr68f71f71f68f67f48f48f48f48f48f48,58f48,65f48,60f48f48f48f48,65f48f48,66w48f48f48f48f48f48f48frr67frr68f71f71f68f72f72f72f72f72f72f72f72f72f72f72f72f72f72f72f72f48f48f48f48f48f48frr67frr68f71f71f71f71f71f71f71f71f71f71f71f70f71f71f71f71f71f71f48f48f48f48f48f48f48frr67frr69frr72frr72frr72frr72frr72frr72frr72frr72frr72frr72frr72frr72frr72frr72frr72frr72frr72f");

        loadRoom(rooms.get(0), "48f48f48f48,153w48,161f48,169w48f48f48,x49w48f48f48f48f48f40w40f40f40f40f40f40w48f48f48f48f48f48f48f48f48,154w48,162f48,170w48f48f48,x49w48f48,88b48f48f48f40w40f40f40f40f40f40w48,87b48f48f48f48f48,64f48f48f48,155w48,163f48,171w48f48f48,x49w48f48f48f48f48f40w40f40f40w40f40f40w48f48w48w48w48f48f48f48f48,156w48,164f48,172w48f48f48,x49w48f48f48,57w48f48f40w44f40w42d40w44f40w48,177f48,179i48,181i48,183i48,185f51f51f51f51,157w51,165f51,173w51f51f48,x50w48f48f48f48f48f40w40w40w41f40w40w40w48,178f48,180f48,182f48,184f48,186f48,54w48,54w48,54w54,158w54,166w54,174w48,54w48,54w48,x53w48f48f48f48f48f48,43f48,43f48,43f48f48,43f48,43f48,43f48,192f48,196f48,200f48,204f48,208f48,88b48f48f48,159w48,167d48,175w48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48,160w48,168f48,176w48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48,57w48frrr72f71fr72f48f48,89f48f48f48frrr52w51w51w51w51w51w51w51w48f51w51w51w51w51w51w48f48f48frrr72f71fr72f48f48f48f48f48fx55w48,54w48,54w48,54w54,86b48,54w48,54w48,54w48f54,86b48,54w48,54w48,54w48,54w48,54w48f48f48frrr72f70fr72f48f48f48f48f48f48,x55w48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48f48frrr72f71fr72f48f48f48f48,59f48f48,x55w48f48f48f48c48f48f48f48f48c48f48f48f48f48f51w51w51wrrr72f71fr72f51w51w51w51w51w48,x50w48f48,136f48,140f48w48w48w48w48f48w48w48w48w48f48f48,54w48,54w48,54wrrr72f71fr72f48,54w48,54w48,54w48,54w48,54w48,x53w48f48,137w48,141w48,120w48,124f48,128f48,132w48f48,120w48,124f48,128f48,132w48f48f48f48f48frrr72f71fr74f48f48f48f48f48f48f48f48,138w48,142w48,121w48,125w48,129w48,133w48f48,121w48,125w48,129w48,133w48f48,64w48,88b48f48frr73f71f71f68f67f48f48f48,64w48f48f48,139w48,143w48,122w48,126d48,130w48,134w48f48,122w48,126d48,130w48,134w48f48f48f48f48frr67frr68f71f71f68f67f48f48f48f48,62w48,63w48,63w48,123w48,127f48,131w48,135w48f48,123w48,127f48,131w48,135w48f48f48f48,57w48f48frr67frr68f71f71f68f67f48f48f48f48f48f48,58s48,65f48,60s48f48f48f48,65f48f48,66w48f48f48f48f48f48f48frr67frr68f71f71f68f72f72f72f72f72f72f72f72f72f72f72f72f72f72f72f72f48f48f48f48f48f48frr67frr68f71f71f71f71f71f71f71f71f71f71f71f70f71f71f71f71f71f71f48f48f48f48f48f48f48frr67frr69frr72frr72frr72frr72frr72frr72frr72frr72frr72frr72frr72frr72frr72frr72frr72frr72frr72f");

        //        rooms.get(0).addChicken(new Chicken(8, 8));
//        rooms.get(0).addRat(new Rat(6, 6));
//        rooms.get(0).addChicken(new Chicken(5, 8));
//        rooms.get(0).addEnemy(new Slime(5, 9));
        rooms.get(0).objects.add(player);
        rooms.get(0).addDoor(new Door(5, 1));


        copyRooms[0].objects.add(player);
        loadRoom(copyRooms[0], "29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f35f8f9f10f11f8f9f10f11f30f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f36f12f13f14f15f12f13f14f15f31f8f9f10f11f30f29f29f29f29f29f29f29f29f29f29fx5f37w16w17w18w19w16w17w18w19w32w12f13f14f15f31f29f29f29f29f29f29f29f29f29f29fx6w0,38w0,20f0,21f0,22f0,23f0,20f0,21f0,22f0,23f0,33w16w17w18w19w32w29f29f29f29f29f29f29f29f29f29fx5w0,39f0p0p0p0p0p0p0p0p0,34f0,20f0,21f0,22f0,23f0,33w29w29f29f29f29f29f29f29f29f29fx4w0,x25p0p0p0p0p0p0p0p0p0p0p0p0p0p0,34f29w29f29f29f29f29f29f29f29f29fx3w0,x24p0,r24p0,r25p0,r26p0p0p0p0p0p0p0p0p0p0p0,27p6w29f29f29f29f29f29f29f29f29f29wr6wr3wr4wr5w0,r47f0p0p0p0p0p0p0p0p0p0,26p5w29f29f29f29f29f29f29f29f29f29f29f29f29f29w29w0,r47f0p0p0p0p0p0p0p0p0,25p4w29f29f29f29f29f29f29f29f29f29f29f29f29f29f29wx5w0,x26p0p0p0p0p0p0f0p0,24p3w29f29f29f29f29f29f29f29f29f29f29f29f29f29f29fx4w0,x25p0p0p0p0p0p0p0p0,27p30w29f29f29f29f29f29f29f29f29f29f11f30f29f29f29fx3w0,x24p0p0p0p0p0p0p0p0,26p31w30f29f29f29f29f29f29f29f29fx5f15f31f8f9f10f2w0,x27p0p0p0p0p0p0p0p0,25p32w31f8f9f10f11f30f29f29f29fx4w19w32w12f13f14f15w0,x26p0p0p0p0p0p0p0p0,24p0,33w32w12f13f14f15f31f30f29f29fx3w0,23f0,33w16w17w18w19w0,x25p0p0p0p0p0p0p0p0p0,34f0,33w16w17w18w19w32w31f29f29fx4w0,x25p0,34f0,20f0,21f0,22f0,23f0,x24p0p0p0p0p0p0p0p0p0p0,34f0,20f0,21f0,22f0,23f0,33w32w29f29fx3w0,x24p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0,34f0,33w29w29f29w0,r47f0,r24p0,r25p0,r26p0,r26p0,r27p0,r24p0,r25p0,r26p0,r27p0,r24p0,r25p0,r26p0,r27p0,r27p0p0p0p0p0p0p0p0,34f29w29f29w29wr3wr4wr6wr5wr4wr3wr4wr5wr6wr3wr4wr5wr5wr6w0,r47f0,r24p0,r25p0,r26p0,r27p0,r24p0,r25p0,r28p5w29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29w29wr3wr4wr5wr6wr3wr4wr3w29w29f");
        copyRooms[0].addDoor(new Door(5, 5));

        copyRooms[1].objects.add(player);
        loadRoom(copyRooms[1], "29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f35f8f9f10f11f30f29f29f29f29f29f29f29f29f29f35f8f9f10f11f30f29f29f29f29f35f36f12f13f14f15f31f30f29f29f29f29f29f29f29f35f36f12f13f14f15f31f8f9f10f11f36f37w16w17w18w19w32w31f30f29f29f29f29f29f29f36f37w16w17w18w19w32w12f13f14f15w37w0,38w0,20f0,21f0,22f0,23f0,33w32w31f30f29f29f29f29f29f37w0,38w0,20f0,21f0,22f0,23f0,33w16w17w18w19w0,38w0,39f0p0p0p0p0,34f0,33w32w31f29f29f29f29f29w0,38w0,39f0p0p0p0p0,34f0,20f0,21f0,22f0,23f0,39f0p0p0p0p0p0p0,34f0,33w32w29f29f29f29f29w0,39f0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0,34f0,33w29w29f29f29frr3w0,rr26p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0,34f29w29f29f29frr6w0,rr27p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0,26p3w29f29f29frr5w0,rr26p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0,27p4w29f29f29frr4w0,rr25p0p0p0p0p0p0p0p0p0p0f0p0p0p0p0p0p0p0p0p0,26p5w29f29f29frr3w0,rr24p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0,27p6w29f29f29f29w0,r47f0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0,26p3w29f29f29f29w29w0,r47f0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0,27p4w29f29f29f29f29w29w0,r47f0,r27p0,r26p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0p0,26p5w29f29f29f29f29f29w29wr4wr3w0,r47f0p0p0p0p0p0p0p0p0p0p0p0p0p0p0,27p6w29f29f29f29f29f29f29f29f29w29w0,r47f0p0p0p0p0p0p0p0p0p0p0p0p0p0,26p3w29f29f29f29f29f29f29f29f29f29wrr4w0,x27p0p0p0p0p0p0p0p0p0p0p0p0p0,47f29w29f29f29f29f29f29f29f29f29f29frr3w0,rr28p0,r26p0,r27p0,r26p0,r27p0,r26p0,r26p0,r26p0,r27p0,r26p0,r27p0,r24p0,47f29w29w29f29f29f29f29f29f29f29f29f29f29wr6wr5wr4wr3wr6wr5wr4wr3wr6wr5wr4wr3w29w29w29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f");
        copyRooms[1].addDoor(new Door(5, 5));

//        copyRooms[2].objects.add(player);
//        loadRoom(copyRooms[2], "29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0,rr47f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0f0f0f0f0f0f0,47fr3fr4fr5fr6f0,r47f0f0f0f0f0f0f0f29f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0f0f0f0f0f0,47f29f29f29f29f29f29f0,r47f0f0f0f0f0f0f29f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0f0f0f0f0,47f29f29f29f29f29f29f29f29f0,r47f0f0f0f0f0f29f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0f0f0f0,47f29f29f29f29f29f29f29f29f29f29f0,r47f0f0f0f0f29f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0f0f0f3f29f29f29f29f29f29f29f29f29f29fx3f0f0f0f0f29f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0f0f0f4f29f29f29f29f29f29f29f29f29f29fx4f0f0f0f0f29f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0f0f0f5f29f29f29f29f29f29f29f29f29f29fx5f0f0f0f0f29f29f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0f0f0f6f29f29f29f29f29f29f29f29f29f29fx6f0f0f0f0frrr47f29f29f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0f0f0f30f29f29f29f29f29f29f29f29f29f29f35f0f0f0f0f0frrr47f29f29f29f29f29f29f29f29f29f29f29f29f29f29f0f0f0f0f29f0f0f0f0f31f30f29f29f29f29f29f29f29f29f35f36f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f30f0f0f0f29f0f0f0f0f32f31f30f29f29f29f29f29f29f35f36f37f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f30f0f0f29f0f0f0f0f0,33f32f31f30f29f29f29f29f35f36f37f0,38f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f29f0f0f0f0f0,34f0,33f32f31f8f9f10f11f36f37f0,38f0,39f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f29f0f0f0f0f0f0,34f0,33f32f12f13f14f15f37f0,38f0,39f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f29f0f0f0f0f0f0f0,34f0,33f16f17f18f19f0,38f0,39f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f29f0f0f0f0f0f0f0f0,34f0,20f0,21f0,22f0,23f0,39f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f29f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0fr47f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0f0fr47f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0f0f0f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29f29fr47f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0fr47f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f47f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f47f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0fr47f0f0f0f0f0f0f0f0f0f0f0f0f0f0f47f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0fr47f0f0f0f0f0f0f0f0f0f0f0f0f47f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f29f29f29f29f29f29f29f29f29f29f29f29f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f");
//        copyRooms[2].addDoor(new Door(5, 5));

        rooms.add(new Room(copyRooms[Loader.randomInt(0,roomsIveCompleted)]));

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
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    if (!player.playingTeleportAnim) {
                        player.x = 8 * Room.tw;
                        player.y = 9 * Room.tw - 12;
                        player.updateWeaponPos();
                        player.showPlayer = false;
                        player.playingTeleportAnim = true;
                        player.teleport.restart();
                        player.shadow = false;
                        room = rooms.get(0);
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_H) {
                    Main.player.hp -= 5;
                    UI.doHPAnim(5);
                }
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    fullscreen = !fullscreen;
                    window.setFullscreen(fullscreen);
                }
                if (e.getKeyCode() == KeyEvent.VK_E) {
                    for (ItemSpot itemSpot : room.itemSpots) {
                        if (player.getPartialBounds().intersects(itemSpot.getBounds())) {
                            player.setWeapon(itemSpot.weapon);
                            break;
                        }
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_V) {
                    player.swordSlice.setSpeed(100);
                }
                if (e.getKeyCode() == KeyEvent.VK_G) {
                    Main.room.addGold(7 * 48, 7 * 48, 3);
                }
                if (e.getKeyCode() == KeyEvent.VK_Q) {
                    player.updateWeaponPos();
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
                    rooms.get(0).addParticle(p.x * Room.tw + 10, p.y * Room.tw + 5, 2, 4, 1, 1.5f, 1f, 1.5f, 4, true, true, false, Math.PI, .2);
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
        Display.mouseRect = new Rectangle(Display.mousePos.x - camera.x, Display.mousePos.y - camera.y, 1, 1);
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
        Weapon[] shop = new Weapon[]{test, rangeTest, magicTest};
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
                        if (Integer.parseInt(tile.toString()) < 80 || Integer.parseInt(tile.toString()) > 89) {
                            room1.addArt(new Tile(xPos, yPos, Loader.rotateImage(tiles[Integer.parseInt(tile.toString())], numRotations * 90)));
                        } else {
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
                    if (flip)
                        room1.frontArt.add(new Tile(xPos, yPos, Loader.rotateImage(Loader.flipped(tiles[Integer.parseInt(tile.toString())], true), numRotations * 90)));
                    else
                        room1.frontArt.add(new Tile(xPos, yPos, Loader.rotateImage(tiles[Integer.parseInt(tile.toString())], numRotations * 90)));
                } else if (s.charAt(x) == 'i') {
                    room1.addWall(new Tile(xPos, yPos, Loader.rotateImage(tiles[Integer.parseInt(tile.toString())], numRotations * 90)));
                    room1.addItemSpot(new ItemSpot(xPos, yPos, shop[tileNum]));
                    tileNum++;
                } else if (s.charAt(x) == 's') {
                    room1.addDestructibleTile(new DestructibleTile(xPos, yPos, Loader.rotateImage(tiles[Integer.parseInt(tile.toString())], numRotations * 90), particles[4]));
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