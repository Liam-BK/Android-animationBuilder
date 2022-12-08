package com.example.animationbuilder2d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import java.util.ArrayList;

public class ModelWindow {
    boolean showGrid = false, addingBone = false, settingColour = false, selecting = false, dragSelect = false, connectMode = false, deleting = false;
    float left, top, right, bottom;
    Button addBone;
    SymbolButton save, clear, setColour, connectBones, select, copy, delete, addKeyframe, selectAll, resetOrigin;
    Paint paint = new Paint();
    ArrayList<ModelBone> bones = new ArrayList<>();
//    ArrayList<Integer> connections = new ArrayList<>();

    OriginPoint origin;
    PointF point1 = new PointF(-1, -1), point2 = new PointF(-1, -1);
    ColourPicker picker;
    float connectPercent = 0f, percentSize = 0.25f, percentSpeed = 0.04f;
    String check = "";
    int connectIndex = -1, connectToIndex = -1;

    ModelWindow(float left, float top, float right, float bottom, Context context) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        float buttonHeight = ((bottom - top) + 1) / 8;
        addBone = new Button(right + Util.screenX(40), top + buttonHeight * 0.5f, Util.screenX(80), buttonHeight, Color.rgb(200, 200, 200), "+");
        setColour = new SymbolButton(right + Util.screenX(40), addBone.bottom + buttonHeight * 0.5f, Util.screenX(80), buttonHeight, Color.rgb(200, 200, 200), new Symbol_Colour(context));
        save = new SymbolButton(right + Util.screenX(40), setColour.bottom + buttonHeight * 0.5f, Util.screenX(80), buttonHeight, Color.rgb(200, 200, 200), new Symbol_Save(context));
        clear = new SymbolButton(right + Util.screenX(40), save.bottom + buttonHeight * 0.5f, Util.screenX(80), buttonHeight, Color.rgb(200, 200, 200), new Symbol_Clear(context));
        connectBones = new SymbolButton(right + Util.screenX(40), clear.bottom + buttonHeight * 0.5f, Util.screenX(80), buttonHeight, Color.rgb(200, 200, 200), new Symbol_Connect(context));
        select = new SymbolButton(right + Util.screenX(40), connectBones.bottom + buttonHeight * 0.5f, Util.screenX(80), buttonHeight, Color.rgb(200, 200, 200), new Symbol_Select(context));
        copy = new SymbolButton(right + Util.screenX(40), select.bottom + buttonHeight * 0.5f, Util.screenX(80), buttonHeight, Color.rgb(200, 200, 200), new Symbol_Copy(context));
        delete = new SymbolButton(right + Util.screenX(40), copy.bottom + buttonHeight * 0.5f, Util.screenX(80), buttonHeight, Color.rgb(200, 200, 200), new Symbol_Delete(context));
        paint.setStrokeWidth(Util.screenX(3));
        origin = new OriginPoint((left + right) * 0.5f, (top + bottom) * 0.5f, Util.screenX(25), Color.rgb(220, 150, 0));
        addKeyframe = new SymbolButton(Util.screenX(40), bottom - Util.screenX(40), Util.screenX(80), Util.screenX(80), Color.rgb(200, 200, 200), new Symbol_Keyframe(context));
        selectAll = new SymbolButton(Util.screenX(40), bottom - Util.screenX(120), Util.screenX(80), Util.screenX(80), Color.rgb(200, 200, 200), new Symbol_SelectAll(context));
        resetOrigin = new SymbolButton(Util.screenX(40), bottom - Util.screenX(200), Util.screenX(80), Util.screenX(80), Color.rgb(200, 200, 200), new Symbol_Reset(context));
        picker = new ColourPicker(right - Util.screenX(100), setColour.y, Util.screenX(200), Util.screenX(200), Color.rgb(200, 200, 200));
    }

    boolean checkTouch(float touchX, float touchY) {
        return touchX >= left && touchX <= right && touchY >= top && touchY <= bottom;
    }

    void handleTouchDown(float touchX, float touchY){
        if(settingColour){
            picker.handleTouchDown(touchX, touchY);
        }
        if(checkTouch(touchX, touchY) && !picker.active && !addingBone && !selecting && !connectMode){
            origin.handleTouchDown(touchX, touchY);
        }
        if((addingBone || selecting) && checkTouch(touchX, touchY)){
            point1.set(touchX, touchY);
            point2.set(touchX, touchY);
        }

        if(connectMode){
            float closestDist = Float.MAX_VALUE;
            boolean earlyExit = false;
            for(int i = bones.size() - 1; i >= 0; i--){
                if(bones.get(i).touchWithinBounds(touchX, touchY)){
                    if(bones.get(i).checkTouch(touchX, touchY) == 1){
                        connectIndex = i * 2 + 1;
                        earlyExit = true;
                        break;
                    }
                    else if(bones.get(i).checkTouch(touchX, touchY) == 2){
                        connectIndex = i * 2;
                        earlyExit = true;
                        break;
                    }
                }
                else{
                    if (bones.get(i).checkTouch(touchX, touchY) == 1){
                        if(closestDist > Util.getDistSquared(bones.get(i).point1.x, bones.get(i).point1.y, touchX, touchY)){
                            closestDist = Util.getDistSquared(bones.get(i).point1.x, bones.get(i).point1.y, touchX, touchY);
                            connectIndex = i * 2 + 1;
                        }
                    }
                    else if(bones.get(i).checkTouch(touchX, touchY) == 2){
                        if(closestDist > Util.getDistSquared(bones.get(i).point1.x, bones.get(i).point1.y, touchX, touchY)){
                            closestDist = Util.getDistSquared(bones.get(i).point2.x, bones.get(i).point2.y, touchX, touchY);
                            connectIndex = i * 2;
                        }
                    }
                }
            }
            if(connectIndex >= 0){
                if(connectIndex % 2 == 0){
                    point1.set(bones.get(Util.round(connectIndex * 0.5f)).point2.x + origin.offsetX, bones.get(Util.round(connectIndex * 0.5f)).point2.y + origin.offsetY);
                }
                else {
                    point1.set(bones.get(Util.round((connectIndex - 1) * 0.5f)).point1.x + origin.offsetX, bones.get(Util.round((connectIndex - 1) * 0.5f)).point1.y + origin.offsetY);
                }
            }
        }

        addBone.handleTouchDown(touchX, touchY);
        setColour.handleTouchDown(touchX, touchY);
        save.handleTouchDown(touchX, touchY);
        clear.handleTouchDown(touchX, touchY);
        connectBones.handleTouchDown(touchX, touchY);
        select.handleTouchDown(touchX, touchY);
        copy.handleTouchDown(touchX, touchY);
        delete.handleTouchDown(touchX, touchY);
        addKeyframe.handleTouchDown(touchX, touchY);
        selectAll.handleTouchDown(touchX, touchY);
        resetOrigin.handleTouchDown(touchX, touchY);
        ModelBone.offsetX = origin.offsetX;
        ModelBone.offsetY = origin.offsetY;
    }

    void handleTouchMove(float touchX, float touchY){
        origin.handleTouchMove(touchX, touchY);
        if(settingColour){
            picker.handleTouchMove(touchX, touchY);
        }
        if((addingBone || selecting) && point1.x != -1 && point1.y != -1 && point2.x != -1 && point2.y != -1){
            point2.set(touchX, touchY);
        }
        if(selecting && !dragSelect){
            dragSelect = (point2.x - point1.x) * (point2.x - point1.x) + (point2.y - point1.y) * (point2.y - point1.y) >= Util.screenX(25);
        }
        if(connectMode && connectIndex >= 0){
            float closestDist = Float.MAX_VALUE;
            for(int i = 0; i < bones.size(); i++){
                if(connectIndex % 2 == 0){
                    if(i != Util.round(connectIndex * 0.5f)){
                        if(bones.get(i).checkTouch(touchX, touchY) == 1){
                            if(Util.getDistSquared(touchX, touchY, bones.get(i).point1.x + origin.offsetX, bones.get(i).point1.y + origin.offsetY) < closestDist){
                                closestDist = Util.getDistSquared(touchX, touchY, bones.get(i).point1.x + origin.offsetX, bones.get(i).point1.y + origin.offsetY);
                                point2.set(bones.get(i).point1.x + origin.offsetX, bones.get(i).point1.y + origin.offsetY);
                                connectToIndex = i * 2;
                            }
                        }
                        if(bones.get(i).checkTouch(touchX, touchY) == 2){
                            if(Util.getDistSquared(touchX, touchY, bones.get(i).point2.x + origin.offsetX, bones.get(i).point2.y + origin.offsetY) < closestDist){
                                closestDist = Util.getDistSquared(touchX, touchY, bones.get(i).point1.x + origin.offsetX, bones.get(i).point1.y + origin.offsetY);
                                point2.set(bones.get(i).point2.x + origin.offsetX, bones.get(i).point2.y + origin.offsetY);
                                connectToIndex = i * 2 + 1;
                            }
                        }
                    }
                }
                if(connectIndex % 2 == 1){
                    if(i != Util.round((connectIndex - 1) * 0.5f)){
                        if(bones.get(i).checkTouch(touchX, touchY) == 1){
                            if(Util.getDistSquared(touchX, touchY, bones.get(i).point1.x + origin.offsetX, bones.get(i).point1.y + origin.offsetY) < closestDist){
                                closestDist = Util.getDistSquared(touchX, touchY, bones.get(i).point1.x + origin.offsetX, bones.get(i).point1.y + origin.offsetY);
                                point2.set(bones.get(i).point1.x + origin.offsetX, bones.get(i).point1.y + origin.offsetY);
                                connectToIndex = i * 2;
                            }
                        }
                        if(bones.get(i).checkTouch(touchX, touchY) == 2){
                            if(Util.getDistSquared(touchX, touchY, bones.get(i).point2.x + origin.offsetX, bones.get(i).point2.y + origin.offsetY) < closestDist){
                                closestDist = Util.getDistSquared(touchX, touchY, bones.get(i).point1.x + origin.offsetX, bones.get(i).point1.y + origin.offsetY);
                                point2.set(bones.get(i).point2.x + origin.offsetX, bones.get(i).point2.y + origin.offsetY);
                                connectToIndex = i * 2 + 1;
                            }
                        }
                    }
                }

            }
//            point2.set(touchX, touchY);
        }
        addBone.handleTouchMove(touchX, touchY);
        setColour.handleTouchMove(touchX, touchY);
        save.handleTouchMove(touchX, touchY);
        clear.handleTouchMove(touchX, touchY);
        connectBones.handleTouchMove(touchX, touchY);
        select.handleTouchMove(touchX, touchY);
        copy.handleTouchMove(touchX, touchY);
        delete.handleTouchMove(touchX, touchY);
        addKeyframe.handleTouchMove(touchX, touchY);
        selectAll.handleTouchMove(touchX, touchY);
        resetOrigin.handleTouchMove(touchX, touchY);
        ModelBone.offsetX = origin.offsetX;
        ModelBone.offsetY = origin.offsetY;
    }

    void handleTouchUp(float touchX, float touchY){
        origin.handleTouchUp();
        if(settingColour){
            if(picker.set.active && picker.set.checkTouch(touchX, touchY)){
                for(int i = 0; i < bones.size(); i++){
                    if(bones.get(i).selected){
                        bones.get(i).setColour(picker.getColour());
                    }
                }
            }
            picker.handleTouchUp(touchX, touchY);
        }
        if(addBone.checkTouch(touchX, touchY) && addBone.active){
            addingBone = !addingBone;
            settingColour = false;
            connectMode = false;
            selecting = false;
            deleting = false;
        }
        addBone.handleTouchUp();
        if(setColour.checkTouch(touchX, touchY) && setColour.active){
            settingColour = !settingColour;
            addingBone = false;
            connectMode = false;
            selecting = false;
            deleting = false;
        }
        setColour.handleTouchUp();
        if(save.checkTouch(touchX, touchY) && save.active){

        }
        save.handleTouchUp();
        if(clear.checkTouch(touchX, touchY) && clear.active){
            bones.clear();
            connectMode = false;
            addingBone = false;
            selecting = false;
            settingColour = false;
            deleting = false;
        }
        clear.handleTouchUp();
        if(connectBones.checkTouch(touchX, touchY) && connectBones.active){
            connectMode = !connectMode;
            addingBone = false;
            settingColour = false;
            selecting = false;
            deleting = false;
        }
        connectBones.handleTouchUp();
        if(select.checkTouch(touchX, touchY) && select.active){
            selecting = !selecting;
            addingBone = false;
            settingColour = false;
            connectMode = false;
            deleting = false;
        }
        select.handleTouchUp();
        if(copy.checkTouch(touchX, touchY) && copy.active){
            for(int i = 0; i < bones.size(); i++){
                if(bones.get(i).selected){
                    bones.add(i + 1, new ModelBone(new PointF(bones.get(i).point1.x + Util.screenX(10) + origin.offsetX, bones.get(i).point1.y - Util.screenX(10) + origin.offsetY), new PointF(bones.get(i).point2.x + Util.screenX(10) + origin.offsetX, bones.get(i).point2.y - Util.screenX(10) + origin.offsetY), origin));
                    bones.get(i + 1).setColour(bones.get(i).colour);
                    bones.get(i).selected = false;
                    i++;
                }
            }
        }
        copy.handleTouchUp();
        if(delete.checkTouch(touchX, touchY) && delete.active){
            for(int i = 0; i < bones.size(); i++){
                if(bones.get(i).selected){
                    bones.remove(i);
                    i--;
                }
            }
            if(bones.size() == 0){
                connectMode = false;
                addingBone = false;
                selecting = false;
                settingColour = false;
                deleting = false;
            }
        }
        delete.handleTouchUp();
        if(addingBone && point1.x != -1 && point1.y != -1 && point2.x != -1 && point2.y != -1){
            bones.add(new ModelBone(new PointF(point1.x, point1.y), new PointF(point2.x, point2.y), origin));
            bones.get(bones.size() - 1).setColour(picker.defaultColour);
            if(bones.size() > 1){
                bones.get(bones.size() - 2).selected = false;
            }
        }
        else if(selecting && point1.x != -1 && point1.y != -1 && point2.x != -1 && point2.y != -1){
            if(dragSelect){
                for(int i = 0; i < bones.size(); i++){
                    if(bones.get(i).withinRect(point1, point2)){
                        bones.get(i).selected = true;
                    }
                }
            }
            else{
                boolean earlyExit = false;
                for(int i = bones.size() - 1; i >= 0; i--){
                    if(bones.get(i).touchWithinBounds(touchX, touchY)){
                        bones.get(i).selected = !bones.get(i).selected;
                        earlyExit = true;
                        break;
                    }
                }
                if(!earlyExit){
                    for(int i = 0; i < bones.size(); i++){
                        bones.get(i).selected = false;
                    }
                }
            }
        }
        if(addKeyframe.active && addKeyframe.checkTouch(touchX, touchY)){
            if(bones.size() > 0){

            }
        }
        addKeyframe.handleTouchUp();
        if(selectAll.active && selectAll.checkTouch(touchX, touchY)){
            boolean deselect = true;
            for(int i = 0; i < bones.size(); i++){
                deselect = deselect && bones.get(i).selected;
            }
            if(deselect){
                for(int i = 0; i < bones.size(); i++){
                    bones.get(i).selected = false;
                }
            }
            else{
                for(int i = 0; i < bones.size(); i++){
                    bones.get(i).selected = true;
                }
            }
        }
        selectAll.handleTouchUp();
        if(resetOrigin.active && resetOrigin.checkTouch(touchX, touchY)){
            origin.offsetX = 0;
            origin.offsetY = 0;
            ModelBone.offsetX = 0;
            ModelBone.offsetY = 0;
        }
        if(connectMode && connectIndex >= 0 && connectToIndex >= 0){
            float shiftX = point2.x - point1.x;
            float shiftY = point2.y - point1.y;
            int index1, index2;
            if(connectIndex % 2 == 0){
                index1 = Util.round(connectIndex * 0.5f);
            }
            else{
                index1 = Util.round((connectIndex - 1) * 0.5f);
            }
            if(connectToIndex % 2 == 0){
                index2 = Util.round(connectToIndex * 0.5f);
            }
            else{
                index2 = Util.round((connectToIndex - 1) * 0.5f);
            }
            bones.get(index1).point1.set(bones.get(index1).point1.x + shiftX, bones.get(index1).point1.y + shiftY);
            bones.get(index1).point2.set(bones.get(index1).point2.x + shiftX, bones.get(index1).point2.y + shiftY);
            int end = bones.get(connectIndex).checkTouch(point1.x, point1.y);
            if(end == 1){
//                boolean
            }
            else if(end == 2){

            }
        }
        resetOrigin.handleTouchUp();
        point1.set(-1, -1);
        point2.set(-1, -1);
        ModelBone.offsetX = origin.offsetX;
        ModelBone.offsetY = origin.offsetY;

        dragSelect = false;
        connectIndex = -1;
        connectToIndex = -1;
    }

    void draw(Canvas canvas){
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        canvas.save();
        canvas.clipRect(left, top, right, bottom);

        for(int i = 0; i < bones.size(); i++){
            bones.get(i).draw(canvas);
        }
        if (settingColour) {
            picker.draw(canvas);
        }
        if((addingBone || (connectIndex >= 0 && connectMode)) && point1.x != -1 && point1.y != -1 && point2.x != -1 && point2.y != -1){
            canvas.drawLine(point1.x, point1.y, point2.x, point2.y, paint);
            if(connectIndex >= 0 && connectMode){
                float halfX = point1.x + ((point2.x - point1.x) * 0.5f), halfY = point1.y + ((point2.y - point1.y) * 0.5f);
                float length = Util.getMagnitude(point1.x, point1.y, point2.x, point2.y);
                float unitX = (point2.x - point1.x) / length, unitY = (point2.y - point1.y) / length;
                float perpX = -(point2.y - point1.y) / length, perpY = (point2.x - point1.x) / length;
                canvas.drawLine(halfX - (unitX * Util.screenX(15) + perpX * Util.screenX(15)), halfY - (unitY * Util.screenX(15) + perpY * Util.screenX(15)), halfX, halfY, paint);
                canvas.drawLine(halfX - (unitX * Util.screenX(15) - perpX * Util.screenX(15)), halfY - (unitY * Util.screenX(15) - perpY * Util.screenX(15)), halfX, halfY, paint);
            }
        }
        if(dragSelect){
            paint.setColor(Color.rgb(0, 120, 255));
            canvas.drawRect(point1.x, point1.y, point2.x, point2.y, paint);
            paint.setColor(Color.BLACK);
        }
        origin.draw(canvas);
        canvas.restore();
        addBone.draw(canvas);
        setColour.draw(canvas);
        save.draw(canvas);
        clear.draw(canvas);
        connectBones.draw(canvas);
        select.draw(canvas);
        copy.draw(canvas);
        delete.draw(canvas);
        addKeyframe.draw(canvas);
        selectAll.draw(canvas);
        resetOrigin.draw(canvas);
        canvas.drawRect(left, top, right, bottom, paint);
    }
}