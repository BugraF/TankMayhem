package game;

import processing.core.PGraphics;

/**
 *
 * @author Burak Gök
 */
public class OneBounceBomb extends Bomb {

    private final float bounceFriction = 0.85f;
    private boolean bounced = false;
    
    public OneBounceBomb(Game game) {
        super(game);
    }

    @Override
    public void draw(PGraphics g, int[] bounds) {
        g.fill(0);
        g.ellipse(x - 2, y - 2, 4, 4);
    }
    
    @Override
    public void checkConstraints() {
        int[] collision = world.rayCast((int)lastX, (int)lastY, (int)x, (int)y, 
                allMask);
        if (collision.length > 0) {
            if ((collision[4] & tankMask) != 0) { // A tank is hit
                game.removeEntity(this);
                damageTanks(collision[2], collision[3]);
            }
            else { // The terrain is hit
                if (bounced) {
                    game.removeEntity(this);
                    destructTerrain(collision[2], collision[3]);
                }
                else { // Bounce
                    float normal[] = game.getTerrain()
                            .getNormal(collision[2], collision[3]);
      
                    float d = 2 * (velX * normal[0] + velY * normal[1]);
                    velX -= normal[0] * d;
                    velY -= normal[1] * d;

                    // Apply bounce friction
                    velX *= bounceFriction;
                    velY *= bounceFriction;
      
                    // Reset position so that the bomb starts at the point of
                    // collision
                    x = collision[0];
                    y = collision[1];
                    
                    bounced = true;
                }
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
