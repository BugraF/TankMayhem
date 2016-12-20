/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;
import java.util.*;


/**
 *
 * @author aytajabbaszade
 */
public class Catalog {
    
    private Map<String, Integer> idMap;
    private CatalogItem[] items;
    
    public Interaction interact (int itemID, Object[]args){
        Interaction intr=null;
        intr.game.addEntity(args);
        items[itemID] = (CatalogItem)(intr.game.selectionChanged(itemID));
        return intr;   
    }
    public Object create(int itemID, Object[] args){
        Object obj = null;
        
    }
         
    
    public boolean isBomb(int itemID){
        return (items[itemID].key.equals("Bomb"));
             
    }
    public boolean isPowerUp(int itemID){
        return (items[itemID].key.equals("PowerUp"));
    }
}
