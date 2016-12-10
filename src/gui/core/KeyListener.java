package gui.core;

import processing.event.KeyEvent;

/**
 * The components intended be to responsive to key events must implement this
 * interface. Any key event (triggered by any key) can be received by the
 * implementor classes, so the received key must be checked. To make a component
 * responsive to a set of keys, which is inside a Parent object, a focus key
 * should be used to route the key events to the component. In this manner,
 * the keys in which the component is interested can be listened to by
 * filtering the received key events.
 * 
 * @author Burak Gök
 */
public interface KeyListener {
    void keyPressed(KeyEvent e);
    void keyReleased(KeyEvent e);
    void keyTyped(KeyEvent e);
}
