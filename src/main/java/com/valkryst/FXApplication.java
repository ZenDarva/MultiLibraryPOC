package com.valkryst;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class FXApplication extends Application {
    protected Canvas canvas;
    private static JavaFXScreen screen;

    @Override
    public void start(Stage primaryStage) throws Exception {
        canvas = new Canvas(screen.dimensions.width, screen.dimensions.height);
        canvas.setWidth(screen.dimensions.width);
        canvas.setHeight(screen.dimensions.height);

        Pane root = new Pane();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Vterminal");
        primaryStage.show();

        screen.acceptAPI(this);

        new AnimationTimer(){

            @Override
            public void handle(long now) {
                screen.mainFunction.accept(now);
                screen.draw();
            }
        }.start();

    }
    public static void go(JavaFXScreen hostScreen){
        screen=hostScreen;
        FXApplication.launch(null);
    }
}
