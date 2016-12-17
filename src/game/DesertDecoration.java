/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Desert Decoration has 
 *      - desert themed background
 *      - animated sun
 *      - burned tank
 *      - cactuses
 * @author Buğra Felekoğlu
 */
public class DesertDecoration extends Decoration{
    
    private PImage background;
    private PImage sun;
    private PImage tank;
    private PImage cactus;
    
    @Override
    void drawBackground(PGraphics g, int[] bounds) {
//        float translateX = (bounds[0]*(world.width()-background.width)/
//                (world.width()-(bounds[2]-bounds[0])));
//        float translateY = (bounds[1]*(world.height()-background.height)/
//                (world.height()-(bounds[3]-bounds[1])));
//        g.translate(-translateX, -translateY);
//        g.image(background, 0, 0);
//        g.translate(translateX, translateY);
        g.background(-1);
    }

    @Override
    void drawForeground(PGraphics g, int[] bounds) {
        // TODO @Anyone randomly show foreground objects
    }

    @Override
    void setResources(PImage... resources) {
        background = resources[0];
        sun = resources[1];
        tank = resources[2];
        cactus = resources[3];
    }
}
