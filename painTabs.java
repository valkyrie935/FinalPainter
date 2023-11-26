package com.example.jPaint;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class painTabs extends Tab {
    static mouseDraw can; //Create Canvas
     static Pane pain; //Create Canvas
     ScrollPane scroller; //Declare ScrollPane
     Boolean setChanges; //Boolean used to set changes to a tab
     private String title; //Tab title
    static File path = null; //File path used when opening an image
    AnchorPane root; //Establishes the AnchorPane that the program is set on

    mouseDraw canNew; //Create canvas from mouseDraw class


    //Default tab constructor
    public painTabs(){
        super();
        setTitle("Brick"); //Set tab name
        this.setChanges = true; //Set settings
        can = new mouseDraw();
        tabActivate();
    }
    //Tab constructor for opening an image
    public painTabs(File file){
        super();
        this.setChanges = false;
        path = file;
        this.setTitle(path.getName()); //Set tab name to file name
        //canNew = new mouseDraw();
        tabActivate();
    }
    //Method to set attributes and properties of the tab
    public void tabActivate(){
        //Establish Pane, ScrollPane, and Canvas
        pain = new Pane(can);
        canNew = new mouseDraw();
        this.root = new AnchorPane();
        this.root.getChildren().addAll(canNew);
        this.scroller = new ScrollPane(this.root);
        this.setContent(scroller);


        //this.setText((setChanges ? "*": "") + this.title);
        this.setText(this.title);
        this.setOnCloseRequest((Event e) -> {
            e.consume();            //consumes the normal event call
            PaintControl.removeCurrentTab(); //Removes tab
        });
        //Set ScrollPane dimensions
        this.scroller.setPrefViewportWidth(can.getWidth()/2);
        this.scroller.setPrefViewportHeight(can.getHeight()/2);
    }

    //Method to set changes on a tab
    public void setChanges(boolean setChanges){this.setChanges = setChanges;}
    //method to get the active canvas
    public mouseDraw getCanvas(){return canNew;}

    //Method to open an image
    public void openImage() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.jpeg",
                "*.png", "*.bmp", "*.jpg"));
        path = fc.showOpenDialog(PaintControl.myStage);
        drawImage(path);    //draws image from same path
    }

    /*SaveAs method to save images
     * @param fc, FileChooser
     * @param stage, Stage */
    public static void saveAs() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.jpeg",
                "*.png", "*.bmp", "*.jpg"));
        can = PaintControl.getCurrentTab().getCanvas();
        //Try and catch block to handle writing the file
        try {
            //Setup for image adjustment
            path = fc.showSaveDialog(PaintControl.myStage); //Save Dialog Box
            String type = path.toString().substring(path.toString().lastIndexOf(".") + 1);
            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setFill(Color.TRANSPARENT);
            WritableImage imgSave = can.snapshot(parameters, null);
            BufferedImage bImage = SwingFXUtils.fromFXImage(imgSave, null);
            if(type.equals("jpeg")) { //Check if image is of type jpeg and makes sure the user wants to convert it.
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION); //Setup alerts.
                alert.getButtonTypes().remove(ButtonType.OK);
                alert.getButtonTypes().remove(ButtonType.CANCEL);
                ButtonType Continue = new ButtonType("Continue");
                ButtonType Exit = new ButtonType("Exit");
                ButtonType Save = new ButtonType("Save as Png");
                alert.getButtonTypes().addAll(Save, Exit, Continue);
                alert.setHeaderText("Saving Your Drawing as a JPEG will remove its transparency and reduce color accuracy," +
                        " but will reduce its file size.\n Would you still like to convert?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent()) {
                    if (result.get().equals(Continue)) {
                        bImage = pngTojpg(bImage);
                    }
                    if (result.get().equals(Save)) {
                        path = changeExtension(path, "png");
                        type = "png";
                    }
            }
        }
            else if(type.equals("bmp")){ //Check if image is of type bmp and makes sure the user wants to convert it.
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION); //Setup Alert
                alert.getButtonTypes().remove(ButtonType.OK);
                alert.getButtonTypes().remove(ButtonType.CANCEL);
                ButtonType Continue = new ButtonType("Continue");
                ButtonType Exit = new ButtonType("Exit");
                ButtonType Save = new ButtonType("Save as Png");
                alert.getButtonTypes().add(Save);
                alert.getButtonTypes().add(Exit);
                alert.getButtonTypes().add(Continue);
                alert.setHeaderText("Saving Your Drawing as a BMP will remove its transparency and reduce color accuracy.\n\n" +
                        "Do you wish to continue?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent()) {
                    if (result.get().equals(Continue)) {
                        bImage = pngTojpg(bImage);
                    }
                    if (result.get().equals(Save)) {
                        path = changeExtension(path, "png");
                        type = "png";
                    }
                }
            }
            if(bImage != null){
                ImageIO.write(bImage, type, path); //Image writer
                Notifications.create()
                        .title("Saved Image")
                        .text(path.getName() + " successfully saved!")
                        .darkStyle()
                        .hideAfter(new Duration(4000))
                        .owner(PaintControl.getScene().getWindow())
                        .threshold(3, Notifications.create()
                                .title("Saved Image")
                                .text("successfully saved all open tabs!")
                                .darkStyle()
                                .hideAfter(new Duration(4000))
                                .owner(PaintControl.getScene().getWindow()))
                        .show();
            }

    } catch (Exception e) {
            Logger.getLogger(PaintControl.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
    }
    /* Method to change the file extension type
     * @param f, File
     * @param newExtension, String */
    public static File changeExtension(File f, String newExtension) {
        int i = f.getName().lastIndexOf('.');
        String name = f.getName().substring(0, i + 1);
        return new File(f.getParent(), name + newExtension);
    }
    //Method to get pane object
    public static Pane getPane(){
        return pain;
    }

    /* Method to convert pngs to jpgs
     * @param image, BufferedImage */
    private static BufferedImage pngTojpg(BufferedImage image) {
        if (image.getType() == 3 || image.getType() == 2) {
            BufferedImage newBufferedImage = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_INT_RGB);

            // draw a new background and puts the original image on it.
            newBufferedImage.createGraphics()
                    .drawImage(image,
                            0,
                            0,
                            java.awt.Color.WHITE,
                            null);
            return newBufferedImage;
        } else return null;
    }

    /* Method to convert pngs to bmps
     * @param image, BufferedImage */
    private static BufferedImage pngTobmp(BufferedImage image) {
        if (image.getType() == 3 || image.getType() == 2) {
            BufferedImage newBufferedImage = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_3BYTE_BGR);

            // draw a new background and puts the original image on it.
            newBufferedImage.createGraphics()
                    .drawImage(image,
                            0,
                            0,
                            java.awt.Color.WHITE,
                            null);
            return newBufferedImage;
        } else return null;
    }
    /*Method to save an image
     * @param fc, FileChooser
     * @param stage, Stage
     * @param path, File */
    public static void Save(File path) {
        //Try and catch block to handle writing the file
        if(path == null){
            saveAs(); //Call saveAs if file hasn't been saved before
        }
        else {
            can = PaintControl.getCurrentTab().getCanvas();
            String type = path.toString().substring(path.toString().lastIndexOf(".") + 1);
            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setFill(Color.TRANSPARENT);
            try {
               //Image img = getRegion(can.getWidth(), can.getHeight());
                WritableImage imageToSave = can.snapshot(parameters, null);
                BufferedImage bImage = SwingFXUtils.fromFXImage(imageToSave, null);
                //Check filetype image saving
                if (type.equals("jpg") || type.equals("jpeg")) {
                    bImage = pngTojpg(bImage);
                } else if (type.equals("bmp")) {
                    bImage = pngTobmp(bImage);
                }
                ImageIO.write(bImage,type, path); //Image writer
                Notifications.create()
                        .title("Saved Image")
                        .text(path.getName() + " successfully saved!")
                        .darkStyle()
                        .hideAfter(new Duration(4000))
                        .owner(PaintControl.getScene().getWindow())
                        .threshold(3, Notifications.create()
                                .title("Saved Image")
                                .text("successfully saved all open tabs!")
                                .darkStyle()
                                .hideAfter(new Duration(4000))
                                .owner(PaintControl.getScene().getWindow()))
                        .show();
            } catch (Exception x) {
                Logger.getLogger(PaintControl.class.getName()).log(Level.SEVERE, null, x);
                x.printStackTrace();
            }
        }
    }
    /* Method to draw an image to the canvas
     * @param file, File
     */
    public void drawImage(File file) {
        can = PaintControl.getCurrentTab().getCanvas();
        //Confirm file is not null
        if (file == null) {
            return;
        }
        //Create image object and clear the canvas of the old image
        Image im = new Image(file.toURI().toString());
        //Against my better judgement Koeppen was here
        if (im.getWidth() > can.getWidth() || im.getHeight() > can.getHeight()) {
            //Alert popup to warn user about resized image.
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to have the image fit the screen?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                fitToScreen(can, im);
            }
        } else {
            //Clear the rectangle and set the image
            can.getGraphicsContext2D().clearRect(
                    0,
                    0,
                    im.getWidth(),
                    im.getHeight()
            );
            //Set canvas size to image size and draw the image`
            can.setWidth(im.getWidth());
            can.setHeight(im.getHeight());
            can.isResizable();
            can.getGraphicsContext2D().drawImage(im, 0, 50);
        }
    }
    /*Method to fit image to stage screen
     * @param can, Canvas
     * @param im, Image */
    public static void fitToScreen(Canvas can, Image im){
        GraphicsContext gc = can.getGraphicsContext2D(); //Get GraphicsContext
        resetCanvas(); //Call to reset Canvas so it can be adjusted
        //Calculate new image dimensions
        double widthRatio = can.getWidth() / im.getWidth();
        double heightRatio = can.getHeight() / im.getHeight();
        double minRatio = Math.min(widthRatio, heightRatio);
        //Set new image dimensions
        can.setWidth(im.getWidth() * minRatio);
        can.setHeight(im.getHeight() * minRatio);

        gc.drawImage(im, 0, 0, can.getWidth(), can.getHeight()); //Draw new image
    }

    //Method to reset a canvas
    public static void resetCanvas(){
        can = PaintControl.getCurrentTab().getCanvas();
        GraphicsContext gc = can.getGraphicsContext2D(); //Get GraphicsContext
        Rectangle2D rectangle = Screen.getPrimary().getVisualBounds(); //Get screen bounds
        //Collect canvas dimensions, then clear the graphics rectangle
        can.setWidth(rectangle.getWidth());
        can.setHeight(rectangle.getHeight());
        gc.clearRect(0,0, can.getWidth(), can.getHeight());
    }
    //Method to open a blank tab
    public static void openBlankImage(){
        painTabs blank = new painTabs(); //will open blank image by default
        PaintControl.getTabPane().getTabs().add(blank);  //adds it to the tabpane
    }
    //Method to get File object
    public static  File getFilePath(){
        return path;
    }

    /*Method to set title of a tab
     * @param title, String */
    public void setTitle(String title) {
        this.title = title;
    }
}