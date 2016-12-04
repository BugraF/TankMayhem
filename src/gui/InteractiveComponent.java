package gui;

import processing.event.KeyEvent;

/**
 * Adapter Component
 * @author Burak Gök
 */
public abstract class InteractiveComponent extends Component 
    implements KeyListener, MouseListener {
    
    //    protected final List<KeyListener> keyListeners = new ArrayList<>(1);
//    protected final List<MouseListener> mouseListeners = new ArrayList<>(1);
//    
//    public boolean addKeyListener(KeyListener listener) {
//        if (keyListeners.contains(listener))
//            return false;
//        keyListeners.add(listener);
//        return true;
//    }
//    public boolean removeKeyListener(KeyListener listener) {
//        return keyListeners.remove(listener);
//    }
//    
//    public boolean addMouseListener(MouseListener listener) {
//        if (mouseListeners.contains(listener))
//            return false;
//        mouseListeners.add(listener);
//        return true;
//    }
//    public boolean removeMouseListener(MouseListener listener) {
//        return mouseListeners.remove(listener);
//    }
    
//    protected KeyListener keyListener = null;
//    protected MouseListener mouseListener = null;
    
//    public void setKeyListener(KeyListener listener) {
//        keyListener = listener;
//    }
//    public void setMouseListener(MouseListener listener) {
//        mouseListener = listener;
//    }
    
    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    
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
