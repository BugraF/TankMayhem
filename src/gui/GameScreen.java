package gui;

import game.Game;
import game.Player;
import game.Stage;
import game.Tank;
import gui.core.Component;
import gui.core.ActionListener;
import gui.core.Parent;
import java.util.Arrays;
import java.util.Collections;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.event.KeyEvent;

/**
 * Game Screen
 * 
 * @author Bugra Felekoglu
 */
public class GameScreen extends Parent implements ActionListener {
    
    private PFont font;
    
    private Game game;
    private Stage stage;
    
    private final StatusDisplay statusDisplay = new StatusDisplay();
    private final WindDisplay windDisplay = new WindDisplay();
    private final ScoreBoard scoreBoard = new ScoreBoard();
    private final MoneyDisplay moneyDisplay = new MoneyDisplay();
    private final Legend legend = new Legend();
    
    public void setGame(Game game) {
        this.game = game;
        scoreBoard.setPlayers(game.getPlayers());
        stage = game.getStage();
        add(stage);
        setFocusedChild(stage);
        stage.setSize(1280, 648);
        game.selectionChanged(0);
        
        add(stage, legend, moneyDisplay, scoreBoard, windDisplay, statusDisplay);
    }
    
    @Override
    public void init(PApplet context) {
        super.init(context);
        font = context.createFont("font/seguibl.ttf", 60);
        moneyDisplay.setIcon(context.
                loadImage("component/display/money_icon.png"));
        windDisplay.setIcon(context.
                loadImage("component/display/wind_icon.png"));
        statusDisplay.setHealthIcon(context.
                loadImage("component/display/health_icon.png"));
        statusDisplay.setFuelIcon(context.
                loadImage("component/display/fuel_icon.png"));
        statusDisplay.setBar(context.
                loadImage("component/display/bar.png"));
        
        statusDisplay.setLocation(10, 660);
        statusDisplay.setSize(368, 98);
        windDisplay.setLocation(15, 10);
        windDisplay.setSize(300, 56);
        legend.setLocation(0, 648);
        legend.setSize(1280, 120);
        moneyDisplay.setLocation(870, 690);
        moneyDisplay.setSize(180, 50);
        scoreBoard.setLocation(960, 10);
        scoreBoard.setSize(320, 200);
    }

    @Override
    public void draw(PGraphics g) {
        game.update();
        super.draw(g);
    }
    
