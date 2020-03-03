package com.company.rendering;

import com.company.Loader;
import com.company.Main;
import com.company.Room;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static com.company.rendering.Display.*;

public class UI {
    public static Font pixel, compass, equipment, expression, futile, matchup, bigMatchup;
    private BufferedImage[] hpBar = Loader.cutSpriteSheet("hpBar", 1, 3, Room.imageMult, 16, 16);
    private BufferedImage[] hpMana = Loader.cutSpriteSheet("health", 3, 2, Room.imageMult, 16, 80);
    private BufferedImage goldIcon = Loader.loadImage("goldIcon", Room.imageMult), goldBG = Loader.loadImage("goldBG", Room.imageMult);
    private Rectangle hpRect = new Rectangle(17, height - 5 - Room.tw * 6, 24, 240), mpRect = new Rectangle(46, height - 5 - Room.tw * 6, 24, 240);
    private Color gold = new Color(232, 170, 53);
    private HpAnimation hpAnim, mpAnim;
    public static String goldString = "0";
    private static int timer;
    private static boolean transitioning;
    private FontMetrics fm;
    private int hpBounce;
    private double hpSinCount;
    private static boolean hpBouncing;
    private static int damage;

    public UI(Main main) {
        pixel = loadFont("PixelFJVerdana12pt", Font.BOLD, 20);
        compass = loadFont("CompassPro", Font.PLAIN, 50);
        equipment = loadFont("EquipmentPro", Font.PLAIN, 50);
        expression = loadFont("ExpressionPro", Font.PLAIN, 50);
        futile = loadFont("FutilePro", Font.PLAIN, 50);
        matchup = loadFont("MatchupPro", Font.PLAIN, 50);
        bigMatchup = loadFont("MatchupPro", Font.PLAIN, 60);
        fm = new Canvas().getFontMetrics(matchup);

        hpAnim = new HpAnimation(7, new BufferedImage[]{hpMana[0], hpMana[1], hpMana[2]});
        mpAnim = new HpAnimation(7, new BufferedImage[]{hpMana[3], hpMana[4], hpMana[5]});
        main.mouseAdapter = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                Display.mouseDown = true;
            }
            public void mouseReleased(MouseEvent e) {
                Display.mouseDown = false;
            }
        };
    }

    public void tick() {
        if (transitioning) {
            timer++;
            if (timer > 60) {
                timer = 0;
                transitioning = false;
            }
        }
        if (hpBouncing) {
            hpSinCount += .6f;
            hpBounce = (int) (Math.sin(hpSinCount) * 50 * ((float)damage / Main.player.maxHP));
            if (hpSinCount >= Math.PI) {
                hpSinCount = 0;
                hpBounce = 0;
                hpBouncing = false;
            }
        }
        hpAnim.repeatAnimation();
        mpAnim.repeatAnimation();
    }

    public void render(Graphics2D g) {
        //draw room transition
        if (transitioning) {
            g.setFont(equipment);
            g.setColor(Color.white);
            g.drawString("Room: " + Main.roomNum, 20 + (timer / 4), 50);
        }
        //draw hpBars, gold
        final int gap = 5;
        g.drawImage(hpBar[2], gap, height - gap - Room.tw * 2 - hpBounce, null);
        g.drawImage(hpBar[2], 2 * gap + 24, height - gap  - Room.tw * 2, null);
        int h, m;
        for (h = 0; h < 3; h++) {
            g.drawImage(hpBar[1], gap, height - gap - Room.tw * (h+3) - hpBounce, null);
        }
        for (m = 0; m < 3; m++) {
            g.drawImage(hpBar[1], 2 * gap + 24, height - gap - Room.tw * (m+3), null);
        }
        g.drawImage(hpBar[0], gap, height - gap - Room.tw * 6 - hpBounce, null);
        g.drawImage(hpBar[0], 2 * gap + 24, height - gap  - Room.tw * 6, null);
        hpAnim.drawCroppedAnimation(g, gap, height - gap - Room.tw * 6 + 231 - hpBounce, (int)((float)Main.player.hp / Main.player.maxHP * 77));
        mpAnim.drawCroppedAnimation(g, 2 * gap + 24, height - gap - Room.tw * 6 + 231, (int)((float)Main.player.mp / Main.player.maxMP * 77));
        //draw gold
        g.drawImage(goldBG, 3 * gap + 61, height - gap - Room.tw * 2, null);
        g.drawImage(goldIcon, 3 * gap + 64, height - gap - Room.tw * 2, null);
        g.setFont(matchup);
        g.setColor(gold);
        g.drawString(goldString, 4 * gap + 176 - fm.stringWidth(goldString), height - gap - 60);
        if (mouseRect.intersects(hpRect)) {
            g.setColor(Color.white);
            g.drawString(Main.player.hp + "/" + Main.player.maxHP, mouseRect.x + 16, mouseRect.y);
        }
        if (mouseRect.intersects(mpRect)) {
            g.setColor(Color.white);
            g.drawString(Main.player.mp + "/" + Main.player.maxMP, mouseRect.x + 16, mouseRect.y);
        }
    }

    public static void doRoomTransition() {
        transitioning = true;
        timer = 0;
    }

    public static void doHPAnim(int inDamage) {
        hpBouncing = true;
        damage = inDamage;
    }

    //java -jar E:\IntelliJprojects\Arctown\out\artifacts\Arctown_jar\Arctown.jar

    private Font loadFont(String path, int style, int size) {
        InputStream file = Loader.class.getResourceAsStream("assets/fonts/" + path + ".ttf");
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, file).deriveFont(style, size);
           /*
           GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
           ge.registerFont(pixelFont);
            */
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        return font;
    }
//        g.setColor(Color.white);
//        g.setFont(pixel);
//        g.drawString("Fart poo, but is it you?", 50, 50);
//        g.setFont(compass);
//        g.drawString("Fart poo, but is it you?", 50, 100);
//        g.setFont(equipment);
//        g.drawString("Fart poo, but is it you?", 50, 150);
//        g.setFont(expression);
//        g.drawString("Fart poo, but is it you?", 50, 200);
//        g.setFont(futile);
//        g.drawString("Fart poo, but is it you?", 50, 250);
//        g.setFont(matchup);
}
