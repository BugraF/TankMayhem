package game;

/**
 *
 * @author Haluk İncidelen
 */
public class Player {

    private final String name;
    private final int color;
    /**
     * Mode of this player.
     * Each mode gives unique damage and shield bonuses.
     */
    private final String mode;
    
    private int score;
    private int cash;
    
    private Tank tank;
    
    private final Inventory inventory = new Inventory();
    
    public Player(String name, String mode, int color) {
        this.name = name;
        this.mode = mode;
        this.color = color;
    }
    
    /**
     * Returns the tank of this player.
     */
    public Tank getTank() { 
        return tank;
    }
    
    /**
     * Assigns the specified tank to this player.
     */
    public void setTank(Tank tank) { 
        this.tank = tank;
    }
    
    /**
     * Returns this player’s inventory.
     * @see Inventory
     */
    public Inventory getInventory() {
        return inventory;
    }
    
    public String getName() {
        return name;
    }

    public String getMode() {
        return mode;
    }

    public int getColor() {
        return color;
    }
    
    public int getScore() { 	
        return score;
    }
    
    public int getCash() {
        return cash;
    }
    
    /**
     * Increments this player’s total score by the specified amount.
     */
    public void updateScore(int delta) {
        if (delta < 0)
            throw new RuntimeException("Score changes cannot be negative.");
        score += delta;
    }
    
    /**
     * Increments this player’s total cash by the specified amount.
     */
    public void updateCash(int delta) {
        if (delta < 0)
            throw new RuntimeException("Money changes cannot be negative.");
        cash = cash + delta;
    }
    
    /**
     * Indicates whether the tank of this player is destroyed.
     * @see Tank#getHP()
     */
    public boolean isAlive() {   
        return tank.getHP() != 0;
    }
   
}
