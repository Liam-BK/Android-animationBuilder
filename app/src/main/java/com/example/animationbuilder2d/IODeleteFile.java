package com.example.animationbuilder2d;

import android.content.Context;

public class IODeleteFile extends Thread{
    String fileName;
    Context context;
    IODeleteFile(String fileName, Context context){
        this.fileName = fileName;
        this.context = context;
    }
    @Override
    public void run() {

    }
}