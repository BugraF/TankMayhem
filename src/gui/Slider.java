/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.core.ActionListener;
import gui.core.Component;
import gui.core.InteractiveComponent;
import gui.core.MouseEvent;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Slider consist of a button and a basic slider.
 * Aim of the button is mute the sound or music.
 * 
 * @author Bugra Felekoglu
 */
public class Slider extends InteractiveComponent {
    
    /**
     * Icon is a clickable image which makes volume 0 or previous value.
     * Slide is a draggable image that adjusts the volume.
     * Line is clickable image which moves the slide.
     */
    private PImage icon;
    
    private float value;

    /**
     * 
     */
    public void init(PApplet context){
        
    }
    
    public void setIcon(PImage icon){
        this.icon = icon;
    }
    
    public void setValue(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }
    
    @Override
    public void draw(PGraphics g) {
        // Orange rectangle
        g.noStroke();
        g.fill(236, 204, 129);
        g.rect(80,20,210,40);

        // Volume line
        g.stroke(0);
        g.strokeWeight((float) 2.4);
        g.line(90, 40, 275, 40);

        // Limit
        g.strokeWeight(1);
        g.line(105, 37, 105, 43);

        // Slider 
        g.noStroke();
        g.fill(0);
        g.rect(105, 25, 10, 30);

        // Box
        g.fill(0);
        g.quad(0,0,100,0,80,80,0,80);
    }

    @Override
    public boolean mousePressed(MouseEvent e) {
        return true;
    }    
    
    
}
