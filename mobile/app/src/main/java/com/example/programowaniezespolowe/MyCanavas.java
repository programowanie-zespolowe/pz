package com.example.programowaniezespolowe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MyCanavas extends View {
    public MyCanavas(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.images);
        canvas.drawBitmap(bitmap,(canvas.getWidth() - bitmap.getWidth())/2,(canvas.getHeight() - bitmap.getHeight())/2,p);
    }
}
