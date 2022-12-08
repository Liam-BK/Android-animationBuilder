package com.example.animationbuilder2d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Symbol_Select extends Symbol{
    Bitmap image;
    Rect bounds = new Rect();

    Symbol_Select(Context context) {
        paint.setStyle(Paint.Style.FILL);
        bounds.set(Util.round(x - (size * 0.5f)), Util.round(y - (size * 0.5f)), Util.round(x + (size * 0.5f)), Util.round(y + (size * 0.5f)));
        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.clipart531502);
    }

    @Override
    void setPosition(float x, float y) {
        super.setPosition(x, y);
        bounds.set(Util.round(x - (size * 0.5f)), Util.round(y - (size * 0.5f)), Util.round(x + (size * 0.5f)), Util.round(y + (size * 0.5f)));
    }

    @Override
    void setSize(float size) {
        this.size = size;
        bounds.set(Util.round(x - (size * 0.4f)), Util.round(y - (size * 0.5f)), Util.round(x + (size * 0.4f)), Util.round(y + (size * 0.5f)));
    }

    @Override
    void draw(Canvas canvas) {
        canvas.drawBitmap(image, null, bounds, null);
    }
}
