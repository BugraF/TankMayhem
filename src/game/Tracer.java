package game;

import game.engine.World;
import processing.core.PGraphics;

/**
 *
 * @author Burak Gök
 */
public class Tracer {
    /** Context of this interaction */
    private final Game game;
    
    private final static float timeStep = 0.025f;
    
    public Tracer(Game game) {
        this.game = game;
    }
    
    public void draw(PGraphics g, int[] bounds) {
        g.pushStyle();
        g.noFill();
        g.stroke(255, 0, 255);
        g.strokeWeight(2);
        Tank tank = game.getActiveTank();
        World world = game.getWorld();
        
        // Copied from FireInteraction
        float angle = -(tank.fireAngle + tank.getRotation());
        float sin = (float) Math.sin(angle);
        float cos = (float) Math.cos(angle);
        float x1 = tank.getX() + 40 * cos; // TODO 40: barrel width
        float y1 = tank.getBarrelPosition() + 40 * sin;
        float velX = 1500 * tank.firePower * cos;
        float velY = 1500 * tank.firePower * sin;
        
        int wind = game.getPhysicsEngine().getWind() * 5; // 5: wind multiplier
        float x2 = x1 + velX * timeStep;
        float y2 = y1 + velY * timeStep;
        velX += wind * timeStep;
        velY += 980 * timeStep;
        float x3 = x2 + velX * timeStep;
        float y3 = y2 + velY * timeStep;
        
        boolean dash = true;
        while (0 < x3 && x3 < world.width() 
                && 0 < y3 && y3 < world.height()) {
            // Copied from Physics
            velX += wind * timeStep;
            velY += 980 * timeStep;
            float x4 = x3 + velX * timeStep;
            float y4 = y3 + velY * timeStep;
            if (dash)
                g.curve(x1, y1, x2, y2, x3, y3, x4, y4);
            x1 = x2; y1 = y2;
            x2 = x3; y2 = y3;
            x3 = x4; y3 = y4;
            dash = !dash;
        }
        g.popStyle();
    }
    
}
