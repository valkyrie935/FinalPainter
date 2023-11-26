package com.example.jPaint;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class mouseDrawTest {
    @Test
    public void canvasHeightTest(){
        mouseDraw draw = new mouseDraw();
        double height = 800; //Height when scene is created
        assertEquals(height, draw.getHeight());
    }

    @Test
    public void canvasWidthTest(){
        mouseDraw draw = new mouseDraw();
        double width = 1200; //Height when scene is created
        assertEquals(width, draw.getWidth());
    }

    @Test
    public void x2Test(){
        mouseDraw draw = new mouseDraw();
        double x2 = 0; //It should have no value as it hasn't been used yet.
        assertEquals(x2, draw.getX2());
    }
}