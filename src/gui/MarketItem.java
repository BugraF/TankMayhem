package gui;

import game.CatalogItem;
import gui.core.ActionListener;
import gui.core.Component;
import gui.core.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import processing.core.PGraphics;
import processing.event.KeyEvent;

/**
 *
 * @author Buğra Felekoğlu
 */
public class MarketItem extends InventoryView.AbstractButton {

    private final CatalogItem item;
    private final int cost;

    /** Counter of this item */
    private final Counter counter = new Counter();

    /**
     * The objects that listen to the invocation of this button.
     */
    private final List<ActionListener> actionListeners = new ArrayList<>(1);

    /**
     * Constructor that takes name and cost of the item which is known
     * before the creation of object
     */
    public MarketItem(CatalogItem item, int cost) {
        this.item = item;
        this.cost = cost;
    }
    
    CatalogItem getItem() {
        return item;
    }

    Counter getCounter() {
        return counter;
    }
    
    void setHighlighted(boolean highlighted) {
        if (!enabled)
            throw new RuntimeException("Disabled market item cannot be "
                    + "highlighted.");
        state = highlighted ? 1 : 0;
    }

    public void clear() {
        counter.setQuantity(0);
    }

    @Override
    public void draw(PGraphics g) {
        g.stroke(0, 0, 255, 200);
        g.textSize(25);
        g.textAlign(g.CENTER);
        if (state == 0) {
            g.fill(0, 200);
            g.rect(0, 0, width, height, 11, 11, 11, 11);
        }
        else if (state == 1 || state == 2) {
            if (counter.getQuantity() != 0) {
                g.fill(0xBBE30613);
                g.rect(0, 0, width / 2, height, 11, 0, 0, 11);
                g.fill(0xBB009640);
                g.rect(width / 2, 0, width / 2, height, 0, 11, 11, 0);
            }
            else {
                g.fill(0xBB009640);
                g.rect(0, 0, width, height, 11, 11, 11, 11);
            }
        }
        g.image(item.getIcon(), 50, 55);
        g.fill(255);
        g.text(item.getName(), width / 2, height / 2 - 20);
        g.text(String.valueOf(cost), width / 2 + 50, height - 35);
    }

    /**
     * Associates the specified action listener to this button.
     * @return False if this button is already associated to the specified
     *         action listener, true otherwise.
     */
    public boolean addActionListener(ActionListener listener) {
        if (actionListeners.contains(listener))
            return false;
        actionListeners.add(listener);
        return true;
    }

    /**
     * Disassociates the specified action listener from this button.
     * @return False if the specified action listener is not associated to this
     *         button, true otherwise.
     */
    public boolean removeActionListener(ActionListener listener) {
        return actionListeners.remove(listener);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 109 && counter.getQuantity() != 0) { // -
            counter.decrement();
            invoke();
        }      
        else if (e.getKeyCode() == 107) { // +
            counter.increment();
            invoke();
        }          
    }

    private void invoke() {
        for (ActionListener listener : actionListeners)
            listener.actionPerformed(this);
    }

    @Override
    public boolean mouseClicked(MouseEvent e) {
        if(e.getX() > width / 2 || counter.getQuantity() == 0)
            counter.increment();
        else 
            counter.decrement();
        invoke();
        return true;
    }
    
    class Counter extends Component {
        private int quantity = 0;
        private int diff = 0;

        void init() {
            int[] parentBounds = MarketItem.this.bounds;
            setLocation(parentBounds[2] - 4, parentBounds[1] - 4);
            setSize(25, 25);
        }
        
        int getQuantity() {
            return quantity;
        }
        void setQuantity(int quantity) {
            if (quantity < 0)
                throw new RuntimeException("The quantity of an item cannot " +
                        "be negative.");
            this.quantity = quantity;
        }
        void increment() {
            quantity++;
            diff++;
        }
        void decrement() {
            if (quantity == 0)
                throw new RuntimeException("The quantity for this item is " +
                        "already zero.");
            quantity--;
            diff--;
        }
        
        int collect() {
            int change = diff * cost;
            diff = 0;
            return change;
        }
        
        @Override
        public void draw(PGraphics g){
            if(quantity > 0){
                g.fill(0xFFE30613);
                g.ellipse(0, 0, width, height);
                g.fill(255);
                if(quantity < 10) {
                    g.textSize(32);
                    g.textAlign(g.CENTER, g.CENTER);
                    g.text(quantity, 0, -6);
                }
                else {
                    g.textSize(30);
                    g.textAlign(g.CENTER, g.CENTER);
                    g.text(quantity, -2, -6);
                }
            }
        }
    }
}