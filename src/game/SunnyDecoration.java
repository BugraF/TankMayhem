package game;

import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Sunny Decoration has 
 *      - Sunny themed background
 * @author Buğra Felekoğlu 
 */
public class SunnyDecoration extends Decoration {
    
    private PImage background;
    
    @Override
    void drawBackground(PGraphics g, int[] bounds) {
        // bounds[0]: 0 -> world.width() - (bounds[2] - bounds[0])
        // translateX: 0 -> -(background.width - (bounds[2] - bounds[0])
        int cw = bounds[2] - bounds[0]; // Camera Width
        int ch = bounds[3] - bounds[1]; // Camera Height
        
//        float translateX = PApplet.map(bounds[0], 0, world.width() - cw, 
//                0, -(background.width - cw));
//        float translateY = PApplet.map(bounds[1], 0, world.height() - ch, 
//                0, -(background.height - ch));
        float translateX = -(background.width - cw) * bounds[0] / (world.width() - cw);
        float translateY = -(background.height - ch) * bounds[1] / (world.height() - ch);
        g.translate(translateX, translateY);
        g.image(background, 0, 0);
        g.translate(-translateX, -translateY);
//        g.background(185);
    }

    @Override
    void drawForeground(PGraphics g, int[] bounds) {
        // TODO @Anyone randomly show foreground objects
    }

    @Override
    void setResources(PImage... resources) {
        background = resources[0];
    }
}
