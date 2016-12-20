/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;
import java.awt.Image;
/**
 *
 * @author aytajabbaszade
 */
public abstract class CatalogItem {
    //parametres
    int ID;
    String key;
    String name;
    Image icon;
    
    //methods
    public abstract Object create (Object[]args);
    public abstract Interaction interact (Object[]args);
}
