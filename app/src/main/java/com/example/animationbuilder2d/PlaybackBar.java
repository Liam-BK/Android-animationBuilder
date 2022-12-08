package com.example.animationbuilder2d;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class PlaybackBar {
    RadioButtonSet set = new RadioButtonSet();
    PlaybackProgressbar progressBar;
    CheckBox checkBox;
    SymbolButton prev_Keyframe, playButton, nextKeyframe;
    NumberEditor editor;
    float x, y, width, height, left, top, right, bottom;
    Symbol_Pause pause = new Symbol_Pause(Color.rgb(80, 80, 80));
    Symbol_Play play = new Symbol_Play(Color.rgb(80, 80, 80));
    int colour;
    Paint paint = new Paint();
    float buttonSize = 70f;
    boolean changeDetected = false;

    PlaybackBar(float x, float y, float width, float height, int colour) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.left = x - (width * 0.5f);
        this.top = y - (height * 0.5f);
        this.right = x + (width * 0.5f);
        this.bottom = y + (height * 0.5f);
        this.colour = colour;
        prev_Keyframe = new SymbolButton(left + Util.screenX(50), y, Util.screenX(buttonSize), Util.screenX(buttonSize), colour, new Symbol_Prev(Color.rgb(80, 80, 80)));
        playButton = new SymbolButton(prev_Keyframe.x + Util.screenX(buttonSize), y, Util.screenX(buttonSize), Util.screenX(buttonSize), colour, play);
        nextKeyframe = new SymbolButton(playButton.x + Util.screenX(buttonSize), y, Util.screenX(buttonSize), Util.screenX(buttonSize), colour, new Symbol_Next(Color.rgb(80, 80, 80)));
        progressBar = new PlaybackProgressbar(x, y, Util.screenX(800), Util.screenX(80), Color.rgb(255, 255, 255), true);
        progressBar.showProgressionVals = true;
        checkBox = new CheckBox(progressBar.right + Util.screenX(30), y + Util.screenX(22.5f), Util.screenX(35), Color.rgb(255, 255, 255));
        checkBox.setText("Repeat");
        editor = new NumberEditor(progressBar.right + Util.screenX(60), y - Util.screenX(22.5f), Util.screenX(100), Util.screenX(35), 0, 0, 1);
        set.addButton(editor.right + Util.screenX(25), editor.y, editor.height, colour);
        set.get(0).setText("Seconds");
        set.addButton(editor.right + Util.screenX(25), checkBox.y, editor.height, colour);
        set.get(1).setText("Frames");
    }

    boolean checkTouch(float touchX, float touchY){
        return touchX >= left && touchX <= right && touchY >= top && touchY <= bottom;
    }

    void handleTouchDown(float touchX, float touchY) {
        prev_Keyframe.handleTouchDown(touchX, touchY);
        playButton.handleTouchDown(touchX, touchY);
        nextKeyframe.handleTouchDown(touchX, touchY);
        progressBar.handleTouchDown(touchX, touchY);
        checkBox.handleTouchDown(touchX, touchY);
        editor.handleTouchDown(touchX, touchY);
        set.handleTouchDown(touchX, touchY);
    }

    void handleTouchMove(float touchX, float touchY) {
        prev_Keyframe.handleTouchMove(touchX, touchY);
        playButton.handleTouchMove(touchX, touchY);
        nextKeyframe.handleTouchMove(touchX, touchY);
        progressBar.handleTouchMove(touchX, touchY);
        checkBox.handleTouchMove(touchX, touchY);
        editor.handleTouchMove(touchX, touchY);
        set.handleTouchMove(touchX, touchY);
    }

    boolean getSetBool() {
        return set.getTrueIndex() == 1;
    }

    void handleTouchUp(float touchX, float touchY) {
        prev_Keyframe.handleTouchUp();
        if (playButton.active && playButton.checkTouch(touchX, touchY)) {
            if (playButton.symbol instanceof Symbol_Play) {
                playButton.setSymbol(pause);
                progressBar.startRunning();
            } else {
                playButton.setSymbol(play);
                progressBar.stopRunning();
            }
        }
        playButton.handleTouchUp();
        nextKeyframe.handleTouchUp();
        checkBox.handleTouchUp(touchX, touchY);
        editor.handleTouchUp();
        if(editor.pad.checkTouch(touchX, touchY)){
            if(getSetBool()){
                progressBar.timingBased = false;
                progressBar.setDuration(editor.getValue());

            }
            else{
                progressBar.timingBased = true;
                progressBar.setDuration(editor.getValue());

            }
        }
        set.handleTouchUp(touchX, touchY);
        if(getSetBool() != changeDetected){
            if(getSetBool()){
                progressBar.timingBased = false;
                progressBar.setDuration(editor.getValue());

            }
            else{
                progressBar.timingBased = true;
                progressBar.setDuration(editor.getValue());

            }
            changeDetected = getSetBool();
        }
        progressBar.handleTouchUp(touchX);
    }

    void draw(Canvas canvas) {
        progressBar.play(checkBox.getIsTrue());
        if (progressBar.durationCompleted == progressBar.duration && !checkBox.getIsTrue() && playButton.symbol instanceof Symbol_Pause) {
            playButton.setSymbol(new Symbol_Play(playButton.symbol.colour));
        }
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(colour);
        canvas.drawRect(left, top, right, bottom, paint);
        prev_Keyframe.draw(canvas);
        playButton.draw(canvas);
        nextKeyframe.draw(canvas);
        progressBar.draw(canvas);
        checkBox.draw(canvas);
        editor.draw(canvas);
        set.draw(canvas);
    }
}