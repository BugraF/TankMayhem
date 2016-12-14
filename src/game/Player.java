package game;

/**
 *
 * @author Haluk İncidelen
 */

class Player {
    private	int score;      //Score of the player
    private	int cash;       //Total money of the player
    private	String name;    //Name of the player
    private	String mode;	//Player’s chosen tank mode which affects the bonuses or penalties of the tanks
    private	int color;      //Color of the player
    private	Tank tank;      //Tank object that is owned by this player
    
    /**
     * Returns the tank of this player
     * @return 
     */
    public Tank getTank ( ){ 
        return tank;
    }
    /**
     * //Assigns the specified tank to this player
     * @param tank 
     */
    public void setTank (Tank tank){ 
        
    }
    /**
     * Returns player’s inventory which includes bombs and power-ups
     * @return 
     */
    /*public Inventory getInventory ( ){  		
        //TODO uncommend and implement this after inventory implemented
    }*/

    public int getScore ( ){ 	
        return score;
    }
    /**
     * Increments player’s total score with given quantity
     * @param delta 
     */
    public void updateScore (int delta){    
        score = score + delta;
    }
    /**
     * Returns player’s current money amount
     * @return 
     */
    public int getCash ( ){
        return cash;
    }
    /**
     * Updates player’s total cash with given quantity (Increases or decreases)
     * @param delta 
     */
    public void updateCash (int delta){ 
        cash = cash + delta;
    }
    /**
     * Shows the player’s current situation whether alive or died
     * @return 
     */
    public boolean isAlive ( ){   
        if( tank.getHP() > 0.0 )
            return true;
        else 
            return false;
    }
   
}
