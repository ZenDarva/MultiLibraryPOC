package com.valkryst.font;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.sun.javafx.iio.ImageStorage;
import com.valkryst.tile.Tile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Java2DFont extends Font {
    private static final Logger LOG=LogManager.getLogger(Java2DFont.class);

    private Image fontTexture;

    private Map<Character, Rectangle> fontMap;

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

    private void loadFontFile(File file) throws IOException {
        if (!file.exists()){
            LOG.error("Attempted to load a font that does not exist: {}.",file);
            System.exit(-1);
        }

        List<String> lines = Files.lines(file.toPath()).filter(line->line.startsWith("char ")).collect(Collectors.toList());
        fontMap = new HashMap<>();
        for (String line : lines) {
            String token[] = line.split("\\s+");
            int id = Integer.parseInt(token[1].replaceAll(".*=",""));
            if (id > 100000){
                continue;
            }
            Character ch = (char)id;
            int x =Integer.parseInt(token[2].replaceAll(".*=",""));
            int y =Integer.parseInt(token[3].replaceAll(".*=",""));
            int width = Integer.parseInt(token[4].replaceAll(".*=",""));
            int height = Integer.parseInt(token[5].replaceAll(".*=",""));

            //Yeah yeah, i'm gonna do this a bunch of unnecessary times.  I don't care.
            charWidth=width;
            charHeight=height;

            if (fontMap.containsKey(ch))
                LOG.info("Duplicate character detected in font {}, character is {}", file,ch);
            fontMap.put(ch,new Rectangle(x,y,width,height));
        }
    }

    public void draw(char ch, int x, int y, Color foreground, Color background, Graphics2D g){
        Rectangle rect = fontMap.get(ch);


        g.setColor(Color.white);
        g.drawImage(fontTexture,x,y,rect.width+x,rect.height+y,rect.x,rect.y,rect.x+rect.width,rect.y+rect.height,null);

        g.setColor(foreground);

        //g.setXORMode(new Color(foreground.getRed(),foreground.getGreen(),foreground.getBlue(),0));
        g.setComposite(AlphaComposite.DstOver);
        g.drawImage(fontTexture,x,y,rect.width+x,rect.height+y, rect.x, rect.y,rect.x+rect.width,rect.y+rect.height,null);


        g.setColor(background);
        g.fillRect(x,y,rect.width,rect.height);
    }
    public void draw(Tile tile, int x, int y, Graphics g){
        if (tile.getCharacter()== 0)
                return;
        Image glyph = getGlyph(tile);
        Rectangle rect = fontMap.get(tile.getCharacter());

        g.setColor(tile.getBackground());
        //g.fillRect(x,y,rect.width,rect.height);
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
