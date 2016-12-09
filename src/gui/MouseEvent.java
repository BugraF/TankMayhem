package gui;

/**
 * Wrapper class for processing.event.MouseEvent
 * Offers low memory usage by mutability
 * @author Burak Gök
 */
public class MouseEvent {
    
    private final processing.event.MouseEvent event;
    private int translateX, translateY;
    
    public MouseEvent(processing.event.MouseEvent event) {
        this.event = event;
    }
    
    public int getAction() {
        return event.getAction();
    }
    
    public MouseEvent translate(int x, int y) {
        translateX = x;
        translateY = y;
        return this;
    }
    
    public int getX() {
        return event.getX() + translateX;
    }
    
    public int getY() {
        return event.getY() + translateY;
    }
    
    public int getXOnScreen() {
        return event.getX();
    }
    
    public int getYOnScreen() {
        return event.getY();
    }
    
    // TODO Implement others & copy their documentation
}
