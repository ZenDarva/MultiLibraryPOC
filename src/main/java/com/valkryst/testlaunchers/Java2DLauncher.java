package com.valkryst.testlaunchers;

import com.valkryst.Java2DScreen;
import com.valkryst.component.Layer;

import java.awt.*;
import java.util.Random;

public class Java2DLauncher {
    public static void main(String[] args) throws InterruptedException {
        Java2DLauncher test = new Java2DLauncher();
        Java2DScreen screen = new Java2DScreen(test::processPhysics);
        Layer layer = new Layer(new Dimension(80,40), new Point(5,5));
        Random rnd = new Random();
        for (int x = 0; x < 80; x++) {
            for (int y = 0; y < 40; y++) {
                layer.setTileCharacter(x,y, (char) (rnd.nextInt(26)+97));
                layer.setTileForeground(x,y,Color.BLACK);
                layer.setTileBackground(x,y,getRandomColor());
            }
        }
        screen.addComponent(layer);

        Layer layer2 = new Layer(new Dimension(20,20));
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                layer2.setTileCharacter(x,y, '@');
                layer2.setTileForeground(x,y,Color.RED);
                layer2.setTileBackground(x,y,new Color(0,0,0,0));
            }
        }
        screen.addComponent(layer2);
        screen.startEngine();


    }

    public void processPhysics(Long time){

    }

    public static Color getRandomColor(){
        Random rnd = new Random();
        return new Color(rnd.nextInt(255),rnd.nextInt(255),rnd.nextInt(255));
    }


}
