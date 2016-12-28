package gui;

import game.Catalog;
import game.CatalogItem;
import game.Game;
import game.Player;
import gui.core.ActionListener;
import gui.core.Button;
import gui.core.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.event.KeyEvent;

/**
 * A frame which includes market items.
 * 
 * @author Bugra Felekoglu
 */
public class MarketFrame extends Frame implements ActionListener {
    /** Context of this market */
    private Game game;
    private Player player;
    
    private PImage background;
    
    private int cart = 0;
    
    private final Button buyBtn = new Button();
    private final List<MarketItem> items = new ArrayList<>(4);
    
    private final static Map<String, Integer> priceTable = new HashMap<>();
    static {
        priceTable.put("simple_bomb", 100);
        priceTable.put("bouncing_bomb", 300);
        priceTable.put("1bounce_bomb", 200);
        priceTable.put("volcano_bomb", 250);
    }
    
    private final static int hpad = 50, vpad = 40;
    
    public void attachGame(Game game) {
        this.game = game;
        
        int x = hpad, y = 105;
        Catalog catalog = game.getCatalog();
        Iterator<CatalogItem> iterator = catalog.iterator();
        while (iterator.hasNext()) {
            CatalogItem item = iterator.next();
            if (catalog.isBomb(item.getId())) {
                MarketItem marketItem = new MarketItem(
                        item, priceTable.get(item.getKey()));
                add(marketItem, marketItem.getCounter());
                marketItem.setLocation(x, y);
                marketItem.setSize(290, 130);
                marketItem.getCounter().init();
                marketItem.addActionListener(this);
                items.add(marketItem);
                x += 290 + hpad;
                if (x + 290 + hpad > width) {
                    x = hpad;
                    y += 130 + vpad;
                }
            }
        }
    }
    
    public void detachGame() {
        this.game = null;
        Iterator<MarketItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            MarketItem item = iterator.next();
            remove(item);
            iterator.remove();
        }
    }
    
    void playerChanged() {
        player = game.getCurrentPlayer();
    }
    
    @Override
    public void init(PApplet context){
        setSize(1080, 580);
        setLocation(100, 40);
        background = context.loadImage("background/market.png");
        setBackground(background);
        
        add(buyBtn);
        
        buyBtn.setStateImages(context.
                loadImage("component/button/buy_btn.png"), false);
        buyBtn.setLocation(812, 467);
        buyBtn.setSize(240, 95);
        buyBtn.setMnemonic(10);     // mnemonic => "Enter"
        buyBtn.addActionListener(this);
        
        super.init(context);
    }
    
    @Override
    protected void drawComponents(PGraphics g) {
        super.drawComponents(g);
        
        g.textAlign(g.LEFT, g.TOP);
        g.fill(255);
        g.textSize(30);
        g.text(player.getMoney(), 85, 505); // Current money
        g.text(cart, 355, 505);      // Shopping Cart
        if(player.getMoney() - cart < 0)
            g.fill(0xFFDF2D29);
        g.text(player.getMoney() - cart, 655, 505);   // Balance
    }
    
    private int lastItem = 0;

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 37 || e.getKeyCode() == 39) { // <- / ->
            items.get(lastItem).setHighlighted(false);
            int nav = e.getKeyCode() == 37 ? -1 : 1;
            do {
                lastItem = Math.floorMod(lastItem + nav, items.size());
            } while (!items.get(lastItem).isEnabled());
            items.get(lastItem).setHighlighted(true);
            setFocusedChild(items.get(lastItem));
        }
    }
    
    @Override
    public void actionPerformed(Component comp) {
        if (comp == closeBtn) {
            reset();
            ((ScreenManager)getContext()).closeFrame();
        }
        else if (comp == buyBtn) {
            for (MarketItem marketItem : items) {
                int itemId = marketItem.getItem().getId();
                int quantity = marketItem.getCounter().getQuantity();
                if(quantity > 0)
                    player.getInventory().add(itemId, quantity);
            }
            player.updateCash(-cart);
            ((GameScreen)owner).purchaseMade();
            actionPerformed(closeBtn);
        }
        else if (comp instanceof MarketItem) {
            for (MarketItem item : items){
                if(comp == item){
                    cart += item.getCounter().collect();
                    buyBtn.setEnabled(cart <= player.getMoney());
                }
            }
        }
    }
    
    private void reset() {
        cart = 0;
        buyBtn.setEnabled(true);
        items.forEach((item) -> item.getCounter().setQuantity(0));
    }
    
}