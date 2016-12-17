package game;

/**
 * Keeps track of collectible power-ups and bombs owned by a player.
 * @author Burak Gök
 */
public class Inventory {
    
    /**
     * The quantities of items are stored at the locations which are the same as
     * their id's.
     */
    private final int[] quantity = new int[1/*Catalog.COUNT*/]; // Test
    
    /**
     * Increments the quantity of the specified item by 1.
     * Equivalent to {@see #add(int, 1)}.
     */
    void add(int itemId) {
        quantity[itemId]++;
    }
    
    /**
     * Updates the quantity of the specified item by the specified amount.
     */
    void add(int itemId, int quantity) {
        if (quantity < 0)
            throw new RuntimeException("A quantity cannot be negative.");
        this.quantity[itemId] += quantity;
    }
    
    /**
     * Decrements the quantity of the specified item by 1.
     */
    void remove(int itemId) {
        if (quantity[itemId] == 0)
            throw new RuntimeException("The quantity of the specified item is "
                    + "already zero.");
        quantity[itemId]--;
    }
    
}
