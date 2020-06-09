package com.example.programowaniezespolowe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

import com.example.programowaniezespolowe.Data.NextPoint;

public class MyCanavas extends View {
    private NextPoint nextPoint;
    private int id;
    public MyCanavas(Context context, NextPoint nextPoint, int id) {
        super(context);
        this.nextPoint = nextPoint;
        this.id = id;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap;
        Matrix matrix = new Matrix();
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        bitmap= BitmapFactory.decodeResource(getResources(), id);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        canvas.drawBitmap(bitmap,(canvas.getWidth() - bitmap.getWidth())/2,(canvas.getHeight() - bitmap.getHeight())/2,p);
    }
}
