/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.MarketFrame.Counter;
import gui.core.ActionListener;
import gui.core.InteractiveComponent;
import static gui.core.InteractiveComponent.propagateMouseEvent;
import gui.core.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.event.KeyEvent;

/**
 *
 * @author Buğra Felekoğlu
 */
public class MarketItem extends InteractiveComponent {
        /**
         * Current state of this button.
         *      - 0: normal & hover
         *      - 1: pressed
         */
        private int state = 0;
        
        /**
        * Sprite sheet of the state images of this button.
        */
        protected PImage[] stateImages; // normal & hover, pressed

        /**
         * Name of the item
         */
        private String name;

        /**
         * Cost of this item
         */
        private int cost;
        
        /**
         * Current quantity of this button
         */
        private int quantity = 0;
        
        /**
         * Counter of item
         */
        private Counter counter;
        
        /**
         * isAdded: Status of the click, is item added or removed
         * hover: is mouse over the button
         */
        private boolean isAdded, 
                        hover = false;
        
        /**
         * Key code for focusing to this button.
         */
        private int focusKey;

        /**
         * The objects that listen to the invocation of this button.
         */
        private final List<ActionListener> actionListeners = new ArrayList<>(1);
        
        /**
         * Constructor that takes name and cost of the item which is known
         * before the creation of object
         */
        public MarketItem (String name, int cost) {
            this.name = name;
            this.cost = cost;
        }
        
        public int getQuantity() {
            return quantity;
        }
        
        public void updateQuantity(int num) {
            quantity += num;
        }
        
        public int charge() {
            if(isAdded == true){
                updateQuantity(1);
                return cost;
            }
            else{
                if (quantity > 0){
                    updateQuantity(-1);
                    return -cost;
                }
                else
                    return 0;
            }
        }
        
        public void setCounter(Counter counter) {
            this.counter = counter;
        }
        
        public Counter getCounter() {
            return counter;
        }
        
        public void clear() {
            quantity = 0;
        }
        
        /**
         * Sets the interested key for this button.
         * When this key is pressed, the button will be invoked and do its
         * click-action.
         * @param key Button mnemonic, can be a key combination.
         */
        public void setFocusKey(int key) {
            focusKey = key;
            if (parent != null)
                parent.associateKeys(this, key);
        }
        
        /**
         * Sets the state images for this button.
         * @param stateImages Sprite sheet of the state images of this button.
         *                    The images should be placed vertically.
         */
        public void setStateImages(PImage stateImages) {
            this.stateImages = new PImage[2];
            int stateHeight = stateImages.height / 2;
            for (int i = 0; i < 2; i++) {
                this.stateImages[i] = stateImages.get(0, i * stateHeight, 
                        stateImages.width, stateHeight);
                this.stateImages[i].loadPixels();
            }
        }
        
        @Override
        public void draw(PGraphics g) {
            g.image(stateImages[state], 0, 0);
            if (state == 0 && hover) {
                if(isAdded) {
                    g.pushMatrix();
                    g.fill(0xBB009640);
                    g.rect(width/2, 0, width/2, height, 0, 11, 11, 0);
                    g.popMatrix();
                }
                else {
                    g.pushMatrix();
                    g.fill(0xBBE30613);
                    g.rect(0, 0, width/2, height, 11, 0, 0, 11);
                    g.popMatrix();
                }
            }
            else if (state == 1) {
                if(isAdded) {
                    g.pushMatrix();
                    g.fill(0xBB009640);
                    g.rect(width/2, 10, width/2, height-10, 0, 11, 11, 0);
                    g.popMatrix();
                }
                else {
                    g.pushMatrix();
                    g.fill(0xBBE30613);
                    g.rect(0, 10, width/2, height-10, 11, 0, 0, 11);
                    g.popMatrix();
                }
            }
        }

        /**
         * Associates the specified action listener to this button.
         * @return False if this button is already associated to the specified
         *         action listener, true otherwise.
         */
        public boolean addActionListener(ActionListener listener) {
            if (actionListeners.contains(listener))
                return false;
            actionListeners.add(listener);
            return true;
        }

        /**
         * Disassociates the specified action listener from this button.
         * @return False if the specified action listener is not associated to this
         *         button, true otherwise.
         */
        public boolean removeActionListener(ActionListener listener) {
            return actionListeners.remove(listener);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == focusKey)
                parent.setFocusedChild(this);
            else if (e.getKeyCode() == 112){
                isAdded = false;
                state = 1;
                invoke();
            }      
            else if (e.getKeyCode() == 113){
                isAdded = true;
                state = 1;
                invoke();
            }          
        }
        
        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == focusKey)
                parent.setFocusedChild(this);
            else if (e.getKeyCode() == 112)
                state = 0;
            else if (e.getKeyCode() == 113)
                state = 0;
        }

        private void invoke() {
            for (ActionListener listener : actionListeners)
                listener.actionPerformed(this);
        }

        @Override
        public boolean handleMouseEvent(MouseEvent event) {
            if (event.getAction() != processing.event.MouseEvent.EXIT
                    && !consumeEvent(event)) return false;
            if (!enabled) return true;
            return propagateMouseEvent(this, event);
        }

        private boolean consumeEvent(MouseEvent e) {
            return true;
        }
        
        @Override
        public boolean mousePressed(MouseEvent e) {
            if(e.getX() < width/2 && e.getY() < height)
                isAdded = false;
            else 
                isAdded = true;
            state = 1;
            return true;
        }

        @Override
        public boolean mouseReleased(MouseEvent e) {
            if(e.getX() < width/2 && e.getY() < height)
                isAdded = false;
            else 
                isAdded = true;
            state = 0;
            hover = true;
            return true;
        }

        @Override
        public boolean mouseClicked(MouseEvent e) {
            if(e.getX() < width/2 && e.getY() < height)
                isAdded = false;
            else 
                isAdded = true;
            invoke();
            return true;
        }

        @Override
        public boolean mouseEntered(MouseEvent e) {
            if(e.getX() < width/2 && e.getY() < height)
                isAdded = false;
            else 
                isAdded = true;
            state = 0;
            hover = true;
            return true;
        }

        @Override
        public boolean mouseExited(MouseEvent e) {
            state = 0;
            hover = false;
            return true;
        }
        
        @Override
        public boolean mouseMoved(MouseEvent e) {
            if(hover) {
                if(e.getX() < width/2 && e.getY() < height)
                    isAdded = false;
                else 
                    isAdded = true;
                state = 0;
                hover = true;
            }
            return true;
        }
    }
