package com.valkryst.font;

import com.valkryst.tile.Tile;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Blend;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

public class JavaFXFont extends Font {
    private static final Logger LOG = LogManager.getLogger(JavaFXFont.class);
    private Image image;

    public JavaFXFont(File file) {
        if (!file.exists()) {
            LOG.error("Attempted to load a font that does not exist: {}.", file);
            System.exit(-1);
        }
        try {
            FileInputStream fis = new FileInputStream(file);
            image = new Image(fis);
        } catch (IOException e) {
            LOG.error("Exception when loading Font {} {}", file, e);
        }
        String fileName = file.getName();
        fileName.replaceAll(".*", ".fnt");
        File fontFile = new File(file.getParentFile(), "data.fnt");
        try {
            loadFontFile(fontFile);
        } catch (IOException e) {
            LOG.error("Exception when loading Font description {} {}", fontFile, e);
        }

    }


    public void draw(Tile tile, int x, int y, GraphicsContext g) {
        if (tile.getCharacter() == 0)
            return;
        Rectangle rect = fontMap.get(tile.getCharacter());

        Paint bg = Color.rgb(tile.getBackground().getRed(),tile.getBackground().getGreen(),tile.getBackground().getBlue(),tile.getBackground().getAlpha()/255);
        g.setFill(bg);

        g.fillRect(x,y,rect.width,rect.height);

        Color fg = Color.rgb(tile.getForeground().getRed(),tile.getForeground().getGreen(),tile.getForeground().getBlue(),tile.getForeground().getAlpha()/255);


        ColorAdjust adjust = new ColorAdjust();
        if (tile.getForeground() != java.awt.Color.black)//I hate hacks... but here we go.
        {
            adjust.setSaturation(fg.getSaturation());
            adjust.setHue(fg.getHue());
            adjust.setBrightness(.1);
            adjust.setContrast(fg.getBrightness() + 180);
        }
        else {
            adjust.setBrightness(-1);
        }


        g.setEffect(adjust);
        g.drawImage(image,rect.x,rect.y,rect.width,rect.height,x,y,rect.width,rect.height);
        g.setEffect(null);

    }
}
