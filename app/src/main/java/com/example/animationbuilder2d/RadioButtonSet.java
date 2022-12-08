package com.example.animationbuilder2d;

import android.graphics.Canvas;

import java.util.ArrayList;

public class RadioButtonSet {
    ArrayList<RadioButton> list = new ArrayList<>();
    int trueIndex = 0;
    void addButton(float x, float y, float size, int colour){
        list.add(new RadioButton(x, y, size, colour));
        if(list.size() == 1){
            list.get(0).isTrue = true;
        }
    }
    void handleTouchDown(float touchX, float touchY){
        for(int i = 0; i < list.size(); i++){
            list.get(i).handleTouchDown(touchX, touchY);
        }
    }
    void handleTouchMove(float touchX, float touchY){
        for(int i = 0; i < list.size(); i++){
            list.get(i).handleTouchMove(touchX, touchY);
        }
    }
    void handleTouchUp(float touchX, float touchY){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).checkTouch(touchX, touchY) && list.get(i).active){
                reset();
                trueIndex = i;
                list.get(i).handleTouchUp(touchX, touchY);
                break;
            }
        }
    }
    int getTrueIndex(){
        return trueIndex;
    }
    void reset(){
        for(int i = 0; i < list.size(); i++){
            list.get(i).reset();
        }
    }
    RadioButton get(int i){
        return list.get(i);
    }
    void draw(Canvas canvas){
        for(int i = 0; i < list.size(); i++){
            list.get(i).draw(canvas);
        }
    }
}