package gui;

import game.Catalog;
import game.CatalogItem;
import game.Game;
import game.Player;
import gui.core.Component;
import gui.core.InteractiveComponent;
import gui.core.MouseEvent;
import gui.core.Parent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.event.KeyEvent;

/**
 *
 * @author Burak Gök
 */
public class InventoryView extends Parent implements Observer {
    /** Context of this inventory view */
    private Game game;
    private Catalog catalog;
    
    private Map<Player, CatalogItem> lastSelection;
    
    private final SelectedItem selectedItem = new SelectedItem();
    private final Selector bombSelector = new Selector("Bombs");
    private final Selector powerupSelector = new Selector("Power-ups");
    private final InventoryItemList bombList = new InventoryItemList();
    private final InventoryItemList powerupList = new InventoryItemList();
    private InventoryItem[] items;
    private boolean expanded = false;
    
    private int tracerId;

    @Override
    public void init(PApplet context) {
        add(selectedItem, bombSelector, powerupSelector, bombList, powerupList);
        parent.associateKeys(this, 73); // I
        super.init(context); // Currently not used
    }
    
    public void attachGame(Game game) {
        this.game = game;
        catalog = game.getCatalog();
        Player[] players = game.getPlayers();
        lastSelection = new HashMap<>(players.length);
        CatalogItem starter = catalog.get("simple_bomb");
        for (Player player : players) {
            lastSelection.put(player, starter);
            player.getInventory().addObserver(this);
        }
        
        tracerId = catalog.get("tracer").getId();
        items = new InventoryItem[Catalog.SIZE];
        initializeItems();
    }
    
    public void detachGame() {
        game = null;
        catalog = null;
        lastSelection.clear();
        selectedItem.setItem(null);
        bombList.clear();
        powerupList.clear();
        items = null;
    }
    
    private void initializeItems() {
        int index = 0;
        Iterator<CatalogItem> iterator = catalog.iterator();
        while (iterator.hasNext()) {
            CatalogItem item = iterator.next();
            items[index++] = new InventoryItem(item);
        }
        placeItems();
    }

    @Override
    protected void drawComponents(PGraphics g) {
        g.pushStyle();
        if (expanded)
            super.drawComponents(g);
        else
            selectedItem.draw(g);
        g.popStyle();
    }
    
    private void expand() {
        super.setSize(width, expandedHeight);
        parent.setFocusedChild(this);
        expanded = true;
    }
    private void collapse() {
        super.setSize(width, collapsedHeight);
        parent.setFocusedChild(game.getStage());
        expanded = false;
    }
    
    private void categorySelected(Selector selector) {
        boolean isBomb = (selector == bombSelector), isPowerUp = !isBomb;
        expandedHeight = isBomb ? bombExpanded : powerupExpanded;
        bombSelector.setSelected(isBomb);
        powerupSelector.setSelected(isPowerUp);
        super.setSize(width, expandedHeight);
        bombList.setVisible(isBomb);
        powerupList.setVisible(isPowerUp);
        setFocusedChild(isBomb ? bombList : powerupList);
    }

    private final static int selector = 120, padding = 15, list = 5;
    private int collapsedHeight, expandedHeight;
    private int bombExpanded, powerupExpanded;
    
