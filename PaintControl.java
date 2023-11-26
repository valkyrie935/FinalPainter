package com.example.jPaint;

//Import necessary modules
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

public class PaintControl extends Application {
    //Create global variables so they can be accessed everywhere.
     mouseDraw can; //Create Canvas
    static Scene scene; //Create Scene
     static Stage myStage;//Create stage
     static TabPane tabPane; //Create pane for tabs
    private BorderPane border; //Create window border

    private menuLayout thisLayout; //make menuLayout object
    private toolLayout tool; //make toolLayout object

     AnchorPane root; //Establishes the AnchorPane that the program is set on

    /* Method to maintain image dimensions when rotated
     * @param value, double
     * @param min, double
     * @param max, double */
    public static double clamp(double value, double min, double max) {
        if (value > max) value = max;
        else if (value < min) value = min;
        return value;
    }

    @Override
    /*Create Stage Method
     * @param stage, Stage */
    public void start(Stage stage) throws IOException {
        //Setup stage, Canvas, and tabs
        myStage = stage;
        can = new mouseDraw();
        tabPane = new TabPane();
        root = new AnchorPane();


        //Create BorderPane
        border = new BorderPane();

        //Create menuLayout by invoking menuLayout method and create image group
        tool = new toolLayout();
        thisLayout = new menuLayout();

        VBox HUD = new VBox(menuLayout.getMenuBar(), toolLayout.getToolBar()); //Add menu and tools

        TimerTask autosave = new autosave(); //Create autosave task
        try{
            Timer t = new Timer();
            t.scheduleAtFixedRate(autosave, 0, 500);
        }catch(Exception e){
            System.out.println("Timer exception handled");
        }

        scene = new Scene(border, 1250, 1750);//Create scene


        //Fill the border with UI and tabs
        border.setTop(HUD);
        border.setCenter(tabPane);
        tabPane.getTabs().add(new painTabs());
        tabPane.getSelectionModel().selectFirst();

        //Create and set scene and stage
        stage.setTitle("Paint");
        stage.setScene(scene);
        stage.show();
        checkSave(stage);
    }
    //Declare main
    public static void main(String[] args) {
        launch();
    }

    //Method to return Scene
    public static Scene getScene(){
        return scene;
    }

    /*Method to prompt the user about saving upon exit
     * @param stage, Stage */
    public void checkSave(Stage stage) {
        ButtonType Yay = new ButtonType("Save", ButtonBar.ButtonData.YES);
        ButtonType Nay = new ButtonType("Don't Save", ButtonBar.ButtonData.NO);
        ButtonType Say = new ButtonType("Cancel", ButtonBar.ButtonData.APPLY);
        if (painTabs.path == null) {
            stage.setOnCloseRequest(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to save first?", Yay, Nay, Say);
                alert.showAndWait();
                if (alert.getResult() == Yay) {
                    painTabs.Save(painTabs.getFilePath());
                }
                if (alert.getResult() == Nay) {
                    stage.close();
                }
                if (alert.getResult() == Say) {
                    event.consume();
                }
            });
        }
    }


    //This method logs when tools are used
    public static void paintLog(){
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        simpleDate.format(date);
        FileHandler fileHandle;
        try {
            // This block configures the logger with handler and formatter
            fileHandle = new FileHandler("Resources/250/logOutput.log");
            toolLayout.logger.addHandler(fileHandle);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandle.setFormatter(formatter);
            toolLayout.logger.info("(" + date + ") - Active Tool: " + toolLayout.getTool());
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Method to get current working tab
    public static painTabs getCurrentTab(){
        return (painTabs)tabPane.getSelectionModel().getSelectedItem();
    }
    //Method to get the TabPane
    public static TabPane getTabPane(){
        return tabPane;
    }
    //Method call to remove current tab
    public static void removeCurrentTab(){tabPane.getTabs().remove(PaintControl.getCurrentTab());}
}