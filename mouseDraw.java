package com.example.jPaint;

//Import necessary modules
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Stack;
import static com.example.jPaint.toolLayout.colorPicker;

public class mouseDraw extends drawing{
    private static double x1, y1, x2, y2; //Set global doubles for mouse inputs
    private final Stack<Image> redo, undo; //Establish stack of the Image for undo and redo functionality

    //Default constructor mouseDraw handles all drawing functions and actions
    public mouseDraw(){
        super();
        redo = new Stack<>();
        undo = new Stack<>();
        this.setWidth(1450);
        this.setHeight(800);

        //Handler for mouse press
        this.setOnMousePressed((MouseEvent e) ->{
            initDraw(toolLayout.getLineWidth(), toolLayout.getColor(), toolLayout.getDashes());//Establish draw settings
            //Get mouse x and y points
            x1 = e.getX();
            y1 = e.getY();
            if (toolLayout.pen.isSelected()){
                PaintControl.getCurrentTab();
                getGC().beginPath();  //Begins line path
                getGC().moveTo(e.getX(), e.getY()); //Gets line movement
                getGC().stroke(); //Adds "BrushStroke" to graphicsContext
                this.addUndo(undo, this);
            }
            else if (toolLayout.erase.isSelected()){ //Eraser tool
                colorPicker.setValue(Color.WHITE);
                getGC().beginPath();  //Begins line path
                getGC().moveTo(e.getX(), e.getY()); //Gets line movement
                getGC().stroke(); //Adds "BrushStroke" to graphicsContext
                this.addUndo(undo, this);
            }
            else if(toolLayout.sqr.isSelected()) {//square tool
                this.addUndo(undo, this);
            }
            else if(toolLayout.line.isSelected()) {//line tool
                this.addUndo(undo, this);
            }
            else if(toolLayout.rectB.isSelected()) {//rectangle tool
                this.addUndo(undo, this);
            }
            else if(toolLayout.rectR.isSelected()) {//round rectangle tool
                this.addUndo(undo, this);
            }
            else if(toolLayout.circ.isSelected()) {//circle tool
                this.addUndo(undo, this);
            }
            else if(toolLayout.eli.isSelected()) {//ellipse tool
                this.addUndo(undo, this);
            }
            else if(toolLayout.poly.isSelected()) {//polygon tool
                this.addUndo(undo, this);
            }
            else if(menuLayout.getArea().isSelected()) {//area select tool
                this.addUndo(undo, this);
            }
        });



        //Handler for mouse dragging
        this.setOnMouseDragged((MouseEvent e) ->{
            initDraw(toolLayout.getLineWidth(), toolLayout.getColor(), toolLayout.getDashes());//Establish draw settings
            //Get mouse x and y points
            x2 = e.getX();
            y2 = e.getY();
            if (toolLayout.pen.isSelected()){
                PaintControl.getCurrentTab();
                getGC().lineTo(e.getX(), e.getY());//Gets line movement
                getGC().stroke(); //Adds "BrushStroke" to graphicsContext
            }
            else if (toolLayout.erase.isSelected()){
                getGC().lineTo(e.getX(), e.getY());//Gets line movement
                getGC().stroke(); //Adds "BrushStroke" to graphicsContext
            }
            else if (toolLayout.rectB.isSelected()) {
                Image live = undo.peek(); //Create image used for live drawing
                this.getGC().drawImage(live, 0, 0); //draw shape live
                drawRect(x1, y1, x2, y2);
            }
            else if (toolLayout.line.isSelected()) {
                Image live = undo.peek(); //Create image used for live drawing
                this.getGC().drawImage(live, 0, 0); //draw shape live
                lineDraw(x1, y1, x2, y2);
            }
            else if (toolLayout.rectR.isSelected()) {
                Image live = undo.peek(); //Create image used for live drawing
                this.getGC().drawImage(live, 0, 0); //draw shape live
                drawRoundRect(x1, y1, x2, y2);
            }
            else if (toolLayout.rectB.isSelected()) {
                Image live = undo.peek(); //Create image used for live drawing
                this.getGC().drawImage(live, 0, 0); //draw shape live
                drawRect(x1, y1, x2, y2);
            }
            else if (toolLayout.sqr.isSelected()) {
                Image live = undo.peek(); //Create image used for live drawing
                this.getGC().drawImage(live, 0, 0); //draw shape live
                drawSquare(x1, y1, x2, y2);
            }
            else if (toolLayout.circ.isSelected()) {
                Image live = undo.peek(); //Create image used for live drawing
                this.getGC().drawImage(live, 0, 0); //draw shape live
                drawCircle(x1, y1, x2, y2);
            }
            else if (toolLayout.eli.isSelected()) {
                Image live = undo.peek(); //Create image used for live drawing
                this.getGC().drawImage(live, 0, 0); //draw shape live
                drawEllipse(x1, y1, x2, y2);
            }
            else if (toolLayout.poly.isSelected()) {
                Image live = undo.peek(); //Create image used for live drawing
                this.getGC().drawImage(live, 0, 0); //draw shape live
                drawPolygon(x1, y1, x2, y2);
            }
            else if (menuLayout.getArea().isSelected()) {
                Image live = undo.peek(); //Create image used for live drawing
                this.getGC().drawImage(live, 0, 0); //draw shape live
                drawArea(x1, y1, x2, y2);
            }
        });

        //Handler for mouse release
        this.setOnMouseReleased((MouseEvent e) ->{
            PaintControl.getCurrentTab();
            initDraw(toolLayout.getLineWidth(), toolLayout.getColor(), toolLayout.getDashes());//Establish draw settings
            //Get mouse x and y points
            x2 = e.getX();
            y2 = e.getY();
            if (toolLayout.pen.isSelected()){ //Draw with pen if pen is selected
                PaintControl.getCurrentTab().setChanges(true); //Update the tab
            }
            else if (toolLayout.line.isSelected()){
                lineDraw(x1, y1, x2, y2);//Draw line if line is selected
                redo.clear();
                PaintControl.getCurrentTab().setChanges(true); //Update the tab
            }
            else if (toolLayout.rectB.isSelected()){
                drawRect(x1, y1, x2, y2);//Draw rectangle if rectB is selected
                redo.clear();
                PaintControl.getCurrentTab().setChanges(true); //Update the tab
            }
            else if (toolLayout.rectR.isSelected()){
                drawRoundRect(x1, y1, x2, y2);//Draw round rectangle if rectR is selected
                redo.clear();
                PaintControl.getCurrentTab().setChanges(true); //Update the tab
            }
            else if (toolLayout.sqr.isSelected()){
                drawSquare(x1, y1, x2, y2);//Draw square if sqr is selected
                redo.clear();
                PaintControl.getCurrentTab().setChanges(true); //Update the tab
            }
            else if (toolLayout.circ.isSelected()){
                drawCircle(x1, y1, x2, y2);//Draw circle if circ is selected
                redo.clear();
                PaintControl.getCurrentTab().setChanges(true); //Update the tab
            }
            else if (toolLayout.eli.isSelected()){
                drawEllipse(x1, y1, x2, y2);//Draw ellipse if eli is selected
                redo.clear();
                PaintControl.getCurrentTab().setChanges(true); //Update the tab
            }
            else if (toolLayout.poly.isSelected()){
                drawPolygon(x1, y1, x2, y2);//Draw polygon if poly is selected
                redo.clear();
                PaintControl.getCurrentTab().setChanges(true); //Update the tab
            }
            else if (toolLayout.erase.isSelected()){ //Erase if erase is selected
                redo.clear();
                PaintControl.getCurrentTab().setChanges(true); //Update the tab
            }
            else if(menuLayout.getArea().isSelected()){ //Draw area for selection if get.Area().isSelected()
                Image live = undo.peek(); //Create image used for live drawing
                this.getGC().drawImage(live, 0, 0); //Draw shape live
                PaintControl.getCurrentTab().setChanges(true);
                redo.clear();
                menuLayout.getCut().setDisable(false);
                menuLayout.getCopy().setDisable(false);
            }
        });

        //Handle action on mouse click
        this.setOnMouseClicked((MouseEvent e) -> { //Event to use colorGrabber if colG is selected
            PaintControl.getCurrentTab();
            if (toolLayout.colG.isSelected()) {
                PaintControl.getCurrentTab();
                colorPicker.setValue(menuLayout.getColorAtPixel(x1, y1));//Change colorPicker value
                PaintControl.getCurrentTab().setChanges(true); //Update changes to tab
            }
            else if(toolLayout.text.isSelected()) { //Event to paste text if text is selected
                initDraw(toolLayout.getFontSize());
                String input = toolLayout.getTyping().getText();
                getGC().fillText(input, x1, y1);
                redo.clear();
                PaintControl.getCurrentTab().setChanges(true); //Update changes to tab
            }
            else if(toolLayout.mystery.isSelected()) { //Event to handle mystery button on click
                getGC().fillText("Mason is the Greatest!", x1, y1); //Set text of mystery
                redo.clear();
                PaintControl.getCurrentTab().setChanges(true); //Update changes to tab
            }
            else if(menuLayout.getPaste().isSelected()){ //Paste selection upon click
                this.getGC().drawImage(drawing.getImage(), x1, y1);
                PaintControl.getCurrentTab().setChanges(true); //Update changes to tab
                redo.clear();
            }
        });
    }
    /*Method to apply drawing settings to canvas
     * @param width, Integer
     * @param colorPicker, ColorPicker
     * @param dash, ComboBox<Double> */
    public void initDraw(Integer width, ColorPicker colorPicker, ComboBox<Double> dash) {
        colorPicker.setValue(colorPicker.getValue()); //Get drawing color
        getGC().setStroke(colorPicker.getValue());  //Set drawing color
        getGC().setLineWidth(width); //Set drawing width
        double ld = Double.parseDouble(dash.getEditor().getText()); //Check and loop to see if dashed line is active
        if (ld < 0d){
            dash.setValue(0d);
            getGC().setLineDashes(0d);
        } else{
            getGC().setLineDashes(new double[] {ld, (ld*1.3), ld, (ld*1.3)});
        }
    }

