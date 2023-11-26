package com.example.jPaint;

//Import necessary modules
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.awt.image.BufferedImage;

public class drawing extends Canvas {
    GraphicsContext gc; //Set graphicsContext
    private Image copied;
    private static Image finalImage;
    private BufferedImage myImage, nextImage;

    //drawing default construct setup extends Canvas
    public drawing() {
        super();
        gc = this.getGraphicsContext2D();
    }

    /*Method to draw straight line
     * @param x1, Double
     * @param y1, Double
     * @param x2, Double
     * @param y2, Double */
    public void lineDraw(Double x1, Double y1, Double x2, Double y2) {
        gc.strokeLine(x1, y1, x2, y2); //Draw line along these dimensions
    }

    /*Method to draw rectangle
     * @param x1, Double
     * @param y1, Double
     * @param x2, Double
     * @param y2, Double */
    public void drawRect(Double x1, Double y1, Double x2, Double y2) {
        double x = (x1 < x2 ? x1 : x2); //set x to the smaller of the two values to map to bottom left
        double y = (y1 < y2 ? y1 : y2); //
        double w = Math.abs(x1 - x2);   //abs val of the two x's = length of x
        double h = Math.abs(y1 - y2);
        //gc.fillRect(x, y, w, h);
        gc.strokeRect(x, y, w, h);
    }
    /*Method to draw round rectangle
     * @param x1, Double
     * @param y1, Double
     * @param x2, Double
     * @param y2, Double */
    public void drawRoundRect(Double x1, Double y1, Double x2, Double y2) {
        double x = (x1 < x2 ? x1 : x2); //set x to the smaller of the two values to map to bottom left
        double y = (y1 < y2 ? y1 : y2); //
        double w = Math.abs(x1 - x2);   //abs val of the two x's = length of x
        double h = Math.abs(y1 - y2);
        //gc.fillRect(x, y, w, h);
        gc.strokeRoundRect(x, y, w, h, 25, 25);
    }

    /*Method to draw square
     * @param x1, Double
     * @param y1, Double
     * @param x2, Double
     * @param y2, Double */
    public void drawSquare(Double x1, Double y1, Double x2, Double y2) {
        double x = (x1 < x2 ? x1 : x2); //set x to the smaller of the two values to map to bottom left
        double y = (y1 < y2 ? y1 : y2); //
        double w = Math.abs(x1 - x2);   //abs val of the two x's = length of x
        double h = Math.abs(y1 - y2);
        //gc.fillRect(x, y, w, h);
        gc.strokeRect(x, y, w, h);
    }

    /*Method to draw circle
     * @param x1, Double
     * @param y1, Double
     * @param x2, Double
     * @param y2, Double */
    public void drawCircle(Double x1, Double y1, Double x2, Double y2) {
        double x = (x1 < x2 ? x1 : x2);
        double y = (y1 < y2 ? y1 : y2);
        double w = Math.abs(x1 - x2);
        double h = Math.abs(y1 - y2);
        gc.strokeOval(x, y, w, h);
    }

    /*Method to draw ellipse
     * @param x1, Double
     * @param y1, Double
     * @param x2, Double
     * @param y2, Double */
    public void drawEllipse(Double x1, Double y1, Double x2, Double y2) {
        double x = (x1 < x2 ? x1 : x2);
        double y = (y1 < y2 ? y1 : y2);
        double w = Math.abs(x1 - x2);
        double h = Math.abs(y1 - y2);
        gc.strokeOval(x, y, w, h);
    }

    /*Method to draw polygon
     * @param x1, Double
     * @param y1, Double
     * @param x2, Double
     * @param y2, Double */
    public void drawPolygon(Double x1, Double y1, Double x2, Double y2) {
        double[] xPoints = new double[toolLayout.getPolySides()]; //Get number of sides for x values
        double[] yPoints = new double[toolLayout.getPolySides()]; //Get number of sides for y values
        double r = Math.sqrt((Math.pow((x2 - x1), 2)) + Math.pow((y2 - y1), 2)); //Calculate dimensions of polygon
        double startDirec = Math.atan2(y2 - y1, x2 - x1); //Get mouse direction
        //Loop to draw all sides of polygon
        for (int i = 0; i < toolLayout.getPolySides(); i++) {
            xPoints[i] = x1 + (r * Math.cos((2 * Math.PI * i) / toolLayout.getPolySides()) + startDirec);
            yPoints[i] = y1 + (r * Math.sin((2 * Math.PI * i) / toolLayout.getPolySides()) + startDirec);
        }
        gc.strokePolygon(xPoints, yPoints, toolLayout.getPolySides());
    }

    /*Method to draw copy area
     * @param x1, Double
     * @param y1, Double
     * @param x2, Double
     * @param y2, Double */
    public void drawCopy(double x1, double y1, double x2, double y2){
        PaintControl.getCurrentTab().getCanvas();
        double w = Math.abs(x2 - x1);
        double h = Math.abs(y2 - y1);
        copied = this.snapshot(null, null);
        myImage = SwingFXUtils.fromFXImage(copied, null);
        nextImage = new BufferedImage((int) w, (int) h, BufferedImage.OPAQUE); //Image to be copied
        if (x2 >= x1 && y2 >= y1){//drawing diagonally down right
            copied = this.snapshot(null, null);
            nextImage.createGraphics().drawImage(myImage.getSubimage((int) x1, (int) y1, (int) w, (int) h),0, 0, null);
            finalImage = SwingFXUtils.toFXImage(nextImage, null);
        }else if (x2 >= x1 && y1 >= y2){//drawing diagonally up right
            copied = this.snapshot(null, null);
            nextImage.createGraphics().drawImage(myImage.getSubimage((int) x1, (int) y2, (int) w, (int) h),0, 0, null);
            finalImage = SwingFXUtils.toFXImage(nextImage, null);
        }else if(x1 >= x2 && y2 >= y1){//drawing diagonally up left
            copied = this.snapshot(null, null);
            nextImage.createGraphics().drawImage(myImage.getSubimage((int) x2, (int) y1, (int) w, (int) h),0, 0, null);
            finalImage = SwingFXUtils.toFXImage(nextImage, null);
        }else if(x1 >= x2 && y1 >= y2){//drawing diagonally up left
            nextImage.createGraphics().drawImage(myImage.getSubimage((int) x2, (int) y2, (int) w, (int) h),0, 0, null);
            finalImage = SwingFXUtils.toFXImage(nextImage, null);
        }
    }

