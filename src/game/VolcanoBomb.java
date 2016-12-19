package game;

import processing.core.PGraphics;

/**
 *
 * @author Burak Gök
 */
public class VolcanoBomb extends Bomb {
    
    public VolcanoBomb(Game game) {
        super(game);
    }

    @Override
    public void draw(PGraphics g, int[] bounds) {
        g.fill(255, 0, 255);
        g.ellipse(x - 2, y - 2, 4, 4);
    }
    
    @Override
    public void checkConstraints() {
        int[] collision = world.rayCast((int)lastX, (int)lastY, (int)x, (int)y, 
                allMask);
        if (collision.length > 0) {
            game.removeEntity(this);
            
            float[] normal = world.getNormal(collision[2], collision[3], 3,
                    collision[4]); // Compute this before destucting the terrain
            
            if ((collision[4] & tankMask) != 0) // A tank is hit
                damageTanks(collision[2], collision[3]);
            else // The terrain is hit
                destructTerrain(collision[2], collision[3]);
            
            double rotate = Math.atan2(normal[1], normal[0]) + Math.PI / 2;
//            System.out.println(rotate);
            for (int i = 0; i < 4; i++) {
                float angle = -(float)(Math.random() * Math.PI - rotate);
                float velX = (float)(300 * Math.cos(angle));
                float velY = (float)(300 * Math.sin(angle));
                
                Bomb bomb = new SimpleBomb(game);
                bomb.blastPower = 30;
                bomb.blastPower *= game.getActiveTank().getDamageBonus();
                bomb.init(collision[0], collision[1], velX, velY);
                game.addEntity(bomb);
            }
        }
        
        // Reset last positions
        lastX = x;
        lastY = y;

        // Boundary constraints
        if (x < 0 || x > world.width() || y > world.height())
            game.removeEntity(this);
    }
    
}
