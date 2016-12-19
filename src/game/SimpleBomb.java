package game;

import processing.core.PGraphics;

/**
 *
 * @author Burak Gök
 */
public class SimpleBomb extends Bomb {
    
    public SimpleBomb(Game game) {
        super(game);
    }

    @Override
    public void draw(PGraphics g, int[] bounds) {
        g.fill(0);
        g.ellipse(x - 1, y - 1, 3, 3);
    }
    
    @Override
    public void checkConstraints() {
        int[] collision = world.rayCast((int)lastX, (int)lastY, (int)x, (int)y, 
                allMask);
        if (collision.length > 0) {
            game.removeEntity(this);
            if ((collision[4] & tankMask) != 0) // A tank is hit
                damageTanks(collision[2], collision[3]);
            else // The terrain is hit
                destructTerrain(collision[2], collision[3]);
        }
        
        // Reset last positions
        lastX = x;
        lastY = y;

        // Boundary constraints
        if (x < 0 || x > world.width() || y > world.height())
            game.removeEntity(this);
    }
    
}