    /*Method to draw area for selection
     * @param x1, Double
     * @param y1, Double
     * @param x2, Double
     * @param y2, Double */
    public void drawArea(Double x1, Double y1, Double x2, Double y2) {
        //Set outline to dashed lines for area selection
        gc.setStroke(Color.BLACK);
        gc.setLineDashes(new double[]{25d, 10d, 25d, 10d});
        double x = (x1 < x2 ? x1 : x2); //set x to the smaller of the two values to map to bottom left
        double y = (y1 < y2 ? y1 : y2); //
        double w = Math.abs(x1 - x2);   //abs val of the two x's = length of x
        double h = Math.abs(y1 - y2);
        gc.strokeRect(x, y, w, h);
        gc.setLineDashes(null);
    }

    /*Method to draw region for selection rotating
     * @param x1, Double
     * @param y1, Double
     * @param x2, Double
     * @param y2, Double */
    public Rectangle2D drawRegion(Double x1, Double y1, Double x2, Double y2) {
        //Set outline to dashed lines for area selection
        gc.setStroke(Color.BLACK);
        gc.setLineDashes(new double[]{25d, 10d, 25d, 10d});
        double x = (x1 < x2 ? x1 : x2); //set x to the smaller of the two values to map to bottom left
        double y = (y1 < y2 ? y1 : y2); //
        double w = Math.abs(x1 - x2);   //abs val of the two x's = length of x
        double h = Math.abs(y1 - y2);
        gc.strokeRect(x, y, w, h);
        gc.setLineDashes(null);
        Rectangle2D myRect = new Rectangle2D(x, y, w, h);
        return myRect;
    }

    /*Method to draw area to be cut
     * @param x1, Double
     * @param y1, Double
     * @param x2, Double
     * @param y2, Double */
    public void drawCut(double x1, double y1, double x2, double y2){
        gc.setFill(Color.WHITE); //Fill cut area to be white
        gc.setStroke(Color.WHITE);
        double w = Math.abs(x2 - x1);
        double h = Math.abs(y2 - y1);
        copied = this.snapshot(null, null);
        nextImage = SwingFXUtils.fromFXImage(copied, null);
        myImage = new BufferedImage((int) w, (int) h, BufferedImage.OPAQUE);//Image to be copied
        if (x2 >= x1 && y2 >= y1){//drawing diagonally down right
            copied = this.snapshot(null, null);
            myImage.createGraphics().drawImage(nextImage.getSubimage((int) x1, (int) y1, (int) w, (int) h),0, 0, null);
            finalImage = SwingFXUtils.toFXImage(myImage, null);
            gc.fillRect(x1, y1, w, h);
            gc.strokeRect(x1, y1, w, h);
        }else if (x2 >= x1 && y1 >= y2){//drawing diagonally up right
            copied = this.snapshot(null, null);
            myImage.createGraphics().drawImage(nextImage.getSubimage((int) x1, (int) y2, (int) w, (int) h),0, 0, null);
            finalImage = SwingFXUtils.toFXImage(myImage, null);
            gc.fillRect(x1, y2, w, h);
            gc.strokeRect(x1, y2, w, h);
        }else if(x1 >= x2 && y2 >= y1){//drawing diagonally up left
            copied = this.snapshot(null, null);
            myImage.createGraphics().drawImage(nextImage.getSubimage((int) x2, (int) y1, (int) w, (int) h),0, 0, null);
            finalImage = SwingFXUtils.toFXImage(myImage, null);
            gc.fillRect(x2, y1, w, h);
            gc.strokeRect(x2, y1, w, h);
        }else if(x1 >= x2 && y1 >= y2){//drawing diagonally up left
            myImage.createGraphics().drawImage(nextImage.getSubimage((int) x2, (int) y2, (int) w, (int) h),0, 0, null);
            finalImage = SwingFXUtils.toFXImage(myImage, null);
            gc.fillRect(x2, y2, w, h);
            gc.strokeRect(x2, y2, w, h);
        }
    }
    //Method to get an image
    public static Image getImage(){
        return finalImage;
    }
    /*Method to get the top left corner of a section of canvas
     * @param x1, Double
     * @param y1, Double
     * @param x2, Double
     * @param y2, Double
     * @param move, Boolean */
    public static javafx.geometry.Point2D getTopLeft(double x1, double y1, double x2, double y2) {
        if (x2 >= x1 && y2 >= y1) {                     //draw down & right
            return new javafx.geometry.Point2D(x1, y1);
        } else if (x2 >= x1) {                          //drawing up & right
            return new javafx.geometry.Point2D(x1, y2);
        } else if (y2 >= y1) {                          //draw down & left
            return new javafx.geometry.Point2D(x2, y1);
        } else {                                        //draw up & left
            return new javafx.geometry.Point2D(x2, y2);
        }
    }
    //Method to get GraphicsContext
    public GraphicsContext getGC() {
        return gc;
    }
}