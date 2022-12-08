package com.example.animationbuilder2d;

import android.content.Context;

public class IOLoadThread extends Thread{
    String fileName;
    Context context;
    //    ArrayList<KeyFrame> keyframes = new ArrayList<>();
    IOLoadThread(String fileName, Context context){
        this.fileName = fileName;
        this.context = context;
    }
    @Override
    public void run() {

    }
//ArrayList<KeyFrame> getKeyFrames(){
//    return keyframes;
//}
}