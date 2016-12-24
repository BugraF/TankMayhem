package gui;

import game.Player;
import gui.core.ActionListener;
import gui.core.Button;
import gui.core.Component;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * A frame which includes market items.
 * 
 * @author Bugra Felekoglu
 */
public class MarketFrame extends Frame implements ActionListener {
    
    private PImage background;
    private final Button buyBtn = new Button();
    private Player player;
    
    public void setPlayer(Player player) {
        this.player = player;
    }
    
    @Override
    public void init(PApplet context){
        super.init(context);
        setSize(1080, 580);
        setLocation(100, 40);
        background = context.loadImage("background/market.png");
        setBackground(background);
        
        add(buyBtn);
        
        buyBtn.setStateImages(context.
                loadImage("component/button/buy_btn.png"), false);
        buyBtn.setLocation(812, 467);
        buyBtn.setSize(240, 95);
        buyBtn.setMnemonic(66);      // mnemonic => "b"
        buyBtn.addActionListener(this); 
    }
    
    @Override
    public void draw(PGraphics g) {
        super.draw(g);
        g.textAlign(g.LEFT, g.TOP);
        
        g.textSize(32);
        g.text("Money", 80, 460);
        
        g.textSize(30);
        g.text(player.getMoney(), 95, 505); // Current money
        g.text("4534", 355, 505);   // Shopping Cart
        g.text("3421", 655, 505);   // Balance
    }
    
    @Override
    public void actionPerformed(Component comp) {
        super.actionPerformed(comp);
        if (comp == buyBtn) {
            
        }
    }
}