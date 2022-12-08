package com.example.animationbuilder2d;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;

public class IOSaveThread extends Thread {
    //    ArrayList<Keyframe> keyframes = new ArrayList<Keyframe>();
    String saveName;
    Context context;

    IOSaveThread(/*ArrayList<KeyFrame> keyframes,*/ String saveName, Context context) {
//        Collections.copy(this.keyframes,  keyframes);
        this.saveName = saveName;
        this.context = context;
    }

    @Override
    public void run() {

    }
}