    /*Method to apply text settings to canvas WIP
     * @param colorPicker, ColorPicker
     * @param font, ComboBox<Integer> */
    public void initDraw(ComboBox<Integer> font) {
        getGC().setFont(Font.font(font.getValue()));
    }

    //Method to redo an undone action
    public void redo() {
        if (!redo.empty()) {
            WritableImage hold = new WritableImage((int) this.getWidth(), (int) this.getHeight()); //Add image to clipboard
            this.snapshot(null, hold);
            Image storedImage = redo.pop(); //Store previous image
            this.getGC().drawImage(storedImage, 0, 0); //draw previous image
            undo.push(hold);
        }
    }
    //Method to undo an action
    public void undo(){
        if(!undo.empty()){
            WritableImage hold = new WritableImage((int) this.getWidth(),(int) this.getHeight()); //Add image to clipboard
            this.snapshot(null, hold);
            Image storedImage = undo.pop(); //Store previous image
            this.getGC().drawImage(storedImage, 0, 0); //draw previous image
            redo.push(hold);
        }
    }
    /* Method to add the undo/redo functionality to a tool
     * @param myStack, Stack<Image>
     * @param can, mouseDraw*/
    public void addUndo(Stack<Image> myStack, mouseDraw can){
        Image prev = new WritableImage((int) can.getWidth(),(int) can.getHeight()); //Hold previous canvas "image"
        this.snapshot(null, (WritableImage) prev); //Create snapshot of previous canvas
        myStack.push(prev);
        redo.clear();
    }

