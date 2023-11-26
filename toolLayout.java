package com.example.jPaint;

//Import necessary modules
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import java.io.File;
import java.util.logging.Logger;


//toolLayout class creates a toolBar for the window
public class toolLayout {
    //Create globals for use throughout the program
    private final ToolBar toolBar;
    private static String activeTool;
    //private ArrayList tools;
    public static ToggleButton line, rectB, pen, sqr, circ, eli, poly, colG, erase, rectR, text, mystery;
    public static TextField typing;
    private static Integer myInt; //Sets default line width
    private static ComboBox<Integer> widthBox, polySides, fontSize;
    private static ComboBox<Double> dashes;
    private final HBox dashBox, drawing1, drawing2, shapes1, shapes2, fontBox, textTypes;
    private static VBox drawingBox, shapeBox, allTools, screenText, timeBox, brushStyles;
    private final Separator drawSep, shapeSep, split1, split2, split3, split4;

    private final Label penLabel, eraseLabel, drawLabel, brushLabel, textLabel, shapeLabel, widthLabel, colorLabel, dashLabel;

    private final Tooltip penTip, eraseTip, lineTip, colGTip, dashTip, textTip, fontTip, typingTip, mysteryTip, recTip,
            rectRTip, sqrTip, circTip, eliTip, polyTip, widthTip, polySideTip, colorTip;

    public static Logger logger;

    //Set icons for tools
    private final static File penIcon = new File("Resources/250/pen.png");
    private final static File eraseIcon = new File("Resources/250/eraser.png");
    private final static File squareIcon = new File("Resources/250/square.png");
    private final static File lineIcon = new File("Resources/250/line.png");
    private final static File roundIcon = new File("Resources/250/rounded-rectangle.png");
    private final static File rectIcon = new File("Resources/250/rectangle.png");
    private final static File circIcon = new File("Resources/250/circle.png");
    private final static File ellipseIcon = new File("Resources/250/ellipse.png");
    private final static File polygonIcon = new File("Resources/250/polygon.png");
    private final static File textIcon = new File("Resources/250/text.png");
    private final static File colorGIcon = new File("Resources/250/dropper.png");


    static ColorPicker colorPicker = new ColorPicker(Color.BLACK); //Add ColorPicker

