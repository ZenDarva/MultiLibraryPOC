package com.valkryst;

import com.valkryst.component.Component;
import com.valkryst.font.Java2DFont;
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.util.function.Consumer;

public class Java2DScreen extends Screen {
    private JFrame frame;
    private Java2DFont font;
    private Canvas canvas;
    public Java2DScreen(Consumer<Long> mainFunction) {
        this(new Dimension(12*80,24*40),mainFunction);
        font = new Java2DFont(new File("C:\\Users\\James\\Documents\\VTerminal2\\src\\main\\resources\\Fonts\\DejaVu Sans Mono\\20pt\\bitmap.png"));
    }

    public Java2DScreen(Dimension dimensions, Consumer<Long> mainFunction) {
        super(dimensions, mainFunction);
        font = new Java2DFont(new File("C:\\Users\\James\\Documents\\VTerminal2\\src\\main\\resources\\Fonts\\DejaVu Sans Mono\\20pt\\bitmap.png"));
    }

    @Override
    public void draw() {
        BufferStrategy strat = canvas.getBufferStrategy();
        do {
            Graphics2D g = (Graphics2D) strat.getDrawGraphics();

            g.setColor(Color.BLACK);
            g.fillRect(0,0,dimensions.width,dimensions.height);

            componentList.forEach(component ->this.drawComponent(component,g));


            g.dispose();
        } while (strat.contentsRestored());
        strat.show();

    }
    private void drawComponent(Component component, Graphics2D g){
        int offsetX=component.getLocation().x * font.getCharWidth();
        int offsetY=component.getLocation().y * font.getCharHeight();

        for (int x = 0; x < component.getWidth(); x++) {
            for (int y = 0; y < component.getHeight(); y++) {
                int lX = x*font.getCharWidth()+offsetX;
                int lY = y*font.getCharHeight()+offsetY;
                //font.draw(component.getTileCharacter(x,y),lX,lY + offsetY,Color.red,Color.GREEN,g);
                font.draw(component.getTiles()[x][y],lX,lY,g);
            }
        }
        component.getComponents().forEach(child -> drawComponent(child,g));
    }

    @Override
    protected void startup() {
        frame = new JFrame("");
        frame.setSize(dimensions);
        frame.setIgnoreRepaint(true);
        canvas = new Canvas();
        canvas.setIgnoreRepaint(true);
        canvas.setSize(dimensions);
        frame.add(canvas);
        frame.setVisible(true);
        canvas.createBufferStrategy(2);
        frame.addKeyListener(new KeyHandler());
        canvas.getBufferStrategy().getDrawGraphics().dispose();//??
    }

    private class KeyHandler implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
                RUNNING=false;
            }
        }
    }
}
