package gui;

import game.Player;
import gui.core.ActionListener;
import gui.core.Button;
import gui.core.Component;
import gui.core.InteractiveComponent;
import static gui.core.InteractiveComponent.propagateMouseEvent;
import gui.core.MouseEvent;
import gui.core.Parent;
import java.util.ArrayList;
import java.util.List;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.event.KeyEvent;

/**
 * A frame which includes market items.
 * 
 * @author Bugra Felekoglu
 */
public class MarketFrame extends Frame implements ActionListener {
    
    private PImage background;
    private Player player;
    
    private int cart = 0;
    
    private final Button buyBtn = new Button();
    private MarketItem volcano;
    private MarketItem oneBounce;
    private MarketItem bounce;
    private MarketItem atom;
    private final List<MarketItem> marketItems = new ArrayList<>(4);
    
    private final Counter volcanoCounter = new Counter();
    private final Counter oneBounceCounter = new Counter();
    private final Counter bounceCounter = new Counter();
    private final Counter atomCounter = new Counter();
    private final List<Counter> counters = new ArrayList<>(4);
    
    public void setPlayer(Player player) {
        this.player = player;
    }
    
    @Override
    public void init(PApplet context){
        setSize(1080, 580);
        setLocation(100, 40);
        background = context.loadImage("background/market.png");
        setBackground(background);
        
        volcano = new MarketItem("volcano", 100);
        oneBounce = new MarketItem("volcano", 200);
        bounce = new MarketItem("volcano", 300);
        atom = new MarketItem("volcano", 400);
        
        marketItems.add(volcano);
        marketItems.add(oneBounce);
        marketItems.add(bounce);
        marketItems.add(atom);
        
        counters.add(volcanoCounter);
        counters.add(oneBounceCounter);
        counters.add(bounceCounter);
        counters.add(atomCounter);
        
        add(buyBtn, volcano, bounce, oneBounce, atom,
            volcanoCounter, bounceCounter, oneBounceCounter, atomCounter);
        
        buyBtn.setStateImages(context.
                loadImage("component/button/buy_btn.png"), false);
        buyBtn.setLocation(812, 467);
        buyBtn.setSize(240, 95);
        buyBtn.setMnemonic(66);     // mnemonic => "b"
        buyBtn.addActionListener(this); 
        
        volcano.setStateImages(context.
                loadImage("component/button/volcano.png"));
        volcano.setLocation(54, 105);
        volcano.setSize(290, 130);
        volcano.setFocusKey(49);    // mnemonic => "1"
        volcano.setCounter(volcanoCounter);
        volcanoCounter.setLocation(340, 110);
        
        bounce.setStateImages(context.
                loadImage("component/button/volcano.png"));
        bounce.setLocation(394, 105);
        bounce.setSize(290, 130);
        bounce.setFocusKey(50);    // mnemonic => "2"
        bounce.setCounter(bounceCounter);
        bounceCounter.setLocation(680, 110);
        
        oneBounce.setStateImages(context.
                loadImage("component/button/volcano.png"));
        oneBounce.setLocation(734, 105);
        oneBounce.setSize(290, 130);
        oneBounce.setFocusKey(51);    // mnemonic => "3"
        oneBounce.setCounter(oneBounceCounter);
        oneBounceCounter.setLocation(1020, 110);
        
        atom.setStateImages(context.
                loadImage("component/button/volcano.png"));
        atom.setLocation(54, 275);
        atom.setSize(290, 130);
        atom.setFocusKey(52);    // mnemonic => "4"
        atom.setCounter(atomCounter);
        atomCounter.setLocation(340, 280);
        
        for (Counter counter : counters)
            counter.setSize(25, 25);
        
        for (MarketItem item : marketItems)
            item.addActionListener(this);
        
        super.init(context);
    }
    
    @Override
    public void draw(PGraphics g) {
        super.draw(g);
                 
        g.pushMatrix();
        g.textAlign(g.LEFT, g.TOP);
        g.fill(255);
        g.textSize(30);
        g.text(player.getMoney(), 85, 505); // Current money
        g.text(cart, 355, 505);      // Shopping Cart
        if(player.getMoney() - cart < 0)
            g.fill(0xFFDF2D29);
        g.text(player.getMoney() - cart, 655, 505);   // Balance
        g.popMatrix();
    }
    
    @Override
    public void actionPerformed(Component comp) {
        if (comp == closeBtn) {
            clearMarket();
            ((ScreenManager)getContext()).closeFrame();
        }
        else if (comp == buyBtn && player.getMoney() != 0) {
            for (MarketItem btn : marketItems)
                if(btn.getQuantity() > 0)
//                    player.getInventory().add(0, btn.getQuantity());
            player.updateCash(-cart);
            clearMarket();
        }
        else if (comp instanceof MarketItem) {
            for (MarketItem item : marketItems){
                if(comp == item){
                    cart += item.charge();
                    item.getCounter().setQuantity(item.getQuantity());
                    if(cart > player.getMoney())
                        buyBtn.setEnabled(false);
                    else
                        buyBtn.setEnabled(true);
                }
            }
        }
    }
    
    public void clearMarket() {
        cart = 0;
        buyBtn.setEnabled(true);
        for (MarketItem item : marketItems){
            item.clear();
            item.getCounter().setQuantity(0);
        }
            
    }
    
    protected class Counter extends Component {
        private int quantity = 0;
        
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
        
        @Override
        public void draw(PGraphics g){
            if(quantity > 0){
                g.pushMatrix();
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
                g.popMatrix();
            }
        }
    }
}