    private void placeItems() {
        int bomb, powerup;
        bomb = powerup = 0;
        for (InventoryItem item : items) {
            int itemId = item.getItem().getId();
            if (catalog.isBomb(itemId)) {
                bombList.add(item);
                item.setLocation(0, bomb);
                bomb += height + list;
            }
            else if (catalog.isPowerUp(itemId)) {
                powerupList.add(item);
                item.setLocation(0, powerup);
                powerup += height + list;
            }
            item.setSize(width, height);
        }
        bombList.setSize(width, bomb - list);
        powerupList.setSize(width, powerup - list);
        int t = height + selector + 2 * padding;
        bombExpanded = t + bomb - list;
        powerupExpanded = t + powerup - list;
        expandedHeight = t - padding;
    }
    
    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        collapsedHeight = height;
        selectedItem.setSize(width, height);
        int w = (width - padding) / 2;
        bombSelector.setLocation(0, height + padding);
        bombSelector.setSize(w, selector);
        powerupSelector.setLocation(w + padding, height + padding);
        powerupSelector.setSize(w, selector);
        int t = height + selector + 2 * padding;
        bombList.setLocation(0, t);
        powerupList.setLocation(0, t);
        if (items != null)
            placeItems();
    }
    
    private void itemSelected(InventoryItem inventoryItem) {
        CatalogItem item = inventoryItem.getItem();
        itemSelected(item);
        if (item.getId() == tracerId)
            inventoryItem.setEnabled(false);
    }
    
    private void itemSelected(CatalogItem item) {
        collapse();
        game.selectionChanged(item.getId());
        if (item.getId() != tracerId) {
            selectedItem.setItem(item);
            lastSelection.put(game.getCurrentPlayer(), item);
            ((GameScreen)parent).interactionChanged();
        }
    }
    
    /**
     * Updates the availability information of inventory items and restores the
     * last interaction if its corresponding item is available.
     */
    void playerChanged() {
        update();
        Player player = game.getCurrentPlayer();
        CatalogItem lastItem = lastSelection.get(player);
        if (player.getInventory().get(lastItem.getId()) == 0)
            lastItem = catalog.get("simple_bomb");
        itemSelected(lastItem);
    }
    
    /**
     * Updates the availability information of inventory items.
     */
    private void update() {
        Player player = game.getCurrentPlayer();
        
        int availableBombs = 0, availablePowerUps = 0;
        for (InventoryItem inventoryItem : items) {
            int itemId = inventoryItem.getItem().getId();
            boolean available = player.getInventory().get(itemId) != 0;
            if (itemId != tracerId || !game.getStage().isTracerEnabled())
                inventoryItem.setEnabled(available);
            if (available) {
                if (catalog.isBomb(itemId)) availableBombs++;
                else if (catalog.isPowerUp(itemId)) availablePowerUps++;
            }
        }
        bombSelector.setAvailable(availableBombs);
        powerupSelector.setAvailable(availablePowerUps);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled)
            collapse();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 73) // I
            expand();
        else if (e.getKeyCode() == 66) // B
            categorySelected(bombSelector);
        else if (e.getKeyCode() == 80) // P
            categorySelected(powerupSelector);
        else if (e.getKeyCode() == 8) // Backspace
            collapse();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == game.getCurrentPlayer().getInventory())
            update();
    }
    
    private class SelectedItem extends InventoryItem {
        public SelectedItem() {
            super(null);
        }
        
        @Override
        public boolean mouseClicked(MouseEvent e) {
            if (expanded) collapse();
            else expand();
            return true;
        }
    }
    
    private class InventoryItem extends AbstractButton {
        private final int[] alpha = {128, 64, 200, 30};
        private CatalogItem item;
        
        InventoryItem(CatalogItem item) {
            setItem(item);
        }
        
        CatalogItem getItem() {
            return item;
        }
        void setItem(CatalogItem item) {
            this.item = item;
        }
        
        private final static int leftSec = 80, rightSec = 100, padding = 13;
        
        @Override
        public void draw(PGraphics g) {
            g.noStroke();
            g.fill(0, alpha[state]);
            g.rect(0, 0, width, height);
            
            g.stroke(255);
            g.strokeWeight(3);
            g.fill(255);
            g.imageMode(g.CENTER);
            g.textAlign(g.CENTER, g.CENTER);
            g.textSize(38);
            
            int quantity = game.getCurrentPlayer().getInventory()
                    .get(item.getId());
            int x = leftSec / 2 + 10, y = height / 2;
            g.image(item.getIcon(), x, y);
            x = (leftSec + width - rightSec) / 2;
            y -= 9;
            g.text(item.getName(), x, y);
            x = width - rightSec;
            g.line(x, padding, x, height - padding);
            x = width - rightSec / 2;
            g.text(quantity == Integer.MAX_VALUE ? "∞"
                    : String.valueOf(quantity), x, y);
        }

        @Override
        public boolean mouseClicked(MouseEvent e) {
            itemSelected(this);
            return true;
        }
        
        void setHighlighted(boolean highlighted) {
            if (!enabled)
                throw new RuntimeException("Disabled inventory item cannot "
                        + "be highlighted.");
            state = highlighted ? 1 : 0;
        }
    }
    
    private class Selector extends AbstractButton {
        private final String name;
        private int available;
        private boolean selected = false;
        private final int[] alpha = {128, 200, 200};
        
        Selector(String name) {
            this.name = name;
        }
        
        void setAvailable(int available) {
            this.available = available;
        }
        
        void setSelected(boolean selected) {
            this.selected = selected;
        }
        boolean isSelected() {
            return selected;
        }
        
        @Override
        public void draw(PGraphics g) {
            g.noStroke();
            g.fill(selected ? 0xFF00FF00 : 0, alpha[state]);
            g.rect(0, 0, width, height);
            
            g.fill(255);
            g.textAlign(g.CENTER);
            g.textSize(38);
            
            int x = width / 2, y = height / 2 - 15;
            g.text(name, x, y);
            y = height - 25;
            g.text(String.valueOf(available), x, y);
        }
        
        @Override
        public boolean mouseClicked(MouseEvent e) {
            categorySelected(this);
            return true;
        }
    }
    
    // TODO Move to UI Core
    private class InventoryItemList extends Parent {
        private boolean visible = false;
        private final List<InventoryItem> items = new ArrayList<>(4);
        
        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        @Override
        public void add(Component... comps) {
            super.add(comps);
            for (Component comp : comps)
                items.add((InventoryItem) comp);
        }
        
        void clear() {
            Iterator<InventoryItem> iterator = items.iterator();
            while (iterator.hasNext()) {
                remove(iterator.next());
                iterator.remove();
            }
        }
        
        @Override
        public void draw(PGraphics g) {
            if (visible)
                super.draw(g);
        }

        @Override
        public void handleKeyEvent(KeyEvent e) {
            if (visible)
                super.handleKeyEvent(e);
        }

        private int lastItem = -1;
        
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 38 || e.getKeyCode() == 40) { // Up / Down
                if (lastItem != -1 && items.get(lastItem).isEnabled())
                    items.get(lastItem).setHighlighted(false);
                int nav = e.getKeyCode() == 38 ? -1 : 1;
                if (lastItem == -1) lastItem = 0;
                int item = lastItem;
                do {
                    item = Math.floorMod(item + nav, items.size());
                } while (item != lastItem && !items.get(item).isEnabled());
                if (items.get(item).isEnabled()) {
                    items.get(item).setHighlighted(true);
                    lastItem = item;
                }
                else lastItem = -1;
            }
            else if (e.getKeyCode() == 10 && lastItem != -1) // Enter
                itemSelected(items.get(lastItem));
        }

        @Override
        public boolean handleMouseEvent(MouseEvent e) {
            if (!visible)
                return false;
            return super.handleMouseEvent(e);
        }
    }
    
    // TODO Move to UI Core
    abstract static class AbstractButton extends InteractiveComponent {
        protected int state = 0;  // normal, hover, pressed, disabled
        
        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            state = enabled ? 0 : 3;
        }
        
        @Override
        public boolean mousePressed(MouseEvent e) {
            state = 2;
            return true;
        }

        @Override
        public boolean mouseReleased(MouseEvent e) {
            state = 1;
            return true;
        }

        @Override
        public boolean mouseEntered(MouseEvent e) {
            state = 1;
            return true;
        }

        @Override
        public boolean mouseExited(MouseEvent e) {
            state = 0;
            return true;
        }
    }
    
}
