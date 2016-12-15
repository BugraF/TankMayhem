
package game;

/**
 *
 * @author Haluk İncidelen
 */
public abstract class AI extends Player {

    public AI(String name, String mode, int color) {
        super(name, mode, color);
    }
    
    /**
     * AI plays its turn
     */
    public void play() {
        // TODO Haluk: Implement AI.play()
    }
    
    /**
     * AI analyzes the game and chooses a path according to the other player’s situations
     */
    abstract void analyze();
    
    /**
     * AI purchases the optimal bomb or bombs in that situation
     */
    abstract void purchase();
    
    /**
     * AI moves if necessary (If there are power-up or if there are an obstacle)
     */
    abstract protected void move();
    
    /**
     * AI fires bomb or uses a power-up
     */
    abstract protected void interact();

}
