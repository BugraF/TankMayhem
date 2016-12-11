package gui.core;

/**
 * Wrapper class for processing.event.MouseEvent
 * Offers low memory usage by mutability
 * @author Burak Gök
 */
public class MouseEvent {
    
    private final processing.event.MouseEvent event;
    private int translateX, translateY;
    private int action;
    
    public MouseEvent(processing.event.MouseEvent event) {
        this.event = event;
        this.action = event.getAction();
    }
    
    public MouseEvent derive(int action) {
        this.action = action;
        return this;
    }
    
    public MouseEvent revert() {
        action = event.getAction();
        return this;
    }
    
    public int getAction() {
        return action;
    }
    
    public MouseEvent translate(int x, int y) {
        translateX += x;
        translateY += y;
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
