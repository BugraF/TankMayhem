package gui;

import game.Game;
import game.Player;
import game.Stage;
import gui.core.Component;
import gui.core.ActionListener;
import gui.core.Parent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Game Screen
 * 
 * @author Bugra Felekoglu
 */
public class GameScreen extends Parent implements ActionListener{
    
    private PFont font;
    
    private Game game;
    private Stage stage;
    
    private final ScoreBoard scoreBoard = new ScoreBoard();
    
    private final MoneyDisplay moneyDisplay = new MoneyDisplay();
    private final Legend legend = new Legend();
    
    public void setGame(Game game) {
        this.game = game;
        scoreBoard.setPlayers(game.getPlayers());
    }
    
    @Override
    public void init(PApplet context) {
        font = context.createFont("font/seguibl.ttf", 60);
        
        add(stage, legend, moneyDisplay, scoreBoard);
        
        
        legend.setLocation(0, 648);
        legend.setSize(1280, 120);
        
        moneyDisplay.setIcon(context.
                loadImage("component/display/money_img.png"));
        moneyDisplay.setLocation(870, 690);
        moneyDisplay.setSize(180, 50);
        moneyDisplay.setMoney(game.getCurrentPlayer().getCash());
        
        scoreBoard.setLocation(960, 10);
        scoreBoard.setSize(320, 200);
        
    }
    
    @Override
    public void actionPerformed(Component comp) {
        
    }
    
    private class ScoreBoard extends Component {
        
        private Player[] players;
        private int loc;
        
        public void setPlayers(Player[] players) {
            this.players = players;
        }
        
        @Override
        public void draw(PGraphics g){
            Collections.sort(Arrays.asList(players), 
                    (p1, p2) -> p1.getScore() - p2.getScore());
            loc = 0;
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
    
    private class MoneyDisplay extends Component {
        
        private PImage icon;
        private String cash;
        
        public void setIcon(PImage icon) {
            this.icon = icon;
        }
        
        public void setMoney(int cash) {
            this.cash = "" + cash;
        }
    
        @Override
        public void draw(PGraphics g){
            g.fill(255);
            g.textFont(font, 30);
            g.textAlign(g.LEFT, g.TOP);
            g.text(cash, 60, 0);
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