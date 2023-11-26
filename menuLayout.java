package com.example.jPaint;

//Import necessary modules
        import javafx.event.ActionEvent;
        import javafx.scene.Scene;
        import javafx.scene.SnapshotParameters;
        import javafx.scene.canvas.Canvas;
        import javafx.scene.control.*;
        import javafx.scene.image.PixelReader;
        import javafx.scene.image.WritableImage;
        import javafx.scene.input.KeyCode;
        import javafx.scene.input.KeyCodeCombination;
        import javafx.scene.input.KeyCombination;
        import javafx.scene.layout.BorderPane;
        import javafx.scene.layout.VBox;
        import javafx.scene.paint.Color;
        import javafx.scene.text.Text;
        import javafx.scene.text.TextFlow;
        import javafx.stage.Stage;
        import javafx.scene.media.MediaPlayer;
        import javafx.scene.media.Media;
        import java.io.*;
        import java.util.Scanner;
        import static com.example.jPaint.painTabs.can;
        import static com.example.jPaint.painTabs.pain;

//Declare class menuLayout to create menu objects
public class menuLayout {
    //Set global variables used in various methods
    final private MenuItem Save, SaveAs, Open, helper, about, Misc, undo, redo, newt, rotateR, flipH, flipV, rotateL;
    private static CheckMenuItem cut;
    private static CheckMenuItem paste, area, copy, autosave;
    public File notes = new File("src/main/resources/com/example/jPaint/Documents/Paint_Version_History.txt");
    final private MenuBar menuBar;
    static VBox myMenu;
    painTabs tabber;

