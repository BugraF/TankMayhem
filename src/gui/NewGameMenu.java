package gui;

import game.Game;
import game.GameManager;
import game.Player;
import static game.Player.Mode;
import gui.core.Component;
import gui.core.ActionListener;
import gui.core.Button;
import gui.core.Parent;
import java.util.ArrayList;
import java.util.List;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

/**
 * New Game Menu Screen
 * 
 * @author Bugra Felekoglu
 */
public class NewGameMenu extends Parent implements ActionListener{
        
    /**
     * Background Image
     */
    private PImage background; 
    private PFont font;
    
    private String map = "desert";
    
    /**
     * Buttons
     */
    private final Button start_btn = new Button();
    private final Button back_btn = new Button();
    
    private final ListButton snowy = new ListButton();
    private final ListButton rainy = new ListButton();
    private final ListButton desert = new ListButton();
    private final ListButton sunny = new ListButton();
    
    private final List<ListButton> listButtons = new ArrayList<>(4);
    
    @Override
    public void init(PApplet context) {
        font = context.createFont("font/seguibl.ttf", 40);
        
        background = context.loadImage("background/new_game_menu.png");
        setBackground(background);
        
        add(start_btn, back_btn, snowy, rainy, desert, sunny);
        listButtons.add(snowy);
        listButtons.add(rainy);
        listButtons.add(desert);
        listButtons.add(sunny);
        
        start_btn.setStateImages(context
                .loadImage("component/button/start_btn.png"), true);
        start_btn.setFreeShape(true);
        start_btn.setLocation(980, 648);
        start_btn.setSize(300, 120);
        start_btn.addActionListener(this);
        start_btn.setMnemonic(10);    // mnemonic => ENTER
        
        back_btn.setStateImages(context
                .loadImage("component/button/back_btn.png"), true);
        back_btn.setFreeShape(true);
        back_btn.setLocation(0, 688);
        back_btn.setSize(200, 80);
        back_btn.addActionListener(this);
        back_btn.setMnemonic(27);    // mnemonic => ESC
        
        snowy.setAttribute("snowy");
        snowy.setText("SNOWY MOUNTAINS");
        snowy.setFont(font);
        snowy.setLocation(970, 198);
        snowy.setSize(360, 90);
        snowy.addActionListener(this);
        
        rainy.setAttribute("rainy");
        rainy.setText("RAINY HILLS");
        rainy.setFont(font);
        rainy.setLocation(970, 303);
        rainy.setSize(360, 90);
        rainy.addActionListener(this);
        
        desert.setAttribute("desert");
        desert.setText("HOT DESERT");
        desert.setFont(font);
        desert.setLocation(970, 408);
        desert.setSize(360, 90);
        desert.setSelected(true);
        desert.addActionListener(this);
        
        sunny.setAttribute("sunny");
        sunny.setText("SUNNY FOREST");
        sunny.setFont(font);
        sunny.setLocation(970, 513);
        sunny.setSize(360, 90);
        sunny.addActionListener(this);
        
        
    }
    
    @Override
    public void actionPerformed(Component comp) {
        if (comp == start_btn) {
            Game game = GameManager.getInstance().startNewGame(map, 
            new Player[] { // Sample Data
                new Player("Burak", Mode.Armored, 0xFFFF0000),
                new Player("Buğra", Mode.Armored, 0xFF0000FF)
            });
            Parent screen = ((ScreenManager)getContext())
                    .switchScreen(ScreenManager.SCREEN_GAME);
            ((GameScreen)screen).attachGame(game);
            game.start();
        }
        else if (comp == back_btn) {
            ((ScreenManager)getContext())
                    .switchScreen(ScreenManager.SCREEN_MAIN_MENU);
        }
        else if (comp instanceof ListButton) {
            for (ListButton btn : listButtons)
                if (btn.isSelected())
                    btn.setSelected(false);
            ((ListButton)comp).setSelected(true);
            map = ((ListButton)comp).getAttribute();
        }
            
    }
}