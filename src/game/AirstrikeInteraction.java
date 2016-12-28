package game;

import gui.core.MouseEvent;
import processing.core.PGraphics;
import processing.core.PShape;
import processing.event.KeyEvent;

/**
 *
 * @author Burak Gök
 */
public class AirstrikeInteraction extends Interaction {
    
    public AirstrikeInteraction(Game game) {
        super(game);
    }

    @Override
    void setTank(Tank tank) {
        super.setTank(tank);
        target = (camBounds[2] + camBounds[0]) / 2;
    }
    
    @Override
    public String getAction() {
        return "CALL IN";
    }
    
    private final int radius = 100;
    private int target;
    
    @Override
    public void drawBehindTerrain(PGraphics g) {
        g.pushStyle();
        g.fill(0, 0, 255, 70);
        g.stroke(0, 0, 255, 200);
        g.strokeWeight(1.5f);
        g.rectMode(PGraphics.CORNERS);
        g.rect(target - radius, camBounds[1] - 2, target + radius, camBounds[3] + 2);
        g.popStyle();
    }
    
    private PGraphics g;
    private PShape arrow;
    private long last;
    private int rowIndex = 0;

    @Override
    public void drawAfterTerrain(PGraphics g) {
        final int hs = 30, vs = 80;
        final int flat = 25;
        final int w = (2 * radius - 3 * hs) / 4;
        if (this.g != g) {
            arrow = g.createShape();
            arrow.beginShape();
            arrow.noFill();
            arrow.stroke(0, 50, 255);
            arrow.strokeJoin(PShape.MITER);
            arrow.strokeCap(PShape.SQUARE);
            arrow.strokeWeight(4);
            arrow.vertex(0, 0);
            arrow.vertex(w, flat);
            arrow.vertex(2 * w, 0);
            arrow.endShape();
            this.g = g;
        }
        
        final int rowCount = (camBounds[3] - camBounds[1]) / (flat + vs);
        long now = System.currentTimeMillis();
        if (now - last >= 500) {
            rowIndex = (rowIndex + 1) % rowCount;
            last = now;
        }

        final int left1 = target - radius + hs;
        final int left2 = left1 + 2 * w + hs;
        int top = camBounds[1] + vs / 2;
//        while (top + flat < camBounds[3]) {
        for (int r = 0; r < rowCount; r++) {
            arrow.setStroke(r == rowIndex ? 0xEE0032FF : 0x50505050);
            g.shape(arrow, left1, top);
            g.shape(arrow, left2, top);
            top += flat + vs;
        }
    }

    @Override
    public void _finalize() {
        super._finalize();
        
        final int bombCount = 5;
        final float sep = 2 * radius / (float)bombCount;
        for (int i = 0; i < bombCount; i++) {
            Bomb bomb = new SimpleBomb(game);
            bomb.blastPower = 50;
            bomb.init(target - radius + sep * i, 0, 0, 0);
            game.addEntity(bomb);
        }

        game.getCurrentPlayer().getInventory()
                .remove(game.getCatalog().get("airstrike").getId());
        game.switchTurnWhenStabilized(true);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        if (e.getKeyCode() == 37) // <-
            target = Math.max(target - 10, radius);
        else if (e.getKeyCode() == 39) // ->
            target = Math.min(target + 10, camBounds[2] - radius);
    }
    
    @Override
    public boolean mousePressed(MouseEvent e) {
        return mouseDragged(e);
    }

    @Override
    public boolean mouseDragged(MouseEvent e) {
        target = e.getX() + camBounds[0]; // x in world
        return true;
    }
    
}
