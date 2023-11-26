package com.example.jPaint;

import javafx.application.Platform;
import javafx.scene.control.Label;
import java.io.File;
import java.util.TimerTask;

//Class to automatically save the canvas to a temp directory
public class autosave extends TimerTask {
    private static int time;
    private Label counter, saved;
    private File autoTemp;

    @Override
    public void run() { //Run the autosave
        Platform.runLater(() -> {
            counter = new Label("Autosave in: " + time + " seconds"); //Set autosave Labels
            saved = new Label("File saved!");
            toolLayout.getTimeBox().getChildren().setAll(counter);
            if (menuLayout.getAutosave().isSelected()) { //Select if counter is displayed or not
                counter.setVisible(true);
            } else {
                counter.setVisible(false);
            }
        });
        time--;

        //Handle autosaving
        if (time == 0) {
            saved.setVisible(true);
            Platform.runLater(() -> {
                autoTemp = new File("Resources/Autosaves/Paint Autosave.jpg");
                painTabs.Save(autoTemp);
            });
            Platform.runLater(() -> time = 45);
        }
    }
    //Autosave default constructor
    public autosave() {
        Platform.runLater(() -> time = 45);
    }
}
//Hannah (Gelly Deli girl) is literally so cool
