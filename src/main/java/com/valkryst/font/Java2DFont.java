package com.valkryst.font;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.valkryst.tile.Tile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Java2DFont extends Font {
    private static final Logger LOG=LogManager.getLogger(Java2DFont.class);

    private Image fontTexture;



    Cache<Integer, Image> tintCache;

    public Java2DFont(File file){
        if (!file.exists()){
            LOG.error("Attempted to load a font that does not exist: {}.",file);
            System.exit(-1);
        }
        try {
            fontTexture = ImageIO.read(file);
        } catch (IOException e) {
            LOG.error("Exception when loading Font {} {}",file,e);
        }
        String fileName = file.getName();
        fileName.replaceAll(".*",".fnt");
        File fontFile = new File(file.getParentFile(),"data.fnt");
        try {
            loadFontFile(fontFile);
        } catch (IOException e) {
            LOG.error("Exception when loading Font description {} {}",fontFile,e);
        }

        tintCache = Caffeine.newBuilder().maximumSize(500).expireAfterAccess(1, TimeUnit.MINUTES).build();
    }



    public void draw(Tile tile, int x, int y, Graphics g){
        if (tile.getCharacter()== 0)
                return;
        Image glyph = getGlyph(tile);
        Rectangle rect = fontMap.get(tile.getCharacter());

        g.setColor(tile.getBackground());
        g.drawImage(glyph,x,y,null);
    }

    private Image getGlyph(Tile tile){
        Image glyph = tintCache.getIfPresent(tile.hashCode());
        if (glyph!=null){
            return glyph;
        }
        glyph= new BufferedImage(charWidth,charHeight,BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = (Graphics2D) glyph.getGraphics();
        Rectangle rect = fontMap.get(tile.getCharacter());
        g.setColor(tile.getBackground());

        g.fillRect(0,0,rect.width,rect.height);

        g.drawImage(fontTexture,0,0,rect.width,rect.height, rect.x, rect.y,rect.x+rect.width,rect.y+rect.height,null);
        Color foreColor = new Color(tile.getForeground().getRed(),tile.getForeground().getGreen(),tile.getForeground().getBlue(),tile.getBackground().getAlpha());
        g.setXORMode(foreColor);
        g.drawImage(fontTexture,0,0,rect.width,rect.height, rect.x, rect.y,rect.x+rect.width,rect.y+rect.height,null);
        g.dispose();
        tintCache.put(tile.hashCode(),glyph);
        return glyph;
    }

}
