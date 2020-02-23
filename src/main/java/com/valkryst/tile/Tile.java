package com.valkryst.tile;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@EqualsAndHashCode
public class Tile {

    @Getter @Setter private char character;
    @Getter @Setter private Color foreground = Color.red;
    @Getter @Setter private Color background = Color.BLUE;

}
