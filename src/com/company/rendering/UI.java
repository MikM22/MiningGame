package com.company.rendering;

import com.company.Loader;
import com.company.Main;
import com.company.Room;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static com.company.rendering.Display.*;

public class UI {
    public static Font pixel, compass, equipment, expression, futile, matchup, bigMatchup;
    private BufferedImage[] hpBar = Loader.cutSpriteSheet("hpBar", 1, 3, Room.imageMult, 16, 16);
    private BufferedImage goldIcon = Loader.loadImage("goldIcon", Room.imageMult), goldBG = Loader.loadImage("goldBG", Room.imageMult);
    private Color red = new Color(182, 18, 14), blue = new Color(45, 7, 191), gold = new Color(232, 170, 53);
    private static int timer;
    private static boolean transitioning;

    public UI(Main main) {
        pixel = loadFont("PixelFJVerdana12pt", Font.BOLD, 20);
        compass = loadFont("CompassPro", Font.PLAIN, 50);
        equipment = loadFont("EquipmentPro", Font.PLAIN, 50);
        expression = loadFont("ExpressionPro", Font.PLAIN, 50);
        futile = loadFont("FutilePro", Font.PLAIN, 50);
        matchup = loadFont("MatchupPro", Font.PLAIN, 50);
        bigMatchup = loadFont("MatchupPro", Font.PLAIN, 60);
        main.mouseAdapter = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                Display.mouseDown = true;
            }
            public void mouseReleased(MouseEvent e) {
                Display.mouseDown = false;
                //System.out.println(mousePos);
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
        g.drawImage(hpBar[2], gap, height - gap - Room.tw * 2, null);
        g.drawImage(hpBar[2], 2 * gap + 24, height - gap  - Room.tw * 2, null);
        int h, m;
        for (h = 0; h < Main.player.hpUpgrades + 2; h++) {
            g.drawImage(hpBar[1], gap, height - gap - Room.tw * (h+3), null);
        }
        for (m = 0; m < Main.player.manaUpgrades + 2; m++) {
            g.drawImage(hpBar[1], 2 * gap + 24, height - gap - Room.tw * (m+3), null);
        }
        g.drawImage(hpBar[0], gap, height - gap - Room.tw * (h+3), null);
        g.drawImage(hpBar[0], 2 * gap + 24, height - gap  - Room.tw * (m+3), null);
        g.setColor(red);
        int hpBarHeight = (gap - Room.tw * (h+3) + 5);
        g.fillRect(gap + 18, height + (int)((float)Main.player.hp / Main.player.maxHP * hpBarHeight), 12, (75 + 48 * h) + (hpBarHeight - (int)((float)Main.player.hp / Main.player.maxHP * hpBarHeight)));
        g.setColor(blue);
        int mpBarHeight = (gap - Room.tw * (m+3) + 5);
        g.fillRect(2 * gap + 42, height + (int)((float)Main.player.mp / Main.player.maxMP * mpBarHeight), 12, (75 + 48 * m) + (mpBarHeight - (int)((float)Main.player.mp / Main.player.maxMP * mpBarHeight)));
        g.drawImage(goldBG, 3 * gap + 61, height - gap - Room.tw * 2, null);
        g.drawImage(goldIcon, 3 * gap + 64, height - gap - Room.tw * 2, null);
        g.setFont(matchup);
        g.setColor(gold);
        g.drawString(Main.player.gold + "G", 4 * gap + 108, height - gap - 60);
    }

    public static void doRoomTransition() {
        transitioning = true;
        timer = 0;
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