    //menuLayout creates a menu and many of its tools
    public menuLayout() {
        //Create menuBar object
        menuBar = new MenuBar();
        tabber = new painTabs();

        //Create File Menu items
        Menu file = new Menu("File");
        Save = new MenuItem("Save");
        SaveAs = new MenuItem("Save As");
        Open = new MenuItem("Open");
        newt = new MenuItem("New Tab");


        //Create Clipping Menu items
        Menu CCP = new Menu("Edit");
        copy = new CheckMenuItem("Copy");
        cut = new CheckMenuItem("Cut");
        paste = new CheckMenuItem("Paste");
        area = new CheckMenuItem("Selector");
        undo = new MenuItem("Undo");
        redo = new MenuItem("Redo");

        //Create View menu
        Menu view = new Menu("View");
        autosave = new CheckMenuItem("Autosave");
        autosave.setSelected(true);
        rotateR = new MenuItem("RotateR");
        flipH = new MenuItem("Flip H");
        flipV = new MenuItem("Flip V");
        rotateL = new MenuItem("RotateL");

        //Add items to view menu
        view.getItems().add(autosave);
        view.getItems().add(rotateR);
        view.getItems().add(rotateL);
        view.getItems().add(flipH);
        view.getItems().add(flipV);

        //Add items to the file menu bar
        menuBar.getMenus().add(file);
        menuBar.getMenus().add(CCP);
        menuBar.getMenus().add(view);
        file.getItems().add(SaveAs);
        file.getItems().add(Save);
        file.getItems().add(Open);
        file.getItems().add(newt);
        CCP.getItems().add(copy);
        CCP.getItems().add(cut);
        CCP.getItems().add(paste);
        CCP.getItems().add(area);
        CCP.getItems().add(undo);
        CCP.getItems().add(redo);

        myMenu = new VBox(menuBar); //Create menu VBox

        //Action to SaveAs
        SaveAs.setOnAction(e -> painTabs.saveAs());
        SaveAs.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));

        //Action to Save
        Save.setOnAction(e -> painTabs.Save(painTabs.getFilePath()));
        Save.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));

        //Action to Open a file
        Open.setOnAction(e -> tabber.openImage());
        Open.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));

        //Create Help menu and items
        Menu help = new Menu("Help");
        helper = new MenuItem("Help");
        about = new MenuItem("About");
        Misc = new MenuItem("Misc");

        //Add items to Help Menu
        menuBar.getMenus().add(help);
        help.getItems().add(helper);
        help.getItems().add(about);
        help.getItems().add(Misc);

        //Action to display About
        about.setOnAction(e -> versionDisplay(notes));
        about.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN));

        //Action to display Help
        helper.setOnAction(e -> helpDisplay());
        helper.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN));

        //Action to copy a section of the canvas
        copy.setOnAction(e -> {
            toolLayout.toolsOff();
            can.drawCopy(mouseDraw.getX1(), mouseDraw.getY1(), mouseDraw.getX2(), mouseDraw.getY2());
            area.setSelected(false);
        });
        copy.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));

        //Action to cut a section of the canvas
        cut.setOnAction(e -> {
            toolLayout.toolsOff();
            can.drawCut(mouseDraw.getX1(), mouseDraw.getY1(), mouseDraw.getX2(), mouseDraw.getY2());
            paste.setDisable(false);
        });
        cut.setAccelerator(new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN));

        //Action to paste a section of the canvas
        paste.setOnAction(e -> {
        });
        paste.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN));

        //Action to activate the area select tool
        area.setOnAction(e -> {
            toolLayout.toolsOff();
            area.setSelected(true);
        });
        area.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));

        //Action to undo previous action
        undo.setOnAction(e -> {
            PaintControl.getCurrentTab().getCanvas().undo();
            PaintControl.getCurrentTab().setChanges(true);
        });
        undo.setAccelerator(new KeyCodeCombination(KeyCode.U, KeyCombination.CONTROL_DOWN));

        //Action to redo previous undone action
        redo.setOnAction(e -> {
            PaintControl.getCurrentTab().getCanvas().redo();
            PaintControl.getCurrentTab().setChanges(true);
        });
        redo.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN));

        //Action to make a new blank tab
        newt.setOnAction(e -> painTabs.openBlankImage());
        newt.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN));

        //Thanks to Matt Dembny
        Misc.setOnAction(e -> {
            String music = "Resources/250/RickRoll.MP3";
            Media sound = new Media(new File(music).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        });
        Misc.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN));

        //Action to rotate an image right
        rotateR.setOnAction(this::handleRotateR);
        rotateR.setAccelerator(new KeyCodeCombination(KeyCode.M, KeyCombination.CONTROL_DOWN));

        //Action to rotate an image left
        rotateL.setOnAction(this::handleRotateL);
        rotateL.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN));

        //Action to flip an image horizontally
        flipH.setOnAction(this::handleflipH);
        flipH.setAccelerator(new KeyCodeCombination(KeyCode.G, KeyCombination.CONTROL_DOWN));

        //Action to flip an image vertically
        flipV.setOnAction(this::handleflipV);
        flipV.setAccelerator(new KeyCodeCombination(KeyCode.K, KeyCombination.CONTROL_DOWN));

    }

    //getMenuBar method gets the menuBar
    public static VBox getMenuBar() {
        return myMenu;
    }

    /*Method to display the release notes for paint
     * @param f, File */
    private void versionDisplay(File f) {
        Scanner console = null; //Create null scanner object to fix error
        //Create new Stage and canvas for the display
        Stage popper = new Stage();
        Canvas canH = new Canvas();
        VBox boxH = new VBox();
        BorderPane borderH = new BorderPane();
        try {
            //sets scanner to scan file contents
            console = new Scanner(f);
            //Handle the exception
        } catch (FileNotFoundException ex) {
            System.err.println("Failed to display");
            ex.printStackTrace();
        }
        //text to display on screen
        Text text = new Text();
        //loop to convert string to text
        while (console.hasNextLine()) {
            text.setText(text.getText() + console.nextLine() + "\n");
        }
        //Create and populate scene with file information
        boxH.getChildren().add(text);
        borderH.getChildren().add(canH);
        borderH.setTop(boxH);
        Scene sceneH = new Scene(borderH, 900, 900);
        popper.setTitle("Version History");
        popper.setScene(sceneH);
        popper.show();
    }

    //Create Help window
    public void helpDisplay() {
        //Establish stage, canvas, VBox, and BorderPane
        Stage popper = new Stage();
        Canvas canH = new Canvas();
        VBox boxH = new VBox();
        BorderPane borderH = new BorderPane();

        TextFlow t = new TextFlow(new Text("For help, see His Excellency Mason Tulacz.\nAlternatively, see outer-object codemaster Java The Hutt")); //Create Text
        //Add to borderH and set and display stage
        borderH.getChildren().add(canH);
        borderH.setTop(boxH);
        Scene sceneH = new Scene(t, 400, 400);
        popper.setTitle("Help");
        popper.setScene(sceneH);
        popper.show();
    }

    /*Method to get color at selected pixel
     * @param x, double
     * @param y, double*/
   public static Color getColorAtPixel(double x, double y){
       PaintControl.getCurrentTab();
       SnapshotParameters snap = new SnapshotParameters(); //Create Snapshot object
       WritableImage writer = new WritableImage((int) can.getWidth(), (int) can.getHeight()); //Create writable image
       can.snapshot(snap, writer); //Take snapshot of canvas
       PixelReader pixel = writer.getPixelReader();
       return pixel.getColor((int) x, (int) y);
    }

    /* Method to handle rotating an image to the right
     * @param event, ActionEvent */
    public void handleRotateR(ActionEvent event) {
       if (getArea().isSelected()) {
            can.selectionRotate();
        }else {
            pain.setRotate(pain.getRotate() + 90);
            can.rotate();
        }
    }
    /* Method to handle rotating an image to the left
     * @param event, ActionEvent */
    public void handleRotateL(ActionEvent event) {
        if (getArea().isSelected()) {
            can.selectionRotateL();
        }else {
            can.rotateL();
        }
    }
    /* Method to handle mirroring an image horizontally
     * @param event, ActionEvent */
    public void handleflipH(ActionEvent event) {
        if (getArea().isSelected()) {
            can.horizMirror();
        }else {
            PaintControl.getCurrentTab().getCanvas().setScaleX(painTabs.getPane().getScaleX() * -1);
        }
    }
    /* Method to handle mirroring an image vertically
     * @param event, ActionEvent */
   public void handleflipV(ActionEvent event) {
        if (getArea().isSelected()) {
            can.verticMirror();
        }else {
            PaintControl.getCurrentTab().getCanvas().setScaleY(painTabs.getPane().getScaleY() * -1);
        }
    }
   //Method to get paste menuItem
    public static CheckMenuItem getPaste(){
       return paste;
    }
    //Method to get area menuItem
    public static CheckMenuItem getArea(){
        return area;
    }
    //Method to get copy menuItem
    public static CheckMenuItem getCopy(){
        return copy;
    }
    //Method to get cut menuItem
    public static CheckMenuItem getCut(){
        return cut;
    }
    //Method to get autosave
    public static CheckMenuItem getAutosave(){
       return autosave;
    }
}