    //Method to rotate an area to the right
    public void selectionRotate() {
        if (menuLayout.getArea().isSelected()) {
            SnapshotParameters snap = new SnapshotParameters();
            snap.setFill(Color.TRANSPARENT);
            snap.setViewport(drawRegion(x1, y1, x2, y2));
                Image selection = snapshot(snap, null);
                BufferedImage image = SwingFXUtils.fromFXImage(selection, null);
                AffineTransformOp op;
                op = new AffineTransformOp(rotateClockwise90(image), AffineTransformOp.TYPE_BILINEAR);

                image = op.filter(image, null);

                double w = Math.abs(x2 - x1);
                double h = Math.abs(y2 - y1);
                {
                    if (x2 >= x1 && y2 >= y1) {         //draw down & right
                        getGC().clearRect(x1, y1, w, h);
                    } else //draw down & left
                        //draw up & left
                        if (x2 >= x1) {  //drawing up & right
                            gc.clearRect(x1, y2, w, h);
                        } else gc.clearRect(x2, Math.min(y2, y1), w, h);
                }

                Point2D point = drawing.getTopLeft(x1, y1, x2, y2);
                PasteRotate(this, SwingFXUtils.toFXImage(image, null), new Point2D(point.getX() + w / 2, point.getY() + h / 2));
            }
        }

