package game;

import gui.core.MouseEvent;
import processing.core.PGraphics;
import processing.event.KeyEvent;

/**
 *
 * @author Burak Gök
 */
public class FireInteraction implements Interaction {
    /** Context of this interaction */
    private final Game game;
    
    /** Associated tank */
    private Tank tank;
    
    /** Id of the bomb that will be generated */
    private int itemId;
    
    /** Camera bounds of the stage of the game associated to this interaction */
    private final int[] camBounds;
    
    public FireInteraction(Game game) {
        this.game = game;
        tank = game.getActiveTank();
        camBounds = game.getStage().getCameraBounds();
    }
    
    void setTank(Tank tank) {
        this.tank = tank;
    }
     
    void setItemId(int itemId) {
        this.itemId = itemId;
    }
    
    @Override
    public String getAction() {
        return "FIRE";
    }

    private final float maxPower = 100;
    
    @Override
    public void drawBehindTerrain(PGraphics g) {
        g.ellipseMode(PGraphics.RADIUS);
        g.fill(0, 0, 255, 70);
        g.noStroke();
        g.ellipse(tank.getX(), tank.getBarrelPosition(), maxPower, maxPower);
    }

    @Override
    public void drawAfterTerrain(PGraphics g) {
//        g.ellipseMode(PGraphics.RADIUS);
        g.fill(0, 0, 255, 120);
//        g.noStroke();
        float power = maxPower * tank.firePower;
        g.arc(tank.getX(), tank.getBarrelPosition(), power, power,
                -tank.fireAngle - 0.3f, -tank.fireAngle + 0.3f);
    }

    @Override
    public void _finalize() {
//        Bomb bomb = (Bomb) Catalog.create(itemId);
        Bomb bomb = new SimpleBomb(game); // Test
        bomb.blastPower = 60; // Test
        
        float angle = -(tank.fireAngle + tank.getRotation());
        float sin = (float) Math.sin(angle);
        float cos = (float) Math.cos(angle);
        float x = tank.getX() + 40 * cos; // TODO 40: barrel width
        float y = tank.getBarrelPosition() + 40 * sin;
        float velX = 1000 * tank.firePower * cos; // TODO Specify factor
        float velY = 1000 * tank.firePower * sin;
        
        bomb.init(x, y, velX, velY);
        bomb.blastPower *= tank.getDamageBonus();
        game.addEntity(bomb);
        
        game.getCurrentPlayer().getInventory().remove(itemId);
        game.switchTurnWhenStabilized(true);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 37) // <-
            tank.fireAngle = (float)Math.min(tank.fireAngle + 0.05, Math.PI);
        else if (e.getKeyCode() == 39) // ->
            tank.fireAngle = (float)Math.max(tank.fireAngle - 0.05, 0);
        else if (e.getKeyCode() == 38) // Up
            tank.firePower = (float)Math.min(tank.firePower + 0.02, 1);
        else if (e.getKeyCode() == 40) // Down
            tank.firePower = (float)Math.max(tank.firePower - 0.02, 0);
    }
    
    @Override
    public boolean mousePressed(MouseEvent e) {
        return mouseDragged(e);
    }

    @Override
    public boolean mouseDragged(MouseEvent e) {
        float x = tank.getX() - camBounds[0]; // tank x on component
        float y = tank.getBarrelPosition() - camBounds[1]; // tank y on component
        float dx = e.getX() - x;
        float dy = e.getY() - y;
        
        double angle = -Math.atan2(dy, dx) - tank.getRotation();
        if (angle < 0 || angle > Math.PI)
            return true;
        tank.fireAngle = (float)angle;
        
        double dist = Math.sqrt(dx * dx + dy * dy);
        tank.firePower = (float)Math.min(dist / maxPower, 1);
        return true;
    }
    
}
