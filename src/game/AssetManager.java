package game;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;

/**
 *
 * @author Burak Gök
 */
public class AssetManager {
    
    private final PApplet context;
    
    public AssetManager(PApplet context) {
        this.context = context;
    }
    
    public Map<String, Object> readConfigurationFile(String fileName) {
        JSONObject json = context.loadJSONObject("configuration/" + fileName);
        Map<String, Object> assets = new HashMap<>();
        // load
        return assets;
    }
    
    public PImage loadAsset(String name) {
        return context.loadImage(name.replace('.', '/'), "png");
    }
    
    public PImage mask(PImage orig, PImage mask) {
        orig.mask(mask); // mask need to be gray-scale
        return orig;
    }
    
    public PImage fill(PImage src, int srcColor, int destColor) {
        srcColor &= 0xFFFFFF;
        destColor &= 0xFFFFFF;
        src.loadPixels();
        for (int p = 0; p < src.width * src.height; p++)
            if ((src.pixels[p] & 0xFFFFFF) == srcColor)
                src.pixels[p] = (src.pixels[p] & PImage.ALPHA_MASK) | destColor;
        src.updatePixels();
        return src;
    }
}
