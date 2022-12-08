package com.example.animationbuilder2d;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.RequiresApi;

import java.text.DecimalFormat;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    DecimalFormat df = new DecimalFormat("#.##");
    private MainThread thread;
    public Paint paint = new Paint();
    PlaybackBar bar = new PlaybackBar(Util.width() * 0.5f + Util.screenX(40), Util.screenY(645), Util.width() + Util.screenX(80), Util.screenY(150), Color.rgb(200, 200, 200));
    ModelWindow window;
//    Joint joint = new Joint(Util.width() * 0.5f - Util.screenX(100), Util.height() * 0.5f, Util.width() * 0.5f, Util.height() * 0.5f, Util.width() * 0.5f + Util.screenX(150), Util.height() * 0.5f);
    float x = 0, y = 0;
//    Bezier curve = new Bezier(new PointF(Util.width() * 0.5f - 80, Util.height() * 0.5f - 80), new PointF(Util.width() * 0.5f - 80, Util.height() * 0.5f + 80), new PointF(Util.width() * 0.5f + 80, Util.height() * 0.5f + 80), new PointF(Util.width() * 0.5f + 80, Util.height() * 0.5f - 80));

    public GamePanel(Context context) {
        super(context);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
        window = new ModelWindow(Util.screenX(80), 0, Util.width(), bar.top, context);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                bar.handleTouchDown(event.getX(), event.getY());
//                joint.handleTouchDown(event.getX(), event.getY());
                window.handleTouchDown(event.getX(), event.getY());
//                curve.handleTouchDown(event.getX(), event.getY());
                this.x = event.getX();
                this.y = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                bar.handleTouchMove(event.getX(), event.getY());
//                joint.handleTouchMove(event.getX(), event.getY());
                this.x = event.getX();
                this.y = event.getY();
                window.handleTouchMove(event.getX(), event.getY());
//                curve.handleTouchMove(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                bar.handleTouchUp(event.getX(), event.getY());
//                joint.handleTouchUp();
                this.x = event.getX();
                this.y = event.getY();
                window.handleTouchUp(event.getX(), event.getY());
//                curve.handleTouchUp();
                break;
        }
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        paint.setTextSize(40);
        paint.setStrokeWidth(4);
        paint.setColor(Color.BLACK);
        super.draw(canvas);
        canvas.drawColor(Color.rgb(255, 255, 255));
        bar.draw(canvas);
        window.draw(canvas);
        canvas.drawText("FPS: " + df.format(MainThread.getFPS()), 10, 50, paint);
        canvas.drawText("bones: " + df.format(window.bones.size()), 10, 90, paint);
//        canvas.drawText(joint.check, 10, 90, paint);
//        canvas.drawText(joint.check2, 10, 130, paint);
//        canvas.drawLine(joint.point1.x, joint.point1.y, joint.point1.x + (joint.dotProduct(joint.point3.x, joint.point3.y, joint.parallelVecX, joint.parallelVecY) - joint.dotProduct(joint.point2.x, joint.point2.y, joint.parallelVecX, joint.parallelVecY)) * joint.parallelVecX, joint.point1.y + (joint.dotProduct(joint.point3.x, joint.point3.y, joint.parallelVecX, joint.parallelVecY) - joint.dotProduct(joint.point2.x, joint.point2.y, joint.parallelVecX, joint.parallelVecY)) * joint.parallelVecY, paint);
//        canvas.drawLine(joint.point3.x, joint.point3.y, joint.point3.x - (joint.dotProduct(joint.point3.x, joint.point3.y, joint.parallelVecX, joint.parallelVecY) - joint.dotProduct(joint.point2.x, joint.point2.y, joint.parallelVecX, joint.parallelVecY)) * joint.parallelVecX, joint.point3.y - (joint.dotProduct(joint.point3.x, joint.point3.y, joint.parallelVecX, joint.parallelVecY) - joint.dotProduct(joint.point2.x, joint.point2.y, joint.parallelVecX, joint.parallelVecY)) * joint.parallelVecY, paint);
//        joint.draw(canvas);
    }
}