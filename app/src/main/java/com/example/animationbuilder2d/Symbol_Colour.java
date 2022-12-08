package com.example.animationbuilder2d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Symbol_Colour extends Symbol{
    Bitmap image;
    Rect bounds = new Rect();

    Symbol_Colour(Context context) {
        paint.setStyle(Paint.Style.FILL);
        bounds.set(Util.round(x - (size * 0.5f)), Util.round(y - (size * 0.5f)), Util.round(x + (size * 0.5f)), Util.round(y + (size * 0.5f)));
        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.clipart333036);
    }

    @Override
    void setPosition(float x, float y) {
        super.setPosition(x, y);
        bounds.set(Util.round(x - (size * 0.5f)), Util.round(y - (size * 0.5f)), Util.round(x + (size * 0.5f)), Util.round(y + (size * 0.5f)));
    }

    @Override
    void setSize(float size) {
        this.size = size;
        bounds.set(Util.round(x - (size * 0.5f)), Util.round(y - (size * 0.5f)), Util.round(x + (size * 0.5f)), Util.round(y + (size * 0.5f)));
    }

    @Override
    void draw(Canvas canvas) {
        canvas.drawBitmap(image, null, bounds, null);
    }
}
