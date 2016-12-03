package game;

/**
 * @author Burak Gök
 */
public interface SelectionChangeListener {
    /**
     * Indicates that the selected item has changed.
     * @param itemId Id of the currently selected item
     */
    void selectionChanged(int itemId);
}
