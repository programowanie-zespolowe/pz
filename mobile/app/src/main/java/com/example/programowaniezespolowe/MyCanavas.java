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
    public MyCanavas(Context context, NextPoint nextPoint) {
        super(context);
        this.nextPoint = nextPoint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int id = 0;
        Bitmap bitmap;
        int deg = 0;
        if(nextPoint.getIcon() == 0){
            id = R.drawable.do_przodu;
        }else if(nextPoint.getIcon() == 1){
            id = R.drawable.lewo;
        }else if(nextPoint.getIcon() == 2){
            id = R.drawable.lewo_1;
        }else if(nextPoint.getIcon() == 3){
            id =R.drawable.zawroc;
        }else if(nextPoint.getIcon() == 4){
            id = R.drawable.prawo_1;
        }if(nextPoint.getIcon() == 5){
            id = R.drawable.prawo;
        }
        Matrix matrix = new Matrix();
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        bitmap= BitmapFactory.decodeResource(getResources(), id);
        matrix.setRotate(deg, (canvas.getWidth() - bitmap.getWidth())/2, (canvas.getHeight() - bitmap.getHeight())/2);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        canvas.drawBitmap(bitmap,(canvas.getWidth() - bitmap.getWidth())/2,(canvas.getHeight() - bitmap.getHeight())/2,p);
    }
}