    //Method to rotate an area to the left
    public void selectionRotateL() {
        if (menuLayout.getArea().isSelected()) {
            SnapshotParameters snap = new SnapshotParameters();
            snap.setFill(Color.TRANSPARENT);
            snap.setViewport(drawRegion(x1, y1, x2, y2));
            Image selection = snapshot(snap, null);
            BufferedImage image = SwingFXUtils.fromFXImage(selection, null);
            AffineTransformOp op;
            op = new AffineTransformOp(rotateCounter90(image), AffineTransformOp.TYPE_BILINEAR);

            image = op.filter(image, null);

            double w = Math.abs(x2 - x1);
            double h = Math.abs(y2 - y1);
            {
                if (x2 >= x1 && y2 >= y1) {         //draw down & right
                    getGC().clearRect(x1, y1, w, h);
                } else //draw down & left
                    //draw up & left
                    if (x2 >= x1) {  //drawing up & right
                        gc.clearRect(x1, y2, w, h);
                    } else gc.clearRect(x2, Math.min(y2, y1), w, h);
            }

            Point2D point = drawing.getTopLeft(x1, y1, x2, y2);
            PasteRotate(this, SwingFXUtils.toFXImage(image, null), new Point2D(point.getX() + w / 2, point.getY() + h / 2));
        }
    }
    //Method to mirror an area horizontally
    public void horizMirror() {
        if (menuLayout.getArea().isSelected()) {
            SnapshotParameters snap = new SnapshotParameters();
            snap.setFill(Color.TRANSPARENT);
            snap.setViewport(drawRegion(x1, y1, x2, y2));
            Image selection = snapshot(snap, null);
            BufferedImage image = SwingFXUtils.fromFXImage(selection, null);
            AffineTransform MR = new AffineTransform();
            MR = AffineTransform.getScaleInstance(-1, 1);

            MR.translate(-image.getWidth(null), 0);

            AffineTransformOp op = new AffineTransformOp(MR, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

            image = op.filter(image, null);

            double w = Math.abs(x2 - x1);
            double h = Math.abs(y2 - y1);
            {
                if (x2 >= x1 && y2 >= y1) {         //draw down & right
                    gc.clearRect(x1, y1, w, h);
                } else //draw down & left
                    //draw up & left
                    if (x2 >= x1) {  //drawing up & right
                        gc.clearRect(x1, y2, w, h);
                    } else gc.clearRect(x2, Math.min(y2, y1), w, h);
            }

            Point2D point = drawing.getTopLeft(x1, y1, w, h);
            myPaste(this, SwingFXUtils.toFXImage(image, null), point);
        }
    }
    //Method to mirror an area vertically
    public void verticMirror() {
        if (menuLayout.getArea().isSelected()) {
            SnapshotParameters snap = new SnapshotParameters();
            snap.setFill(Color.TRANSPARENT);
            snap.setViewport(drawRegion(x1, y1, x2, y2));
            Image selection = snapshot(snap, null);
            BufferedImage image = SwingFXUtils.fromFXImage(selection, null);
            AffineTransform MR = new AffineTransform();

            MR = AffineTransform.getScaleInstance(1, -1);

            MR.translate(0, -image.getHeight(null));
            AffineTransformOp op = new AffineTransformOp(MR, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

            image = op.filter(image, null);

            double w = Math.abs(x2 - x1);
            double h = Math.abs(y2 - y1);
            {
                if (x2 >= x1 && y2 >= y1) {         //draw down & right
                    gc.clearRect(x1, y1, w, h);
                } else //draw down & left
                    //draw up & left
                    if (x2 >= x1) {  //drawing up & right
                        gc.clearRect(x1, y2, w, h);
                    } else gc.clearRect(x2, Math.min(y2, y1), w, h);
            }

            Point2D point = drawing.getTopLeft(x1, y1, w, h);
            myPaste(this, SwingFXUtils.toFXImage(image, null), point);
        }
    }

    //Method that does the actual rotating to the right
    public void rotate() {
        SnapshotParameters snap = new SnapshotParameters();
        snap.setFill(Color.TRANSPARENT);
            Image screenshot = this.snapshot(snap, null);
            BufferedImage image = SwingFXUtils.fromFXImage(screenshot, null);
            AffineTransformOp op = new AffineTransformOp(rotateClockwise90(image), AffineTransformOp.TYPE_BILINEAR);
            image = op.filter(image, null);

            gc.clearRect(0, 0, this.getWidth(), this.getHeight());
            PasteRotate(this, SwingFXUtils.toFXImage(image, null), new Point2D(this.getWidth() / 2, this.getHeight() / 2));
        }
    //Method that does the actual rotating to the left
    public void rotateL() {
        SnapshotParameters snap = new SnapshotParameters();
        snap.setFill(Color.TRANSPARENT);
        Image screenshot = this.snapshot(snap, null);
        BufferedImage image = SwingFXUtils.fromFXImage(screenshot, null);
        AffineTransformOp op = new AffineTransformOp(rotateCounter90(image), AffineTransformOp.TYPE_BILINEAR);
        image = op.filter(image, null);

        gc.clearRect(0, 0, this.getWidth(), this.getHeight());
        PasteRotate(this, SwingFXUtils.toFXImage(image, null), new Point2D(this.getWidth() / 2, this.getHeight() / 2));
    }

    /*Method that rotates a selected area to the right
     * @param source, BufferedImage */
    private static AffineTransform rotateClockwise90(BufferedImage source) {
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.PI / 2, source.getWidth() / 2d, source.getHeight() / 2d);
        double offset = (source.getWidth() - source.getHeight()) / 2d;
        transform.translate(offset, offset);
        return transform;
    }
    /*Method that rotates a selected area to the left
     * @param source, BufferedImage */
    private static AffineTransform rotateCounter90(BufferedImage source) {
        AffineTransform transform = new AffineTransform();
        transform.rotate(-Math.PI / 2, source.getWidth() / 2d, source.getHeight() / 2d);
        double offset = (source.getWidth() - source.getHeight()) / 2d;
        transform.translate(-offset, -offset);
        return transform;
    }

    /*Unique paste method for pasting mirrored images
     * @param can, Canvas
     * @param image, Image
     * @param point, Point2D*/
    public static void myPaste(Canvas can, Image image, Point2D point) {
        can.getGraphicsContext2D().drawImage(image, point.getX(), point.getY());
    }
    /*Unique paste method for pasting rotated images
     * @param can, Canvas
     * @param image, Image
     * @param point, Point2D*/
    public static void PasteRotate(Canvas can, Image image, Point2D point) {
        double x = image.getWidth();
        double y = image.getHeight();
        if (x > can.getWidth()) can.setWidth(x);
        if (y > can.getHeight()) can.setHeight(y);
        can.getGraphicsContext2D().drawImage(image,
                PaintControl.clamp(point.getX() - x / 2, 0, can.getWidth()),
                PaintControl.clamp(point.getY() - y / 2, 0, can.getHeight()));
    }

    //Method to get first mouse input x value
    public static double getX1(){return x1;}
    //Method to get first mouse input y value
    public static double getY1(){return y1;}
    //Method to get second mouse input x value
    public static double getX2(){return x2;}
    //Method to get second mouse input y value
    public static double getY2(){return y2;}
}