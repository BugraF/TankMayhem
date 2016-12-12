package game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        // TODO Load
        return assets;
    }
    
    public String[] getFiles(String path) {
        String[] paths = null;
        try {
            paths = Files
                .list(Paths.get(System.getProperty("user.dir"), "data", path))
                .map((p) -> {
                    int count = p.getNameCount();
                    return p.subpath(count - 2, count).toString();
                }).toArray((count) -> new String[count]);
        } catch (IOException ex) {
            Logger.getLogger(AssetManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return paths;
    }
    
    public PImage loadAsset(String name) {
        int dot = name.lastIndexOf('.');
        String imageName = name.substring(0, dot).replace('.', '/');
        String extension = name.substring(dot + 1);
        return context.loadImage(imageName, extension);
    }
    
    public static PImage mask(PImage orig, PImage mask) {
        orig.mask(mask); // mask need to be gray-scale
        return orig;
    }
    
    public static PImage fill(PImage src, int srcColor, int destColor) {
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