    //toolLayout method which actually makes the toolbar
    public toolLayout() {
        logger = Logger.getLogger(this.getClass().getName());

        //Set buttons for alerts
        ButtonType Yes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType No = new ButtonType("No", ButtonBar.ButtonData.NO);

        colorTip = new Tooltip("Select Pen color");
        colorPicker.setTooltip(colorTip);
        colorLabel = new Label("Pen Color");

        //Set separators for toolbar
        shapeSep = new Separator();
        drawSep = new Separator();

        split1 = new Separator(Orientation.VERTICAL);
        split2 = new Separator(Orientation.VERTICAL);
        split3 = new Separator(Orientation.VERTICAL);
        split4 = new Separator(Orientation.VERTICAL);

        Integer[] values = {2, 3, 5, 7, 8, 10, 12, 14, 16, 20, 24, 28, 32, 40}; //Values for widthBox
        Integer[] sides = {3, 5, 6, 7, 8, 10, 5000}; //Values for polySides

        // Create and set combo boxes
        widthTip = new Tooltip("Set width of the pen");
        polySideTip = new Tooltip("Select #of sides for polygon");
        widthBox = new ComboBox<>(FXCollections.observableArrayList(values));
        polySides = new ComboBox<>(FXCollections.observableArrayList(sides));
        widthBox.setValue(5);
        widthBox.setTooltip(widthTip);
        widthLabel = new Label("Brush Styles");
        polySides.setValue(3);
        polySides.setTooltip(polySideTip);

        //Create ToolBar object
        toolBar = new ToolBar();

        //Create Pen button
        penTip = new Tooltip("Tool that free draws lines");
        penTip.setTextAlignment(TextAlignment.RIGHT);
        penLabel = new Label("         Pen");
        pen = new ToggleButton("Pen");
        pen.setGraphic(openImage(penIcon));
        pen.setTooltip(penTip);

        //Create erase button
        eraseTip = new Tooltip("Tool that erases");
        eraseTip.setTextAlignment(TextAlignment.RIGHT);
        eraseLabel = new Label("         Eraser");
        erase = new ToggleButton("Eraser");
        erase.setGraphic(openImage(eraseIcon));
        erase.setTooltip(eraseTip);

        //Create Line tool button
        lineTip = new Tooltip("Tool that draws straight lines");
        lineTip.setTextAlignment(TextAlignment.RIGHT);
        line = new ToggleButton("Line");
        line.setGraphic(openImage(lineIcon));
        line.setTooltip(lineTip);

        //Create colorgrabber button
        colGTip = new Tooltip("Tool that selects color from canvas");
        Button clear = new Button("Clear");
        colG = new ToggleButton("Color Grab");
        colG.setGraphic(openImage(colorGIcon));
        colG.setTooltip(colGTip);


        drawLabel = new Label("Drawing Tools");
        brushLabel = new Label("Brush Settings");

        //Create ComboBox for dashed line tool use
        dashTip = new Tooltip("Set gap of dashed line");
        dashes = new ComboBox<>();
        dashes.setPrefWidth(90);
        dashes.setEditable(true);
        dashes.getItems().addAll(0d, 1d, 3d, 5d, 10d, 15d, 20d, 25d, 30d, 40d, 50d);
        dashes.setValue(0d);
        dashes.setTooltip(dashTip);
        dashLabel = new Label("Set Line Dashes");
        dashBox = new HBox(dashes);

        drawing1 = new HBox(pen, erase, line, colG, clear); //Add buttons to HBox
        drawing2 = new HBox(colG, clear);
        brushStyles = new VBox(widthLabel, widthBox, colorPicker, dashes);
        drawingBox = new VBox(drawLabel, drawing1, drawing2); //Add HBox and separator to VBox

        //Crate ToggleButton and font ComboBox for Text
        textTip = new Tooltip("Tool to paste text to canvas");
        text = new ToggleButton("Text");
        text.setTooltip(textTip);
        fontTip = new Tooltip("Set the size of text");
        fontSize = new ComboBox<>(FXCollections.observableArrayList(values));
        fontSize.setValue(12);
        fontSize.setTooltip(fontTip);
        fontBox = new HBox(fontSize);

        //Create TextField and ToggleButton buttons
        typingTip = new Tooltip("Space to enter text for use");
        typing = new TextField("Insert text here.");
        typing.setTooltip(typingTip);

        //Create Mystery ToggleButton
        mysteryTip = new Tooltip("Mystery text button");
        mystery = new ToggleButton("???");
        mystery.setTooltip(mysteryTip);
        textLabel = new Label("Text");
        textTypes = new HBox(text, mystery);
        text.setGraphic(openImage(textIcon));

        screenText = new VBox(textLabel, textTypes, fontBox, typing); //Put text buttons in VBox

        //Create rectangle toggle button
        recTip = new Tooltip("Tool to draw rectangle");
        rectB = new ToggleButton("Rectangle");
        rectB.setGraphic(openImage(rectIcon));
        rectB.setTooltip(recTip);

        //Create round rectangle toggle button
        rectRTip = new Tooltip("Tool to draw rounded rectangle");
        rectR = new ToggleButton("Round Rectangle");
        rectR.setGraphic(openImage(roundIcon));
        rectR.setTooltip(rectRTip);

        //Create square toggle button
        sqrTip = new Tooltip("Tool to draw a square");
        sqr = new ToggleButton("Square");
        sqr.setGraphic(openImage(squareIcon));
        sqr.setTooltip(sqrTip);

        //Create circle toggle button
        circTip = new Tooltip("Tool to draw a circle");
        circ = new ToggleButton("Circle");
        circ.setGraphic(openImage(circIcon));
        circ.setTooltip(circTip);

        //Create ellipse toggle button
        eliTip = new Tooltip("Tool to draw an ellipse");
        eli = new ToggleButton("Ellipse");
        eli.setGraphic(openImage(ellipseIcon));
        eli.setTooltip(eliTip);

        //Create polygon toggle button
        polyTip = new Tooltip("Tool to draw a polygon");
        poly = new ToggleButton("Polygon");
        poly.setGraphic(openImage(polygonIcon));
        poly.setTooltip(polyTip);
        shapeLabel = new Label("Shapes");

        //Set shape buttons and separator in H and VBoxes
        shapes1 = new HBox(rectB, rectR, sqr);
        shapes2 = new HBox(circ, eli, poly, polySides);
        shapeBox = new VBox(shapeLabel, shapes1, shapes2, shapeSep);
        timeBox = new VBox();

        //Add items to toolBar
        toolBar.getItems().addAll(drawingBox, split1, brushStyles, split2, screenText, split3, shapeBox, split4, timeBox);
        allTools = new VBox(toolBar);
        toolBar.setBackground(Background.fill(Color.LIGHTSKYBLUE));

        //Activate pen
        pen.setOnAction(e -> {
            toolsOff();
            pen.setSelected(true);
            activeTool = "Pen";
            PaintControl.paintLog();
            menuLayout.getArea().setSelected(false);
        });
        //Control for width control box
        widthBox.setOnAction(e -> {
            myInt = widthBox.getValue();
        });
        //dashed line draw action
        dashes.setOnAction(e -> {
            dashes.getValue();
        });
        //line draw action
        line.setOnAction(e -> {
            toolsOff();
            line.setSelected(true);
            activeTool = "Line";
            PaintControl.paintLog();
        });
        //rectangle draw action
        rectB.setOnAction(e -> {
            toolsOff();
            rectB.setSelected(true);
            activeTool = "Rectangle";
            PaintControl.paintLog();
        });
        //Round rectangle draw action
        rectR.setOnAction(e -> {
            toolsOff();
            rectR.setSelected(true);
            activeTool = "Round Rectangle";
            PaintControl.paintLog();
        });
        //square draw action
        sqr.setOnAction(e -> {
            toolsOff();
            sqr.setSelected(true);
            activeTool = "Square";
            PaintControl.paintLog();
        });
        //Clear canvas action
        clear.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", Yes, No);
            alert.showAndWait();
            if (alert.getResult() == Yes) {
                painTabs.resetCanvas();
            }
            if (alert.getResult() == No) {
                e.consume();
            }
        });
        //circle draw action
        circ.setOnAction(e -> {
            toolsOff();
            circ.setSelected(true);
            activeTool = "Circle";
            PaintControl.paintLog();
        });
        //ellipse draw action
        eli.setOnAction(e -> {
            toolsOff();
            eli.setSelected(true);
            activeTool = "Ellipse";
            PaintControl.paintLog();
        });
        //polygon draw action
        poly.setOnAction(e -> {
            toolsOff();
            poly.setSelected(true);
            activeTool = "Polygon";
            PaintControl.paintLog();
        });
        //Color grabber draw action
        colG.setOnAction(e -> {
            toolsOff();
            colG.setSelected(true);
            activeTool = "Color Grabber";
            PaintControl.paintLog();
        });
        //Text button activation action
        text.setOnAction(e -> {
            toolsOff();
            text.setSelected(true);
            activeTool = "Text";
            PaintControl.paintLog();
        });
        //Mystery button activation action
        mystery.setOnAction(e -> {
            toolsOff();
            mystery.setSelected(true);
            activeTool = "Mystery";
            PaintControl.paintLog();
        });
        //Activate eraser
        erase.setOnAction(e -> {
            toolsOff();
            erase.setSelected(true);
            activeTool = "Eraser";
            PaintControl.paintLog();
        });
    }

    //Method to turn tools off when another is selected
    public static void toolsOff() {
        line.setSelected(false);
        pen.setSelected(false);
        rectB.setSelected(false);
        rectR.setSelected(false);
        sqr.setSelected(false);
        circ.setSelected(false);
        eli.setSelected(false);
        poly.setSelected(false);
        colG.setSelected(false);
        erase.setSelected(false);
        text.setSelected(false);
        mystery.setSelected(false);
        menuLayout.getArea().setSelected(false);
        menuLayout.getPaste().setSelected(false);
    }

    /*This method takes a file to open an image to
     * the size of 25, 25 so it can be used as an icon
     * @param p, file object
     * @return an image view
     */
    private static ImageView openImage(File p){
        Image i = new Image(p.toURI().toString(), 25, 25, false, false);
        ImageView iv = new ImageView(i);
        return iv;
    }
    //Method to get toolBar
    public static VBox getToolBar() {
        return allTools;
    }

    //Method to get line width value
    public static Integer getLineWidth() {
        return widthBox.getValue();
    }

    //Method to get color picker color
    public static ColorPicker getColor() {
        return colorPicker;
    }

    //Method to get number of polygon sides
    public static Integer getPolySides() {
        return polySides.getValue();
    }

    //Method to get dashes setting for lines.
    public static ComboBox<Double> getDashes() {
        return dashes;
    }
    //Method to get fontSize for text
    public static ComboBox<Integer> getFontSize() {
        return fontSize;
    }
    //Method to get a textField
    public static TextField getTyping() {
        return typing;
    }
    //Method to get timer box
    public static VBox getTimeBox(){
        return timeBox;
    }
    //Method to get the currently active tool
    public static String getTool(){return activeTool;}
}