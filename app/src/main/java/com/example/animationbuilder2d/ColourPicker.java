package com.example.animationbuilder2d;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ColourPicker {
    Slider red, green, blue;
    float x, y, width, height, left, top, right, bottom;
    int colour, defaultColour;
    boolean active = false;
    Paint paint = new Paint();
    Button set, setAsDefault;
    ColourPicker(float x, float y, float width, float height, int colour){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        float halfWidth = 0.5f * width;
        float halfHeight = 0.5f * height;
        this.left = x - halfWidth;
        this.top = y - halfHeight;
        this.right = x + halfWidth;
        this.bottom = y + halfHeight;
        this.colour = colour;
        red = new Slider(x, top + height * 0.1f, width * 0.7f, 0, 255, 255, Color.RED);
        green = new Slider(x, top + height * 0.3f, width * 0.7f, 0, 255, 0, Color.GREEN);
        blue = new Slider(x, top + height * 0.5f, width * 0.7f, 0, 255, 0, Color.BLUE);
        paint.setStrokeWidth(3);
        set = new Button(left + width * 0.25f, bottom - width * 0.25f, width * 0.4f, width * 0.2f, Color.rgb(200, 200, 200), "set selected");
        set.drawBorder = true;
        setAsDefault = new Button(right - width * 0.25f, bottom - width * 0.25f, width * 0.4f, width * 0.2f, Color.rgb(200, 200, 200), "set default");
        setAsDefault.drawBorder = true;
        defaultColour = getColour();
    }

    int getColour(){
        return Color.rgb(red.getCurrentValue(), green.getCurrentValue(), blue.getCurrentValue());
    }

    void handleTouchDown(float touchX, float touchY){
        red.handleTouchDown(touchX, touchY);
        green.handleTouchDown(touchX, touchY);
        blue.handleTouchDown(touchX, touchY);
        set.handleTouchDown(touchX, touchY);
        setAsDefault.handleTouchDown(touchX, touchY);
        active = touchX > left && touchX < right && touchY > top && touchY < bottom;
    }

    void handleTouchMove(float touchX, float touchY){
        red.handleTouchMove(touchX, touchY);
        green.handleTouchMove(touchX, touchY);
        blue.handleTouchMove(touchX, touchY);
        set.handleTouchMove(touchX, touchY);
        setAsDefault.handleTouchMove(touchX, touchY);
    }

    void handleTouchUp(float touchX, float touchY){
        red.handleTouchUp(touchX, touchY);
        green.handleTouchUp(touchX, touchY);
        blue.handleTouchUp(touchX, touchY);
        set.handleTouchUp();
        if(setAsDefault.active && setAsDefault.checkTouch(touchX, touchY)){
            defaultColour = getColour();
        }
        setAsDefault.handleTouchUp();
        active = false;
    }

    void draw(Canvas canvas){
        paint.setColor(colour);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(left, top, right, bottom, paint);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(left, top, right, bottom, paint);
        red.draw(canvas);
        green.draw(canvas);
        blue.draw(canvas);
        set.draw(canvas);
        setAsDefault.draw(canvas);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getColour());
        canvas.drawRect(set.left, bottom - height * 0.1f, set.right, bottom - height * 0.05f, paint);
        paint.setColor(defaultColour);
        canvas.drawRect(setAsDefault.left, bottom - height * 0.1f, setAsDefault.right, bottom - height * 0.05f, paint);
    }
}