package gui;

/**
 *
 * @author Burak Gök
 */
public class KeyConflictException extends RuntimeException {

    /**
     * Constructs an instance of <code>KeyConflictException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public KeyConflictException(int key, KeyListener comp) {
        super("The hot-key " + Key.toString(key) + " is already used by " 
                + comp.toString());
    }
}
