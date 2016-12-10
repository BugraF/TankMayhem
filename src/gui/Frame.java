package gui;

/**
 * A scheme for mini screens (frames). 
 *      #HelpFrame
 *      #MarketFrame
 *      #PauseFrame
 *      #CreditsFrame
 * 
 * @author Bugra Felekoglu
 */
public class Frame extends Parent implements ActionListener{
    
    private final Button closeBtn = new Button();
    
    public Frame(){
        closeBtn.setStateImages(getContext()
                .loadImage("component/button/close_btn.png"));
        closeBtn.setFreeShape(true);
        closeBtn.setLocation(160, 333);
        closeBtn.setSize(300, 200);
        closeBtn.addActionListener(this);
    }

    @Override
    public void actionPerformed(Component comp) {
        if(comp == closeBtn)
            ((ScreenManager)getContext()).closeFrame();
    }
}