package gui;

import processing.event.MouseEvent;

/**
 * The components intended be to responsive to mouse events must implement this
 * interface. When a mouse event is received, it can be marked as unhandled by
 * returning false, so that the event can be passed to another component. This
 * feature is mainly used by free-shape (or non-rectangular) components.
 * 
 * @author Burak Gök
 */
public interface MouseListener {
    boolean mousePressed(MouseEvent e);
    boolean mouseReleased(MouseEvent e);
    boolean mouseClicked(MouseEvent e);
    boolean mouseDragged(MouseEvent e);
    boolean mouseMoved(MouseEvent e);
    boolean mouseEntered(MouseEvent e);
    boolean mouseExited(MouseEvent e);
    boolean mouseWheel(MouseEvent e);
}
