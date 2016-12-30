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
import java.util.Observable;
import java.util.Observer;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Game Screen
 *
 * @author Bugra Felekoglu
 */
public class GameScreen extends Parent implements ActionListener, Observer {

    private PFont font;

    private Game game;
    private Stage stage;
    
    private final Legend legend = new Legend();
    private final ScoreBoard scoreBoard = new ScoreBoard();
    private final MoneyDisplay moneyDisplay = new MoneyDisplay();
    private final WindDisplay windDisplay = new WindDisplay();
    private final StatusDisplay statusDisplay = new StatusDisplay();
    private final LaunchButton launchBtn = new LaunchButton();
    private final FlatButton marketBtn = new FlatButton();
    private final FlatButton pauseBtn = new FlatButton();
    private final InventoryView inventoryView = new InventoryView();

    public void attachGame(Game game) {
        this.game = game;
        game.addTurnListener(this);
        scoreBoard.setPlayers(game.getPlayers());
        stage = game.getStage();
        add(0, stage);
        setFocusedChild(stage);
        stage.setSize(1280, 648);
        
        inventoryView.attachGame(game);
        ((MarketFrame)((ScreenManager)getContext())
                .getFrame(ScreenManager.FRAME_MARKET)).attachGame(game);
        launchBtn.setEnabled(true);
        marketBtn.setEnabled(true);
    }
    
    public Game detachGame() {
        game.removeTurnListener(this);
        Game temp = game;
        game = null;
        scoreBoard.setPlayers(null);
        remove(stage);
        setFocusedChild(null);
        inventoryView.detachGame();
        ((MarketFrame)((ScreenManager)getContext())
                .getFrame(ScreenManager.FRAME_MARKET)).detachGame();
        return temp;
    }

    @Override
    public void init(PApplet context) {
        add(legend, moneyDisplay, scoreBoard, windDisplay, statusDisplay,
                 launchBtn, marketBtn, pauseBtn, inventoryView);
        
        font = context.createFont("font/seguibl.ttf", 60);
        moneyDisplay.setIcon(context.
                loadImage("component/display/money_icon.png"));
        windDisplay.setIcon(context.
                loadImage("component/display/wind_icon.png"));
        statusDisplay.setIcons(context.
                loadImage("component/display/health_icon.png"),context.
                loadImage("component/display/fuel_icon.png"));
        statusDisplay.setBar(context.
                loadImage("component/display/bar.png"));
        launchBtn.setStateImages(context.
                loadImage("component/button/launch_btn.png"), false);
        marketBtn.setImage(context.
                loadImage("component/button/market_btn.png"));
        pauseBtn.setImage(context.
                loadImage("component/button/pause_btn.png"));

        legend.setLocation(0, 648);
        legend.setSize(1280, 120);
        scoreBoard.setLocation(960, 10);
        scoreBoard.setSize(320, 200);
        moneyDisplay.setLocation(870, 690);
        moneyDisplay.setSize(180, 50);
        windDisplay.setLocation(15, 10);
        windDisplay.setSize(300, 56);
        statusDisplay.setLocation(10, 660);
        statusDisplay.setSize(368, 98);
        inventoryView.setLocation(380, 10);
        inventoryView.setSize(520, 75);

        launchBtn.setLocation(410, 658);
        launchBtn.setSize(430, 100);
        launchBtn.setMnemonic(32);      // mnemonic => "space"
        launchBtn.addActionListener(this);
        marketBtn.setLocation(1070, 673);
        marketBtn.setSize(80, 70);
        marketBtn.setMnemonic(77);      // mnemonic => "m"
        marketBtn.addActionListener(this);
        pauseBtn.setLocation(1180, 673);
        pauseBtn.setSize(80, 70);
        pauseBtn.setMnemonic(27);       // mnemonic => "escape"
        pauseBtn.addActionListener(this);

        launchBtn.setFont(font);
        
        marketBtn.setTintValues(0xFFFFFFFF, 0xFFFFCC00, 0xFFFF9C00);
        pauseBtn.setTintValues(0xFFFFFFFF, 0xFFD70000, 0xFF7D0000);
        
        super.init(context); // Currently not used
    }

    @Override
    public void draw(PGraphics g) {
        if (enabled)
            game.update();
        super.draw(g);
    }

