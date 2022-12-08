package com.example.animationbuilder2d;

import android.graphics.Canvas;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class Model {
    ArrayList<ModelBone> list = new ArrayList<>();
    void addBone(ModelBone bone){
        list.add(bone);
        if(list.size() > 1){
            for(int i = 0; i < list.size() - 1; i++){
                for(int j = 1; j < list.size(); j++){
                    if(Util.withinRange(list.get(i).point1.x, list.get(i).point1.y, list.get(j).point1.x, list.get(j).point1.y, ModelBone.touchRadius * 0.3333f)){

                    }
                    else if(Util.withinRange(list.get(i).point2.x, list.get(i).point2.y, list.get(j).point1.x, list.get(j).point1.y, ModelBone.touchRadius * 0.3333f)){

                    }
                    else if(Util.withinRange(list.get(i).point2.x, list.get(i).point2.y, list.get(j).point2.x, list.get(j).point2.y, ModelBone.touchRadius * 0.3333f)){

                    }
                    else if(Util.withinRange(list.get(i).point1.x, list.get(i).point1.y, list.get(j).point2.x, list.get(j).point2.y, ModelBone.touchRadius * 0.3333f)){

                    }
                }
            }
        }

    }
    boolean checkTouch(float touchX, float touchY){
        boolean val = false;
        for(int i = 0; i < list.size(); i++){
            val = val || list.get(i).checkTouch(touchX, touchY) > 0;
            if(val){
                break;
            }
        }
        return val;
    }

    void draw(Canvas canvas){
        for(int i = 0; i < list.size(); i++){
            list.get(i).draw(canvas);
        }
    }
}