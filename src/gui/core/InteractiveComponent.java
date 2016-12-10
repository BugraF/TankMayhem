package gui.core;

import processing.event.KeyEvent;

/**
 * Adapter Component
 * @author Burak Gök
 */
public abstract class InteractiveComponent extends Component 
    implements KeyListener, MouseListener {
    
    // Multiple Listeners
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
    
    // Single Listener
//    protected KeyListener keyListener = null;
//    protected MouseListener mouseListener = null;
    
//    public void setKeyListener(KeyListener listener) {
//        keyListener = listener;
//    }
//    public void setMouseListener(MouseListener listener) {
//        mouseListener = listener;
//    }
    
    /*
    Common event handling logic can easily be defined in handleXXXEvent()
    methods. Propagation methods call the relevant event handling methods of the
    specified listener. Events must be passed to child components via "handle"
    methods since propagation methods only visit one level. Event passing is
    done by parents and so only concerns them. After "handle..event" methods are
    introduced, it has become easier to implement multiple listeners since this
    class handles the events in "handle" methods, not the actual event methods.
    */
    
    public void handleKeyEvent(KeyEvent event) {
        if (enabled)
            propagateKeyEvent(this, event);
    }
    
    public static void propagateKeyEvent(KeyListener listener, KeyEvent event) {
        switch (event.getAction()) {
            case KeyEvent.PRESS:
                listener.keyPressed(event);
                break;
            case KeyEvent.RELEASE:
                listener.keyReleased(event);
                break;
            case KeyEvent.TYPE:
                listener.keyTyped(event);
                break;
        }
    }
    
    public boolean handleMouseEvent(MouseEvent event) {
        if (!enabled) return true;
        return propagateMouseEvent(this, event);
    }
    
    public static boolean propagateMouseEvent(MouseListener listener, 
            MouseEvent event) {
        switch (event.getAction()) {
            case processing.event.MouseEvent.PRESS:
                return listener.mousePressed(event);
            case processing.event.MouseEvent.RELEASE:
                return listener.mouseReleased(event);
            case processing.event.MouseEvent.CLICK:
                return listener.mouseClicked(event);
            case processing.event.MouseEvent.DRAG:
                return listener.mouseDragged(event);
            case processing.event.MouseEvent.MOVE:
                return listener.mouseMoved(event);
            case processing.event.MouseEvent.ENTER:
                return listener.mouseEntered(event);
            case processing.event.MouseEvent.EXIT:
                return listener.mouseExited(event);
            case processing.event.MouseEvent.WHEEL:
                return listener.mouseWheel(event);
            default:
                return false;
        }
    }
    
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
