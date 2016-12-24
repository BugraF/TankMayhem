
package game;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Haluk İncidelen
 */
public class SimpleAI extends AI{

    public SimpleAI( String name, Mode mode, int color) {
        super(  name, mode, color);
    }

    @Override
    protected void analyze() {
        Tank tanks[]=game.getPhysicsEngine().getTanks();
        float minHP=tanks[0].getHP();
        int victim=0;
        for(int i=0; i<tanks.length; i++){
            if(minHP>tanks[i].getHP()){
                minHP=tanks[i].getHP();
                victim=i;
            }  
        }
        
        float delta = tanks[victim].getX() - this.getTank().getX();
        if(delta>0){
            this.getTank().fireAngle = (float) Math.PI/4F;
            this.getTank().firePower = Math.abs(delta)/2000;
        }
            
        else{
            this.getTank().fireAngle = (float) Math.PI*3/4F;
            this.getTank().firePower = Math.abs(delta)/2000;
        }
    }

    @Override
    protected void purchase() {
        return;
    }

    @Override
    protected void move() {
        return;        
    }

    @Override
    protected void interact() {
        //this.game.getStage().setInteraction(interaction);
        if(game.getActiveTank() == this.getTank())
            game.getStage().finalizeInteraction();
    }

}
