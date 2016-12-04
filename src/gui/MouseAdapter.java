package gui;

//import processing.event.MouseEvent;

/**
 *
 * @author Burak Gök
 */
public abstract class MouseAdapter implements MouseListener {

    @Override
    public boolean mousePressed(MouseEvent e) {
        return true;
    }

    @Override
    public boolean mouseReleased(MouseEvent e) {
        return true;
    }

    @Override
    public boolean mouseClicked(MouseEvent e) {
        return true;
    }

    @Override
    public boolean mouseDragged(MouseEvent e) {
        return true;
    }

    @Override
    public boolean mouseMoved(MouseEvent e) {
        return true;
    }

    @Override
    public boolean mouseEntered(MouseEvent e) {
        return true;
    }

    @Override
    public boolean mouseExited(MouseEvent e) {
        return true;
    }

    @Override
    public boolean mouseWheel(MouseEvent e) {
        return true;
    }
    
}
