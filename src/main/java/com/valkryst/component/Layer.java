package com.valkryst.component;

import com.valkryst.font.Font;
import com.valkryst.tile.Tile;
import com.valkryst.tile.TileGrid;

import java.awt.*;

public class Layer extends Component {
    private final Dimension dimension;


    public Layer(Dimension dimension) {
        this(dimension,new Point(0,0));
    }

    public Layer(Dimension dimension, Point location){
        super(dimension);
        this.dimension = dimension;
        this.location = location;
        this.tileGrid=new TileGrid(dimension);
    }


}