    @Override
    public void actionPerformed(Component comp) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) { // Test
        if (e.getKeyCode() == 32) // Space
            game.getStage().finalizeInteraction();
    }
    
    private class ScoreBoard extends Component {
        
        private Player[] players;
        
        public void setPlayers(Player[] players) {
            this.players = players;
        }
        
        @Override
        public void draw(PGraphics g){
            Collections.sort(Arrays.asList(players), 
                    (p1, p2) -> p1.getScore() - p2.getScore());
            int loc = 0;
            for(Player player : players){
                if(player == game.getCurrentPlayer())
                    g.fill(player.getColor());
                else
                    g.fill(255);

                if(!player.isAlive())
                    g.fill(0);

                g.textFont(font, 35);
                
                g.textAlign(g.LEFT, g.TOP);
                g.text(player.getName() + ": ", 0, loc);
                
                g.textAlign(g.RIGHT, g.TOP);
                g.text(player.getScore(), width - width/16, loc);
                loc += 50;
            }
        }
    }
    
    private class StatusDisplay extends Component {
        
        private PImage healthIcon;
        private PImage fuelIcon;
        private PImage bar;
        
//        private final int maxHP = ;
//        private final int maxFuel = ;
        
        public void setHealthIcon(PImage healthIcon) {
            this.healthIcon = healthIcon;
        }
        
        public void setFuelIcon(PImage fuelIcon) {
            this.fuelIcon = fuelIcon;
        }
        
        public void setBar(PImage bar) {
            this.bar = bar;
        }
               
        @Override
        public void draw(PGraphics g) {
            
//            g.fill(255, 150);
//            g.rect(healthIcon.width + 9 + 4, 
//                  (healthIcon.height - bar.height)/2, 
//                  bar.width, bar.height);
//            g.tint(231, 76, 60);
//            g.image(bar, healthIcon.width + 9 + 4, 
//                        (healthIcon.height - bar.height)/2);
//            g.tint(255);
//            
//            g.fill(255);
//            // Indicators
//            g.rect(healthIcon.width + 9,
//                  (healthIcon.height - 35)/2, 
//                  4, 35);
//            g.rect(width - 4,
//                  (healthIcon.height - 35)/2, 
//                  4, 35);
//            g.rect(width - 4 - (width - 8 - (healthIcon.width + 9))/2 - 2,
//                  (healthIcon.height - 17)/2, 
//                  4, 17);
//            g.rect(width - 4 - (width - 8 - (healthIcon.width + 9))/4 - 2,
//                  (healthIcon.height - 8)/2, 
//                  4, 8);
//            g.rect(width - 4 - 3*(width - 8 - (healthIcon.width + 9))/4 - 2,
//                  (healthIcon.height - 8)/2, 
//                  4, 8);
            Tank tank = game.getCurrentPlayer().getTank();
            float hp = bar.width * tank.getHP() / 100;
            float fuel = bar.width * tank.getFuel() / 100;
            
            // Health Icon
            g.image(healthIcon, 0, 0);

            // Health Bar 
            g.fill(255, 150);
            g.rect(64, 9, (int)hp, bar.height);
            g.tint(231, 76, 60);
            g.image(bar.get(0, 0, (int)hp, bar.height), 64, 9);
            g.tint(255);
            
            // Indicators
            g.fill(255);
            g.rect(60, 5, 4, 35);
            g.rect(364, 5, 4, 35);
            g.rect(212, 14, 4, 17);
            g.rect(137, 18, 4, 8);
            g.rect(287, 18, 4, 8); 
           
            // Fuel Icon
            g.image(fuelIcon, 0, 53);
            
            // Fuel Bar
            g.fill(255, 150);
            g.rect(64, 62, (int)fuel, bar.height);
            g.tint(255, 204, 0);
            g.image(bar.get(0, 0, (int)fuel, bar.height), 64, 62);
            g.tint(255);
            
            // Indicators
            g.fill(255);
            g.rect(60, 58, 4, 35);
            g.rect(364, 58, 4, 35);
            g.rect(212, 67, 4, 17);
            g.rect(137, 71, 4, 8);
            g.rect(287, 71, 4, 8); 
            
            
        }
    }
    
    private class WindDisplay extends Component {
        
        private PImage icon;
        
        public void setIcon(PImage icon) {
            this.icon = icon;
        }
        
        @Override
        public void draw(PGraphics g) {
            int wind = game.getWind();
            g.fill(255);
            g.textFont(font, 35);
            g.textAlign(g.CENTER, g.CENTER);
            g.text(Math.abs(wind) + " kph", 150, 22); // size 156, 58
            g.fill(0);
            
            int tintLeft = wind >= 0 ? 20 : 255;
            int tintRight = wind <= 0 ? 20 : 255;
            
            g.tint(255, tintLeft);  
            g.pushMatrix();
            g.scale(-1, 1);
            g.image(icon, -icon.width, 0);  
            g.popMatrix();

            g.tint(255, tintRight);
            g.image(icon, 228, 0);
            g.noTint();
        }
    }
    
    private class MoneyDisplay extends Component {
        
        private PImage icon;
        
        public void setIcon(PImage icon) {
            this.icon = icon;
        }
        
        @Override
        public void draw(PGraphics g){
            g.fill(255);
            g.textFont(font, 30);
            g.textAlign(g.LEFT, g.TOP);
            g.text(game.getCurrentPlayer().getMoney(), 60, 0);
            g.image(icon, 0, 0);
        }
    }
    
    private class Legend extends Component {
        
        @Override
        public void draw(PGraphics g){
            g.fill(40, 40, 40);
            g.rect(0, 0, width, height);
        }
    }
}