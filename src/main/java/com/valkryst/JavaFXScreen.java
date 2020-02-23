package com.valkryst;

import com.valkryst.component.Component;
import com.valkryst.font.JavaFXFont;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.util.function.Consumer;


public class JavaFXScreen extends Screen {

    JavaFXFont font;

    public JavaFXScreen(Consumer<Long> mainFunction) {
        this(new Dimension(12*80,24*40),mainFunction);
    }

    public JavaFXScreen(Dimension dimensions, Consumer<Long> mainFunction) {
        super(dimensions, mainFunction);
        font = new JavaFXFont(new File("C:\\Users\\James\\Documents\\VTerminal2\\src\\main\\resources\\Fonts\\DejaVu Sans Mono\\20pt\\bitmap.png"));
    }

    private Canvas canvas;
    @Override
    public void draw() {

        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setFill(Color.BLACK);
        g.fillRect(0,0,dimensions.width,dimensions.height);
        componentList.forEach(component ->this.drawComponent(component,g));

    }

    private void drawComponent(Component component, GraphicsContext g) {
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
        FXApplication.go(this);

    }


    protected void acceptAPI(FXApplication app){
        canvas=app.canvas;
    }


}
