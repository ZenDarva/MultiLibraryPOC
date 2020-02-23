package com.valkryst;

import com.valkryst.component.Component;
import com.valkryst.font.Java2DFont;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public abstract class Screen extends Thread {
    protected static final Logger LOG= LogManager.getLogger(Screen.class);
    protected final Dimension dimensions;

    protected Thread gameThread;

    protected Consumer<Long> mainFunction;

    protected List<Component> componentList;

    protected boolean RUNNING=true;

    protected boolean mainLoop = true;

    protected long lastTime = System.currentTimeMillis();

    public Screen(Consumer<Long> mainFunction){
        this(new Dimension(800,600),mainFunction);
    }

    public Screen(Dimension dimensions, Consumer<Long> mainFunction){
        this.dimensions = dimensions;
        this.mainFunction = mainFunction;
        this.componentList=new ArrayList<>();
    }

    public abstract void draw();

    public void addComponent(Component component){
        componentList.add(component);
    }

    public List<Component> getComponents(){
        return Collections.unmodifiableList(componentList);
    }
    public void removeComponent(Component component){
        componentList.remove(component);
    }

    public void startEngine(){
        if (mainLoop) {
            gameThread = new Thread() {
                @Override
                public void run() {
                    startup();
                    LOG.info("Starting main engine thread.");
                    mainLoop();
                    LOG.info("Stopping main engine thread ");
                    System.exit(0);
                }
            };
            gameThread.start();
        }
        else {
            startup();
        }
    }
    protected abstract void startup();

    private void mainLoop(){

        while(RUNNING){
            mainFunction.accept(System.currentTimeMillis() - lastTime);
            draw();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LOG.info("Main engine thread interrupted. {}", e);
            }
            lastTime=System.currentTimeMillis();
        }
    }

}
