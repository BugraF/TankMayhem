package game;

import processing.core.PGraphics;

/**
 *
 * @author Burak Gök
 */
public class FireInteraction implements Interaction {
    /** Context of this interaction */
    private final Game game;
    
    /** Id of the bomb that will be generated */
    private int itemId;
    
    public FireInteraction(Game game) {
        this.game = game;
    }
    
    void setItemId(int itemId) {
        this.itemId = itemId;
    }
    
    @Override
    public String getAction() {
        return "FIRE";
    }

    @Override
    public void draw(PGraphics g) {
    }

    @Override
    public void _finalize() {
        Bomb bomb = null;//(Bomb) Catalog.create(itemId);
        
        Tank tank = game.getActiveTank();
        float sin = (float) Math.sin(tank.fireAngle);
        float cos = (float) Math.cos(tank.fireAngle);
        float x = tank.getX() + 30 * cos; // TODO 30: barrel width
        float y = tank.getY() + 30 * sin;
        float velX = tank.firePower * cos * 1; // TODO Specify factor
        float velY = tank.firePower * sin * 1;
        
        bomb.init(x, y, velX, velY);
//        bomb.blastPower *= tank.getDamageBonus();
        game.addEntity(bomb);
        
        game.getCurrentPlayer().getInventory().remove(itemId);
        game.switchTurnWhenStabilized(true);
    }
    
}