    @Override
    public void actionPerformed(Component comp) {
        if (comp == marketBtn) {
            Frame market = ((ScreenManager) getContext())
                    .showFrame(ScreenManager.FRAME_MARKET);
            ((MarketFrame) market).playerChanged();
        }
        else if (comp == pauseBtn) {
            Frame pauseMenu = ((ScreenManager) getContext())
                    .showFrame(ScreenManager.FRAME_PAUSE_MENU);
        }
        else if (comp == launchBtn) {
            stage.finalizeInteraction();
            launchBtn.setText("LOCKED IN", 60);
            launchBtn.setEnabled(false);
            marketBtn.setEnabled(false);
        }
    }
    
//    @Override
//    public void keyPressed(KeyEvent e) { // Test
//        if (e.getKeyCode() == 32){ // Space
//            actionPerformed(launchBtn);
//        }
//    }
    
    @Override
    public void update(Observable o, Object arg) {
        boolean gameOver = (boolean) arg;
        // Turn switched
        if (!gameOver) {
            inventoryView.playerChanged();
            launchBtn.setEnabled(true);
            marketBtn.setEnabled(true);
        }
        // Game is over
        else {
            Frame endGame = ((ScreenManager) getContext())
                    .showFrame(ScreenManager.FRAME_END_GAME);
            ((EndGameFrame) endGame).setWinner(game.getCurrentPlayer());
        }
    }
    
    void interactionChanged() { // Called by InventoryView
        launchBtn.setText(stage.getAction(), 75);
        launchBtn.setTint(stage.getActionColor());
    }
    
    void purchaseMade() { // Called by Market
        inventoryView.update();
    }

    private class ScoreBoard extends Component {

        private Player[] players;

        public void setPlayers(Player[] players) {
            this.players = players;
        }

        @Override
        public void draw(PGraphics g) {
            Collections.sort(Arrays.asList(players),
                    (p1, p2) -> p2.getScore() - p1.getScore());
            int loc = 0;
            for (Player player : players) {
                if (player == game.getCurrentPlayer()) 
                    g.fill(player.getColor());
                else
                    g.fill(255);

                if (!player.isAlive())
                    g.fill(0);

                g.textFont(font, 35);

                g.textAlign(g.LEFT, g.TOP);
                g.text(player.getName() + ": ", 0, loc);

                g.textAlign(g.RIGHT, g.TOP);
                g.text(player.getScore(), width - width / 16, loc);
                loc += 50;
            }
        }
    }

    private class StatusDisplay extends Component {

        private PImage healthIcon;
        private PImage fuelIcon;
        private PImage bar;

        public void setIcons(PImage healthIcon, PImage fuelIcon) {
            this.healthIcon = healthIcon;
            this.fuelIcon = fuelIcon;
        }

        public void setBar(PImage bar) {
            this.bar = bar;
        }

        @Override
        public void draw(PGraphics g) {
            Tank tank = game.getCurrentPlayer().getTank();
            float hp = bar.width * tank.getHP() / 100;
            float fuel = bar.width * tank.getFuel() / 100;

            // Health Icon
            g.image(healthIcon, 0, 0);

            // Health Bar 
            g.fill(255, 150);
            g.rect(64, 9, (int) hp, bar.height);
            g.tint(231, 76, 60);
            g.image(bar, 64, 9);
            g.tint(255);

            g.fill(0xFF282828);
            g.rect(64 + (int) hp, 9, bar.width - (int) hp, bar.height);

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
            g.rect(64, 62, (int) fuel, bar.height);
            g.tint(255, 204, 0);
            g.image(bar, 64, 62);
            g.tint(255);

            g.fill(0xFF282828);
            g.rect(64 + (int) fuel, 62, bar.width - (int) fuel, bar.height);

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
        public void draw(PGraphics g) {
            g.fill(255);
            g.textFont(font, 30);
            g.textAlign(g.LEFT, g.TOP);
            g.text(game.getCurrentPlayer().getMoney(), 60, 0);
            g.image(icon, 0, 0);
        }
    }

    private class Legend extends Component {

        @Override
        public void draw(PGraphics g) {
            g.fill(40, 40, 40);
            g.rect(0, 0, width, height);
        }
    }
}
