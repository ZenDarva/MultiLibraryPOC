package com.valkryst.font;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Font {
    @Getter protected int charWidth;
    @Getter protected int charHeight;

    protected Map<Character, Rectangle> fontMap;

    private static final Logger LOG= LogManager.getLogger(Font.class);

    protected void loadFontFile(File file) throws IOException {
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
}
