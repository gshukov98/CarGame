package fmi.uni.cargame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Line {
    int x;
    int y;
    int maxX;
    int maxY;
    int speed = 10;
    Bitmap bitmap;
    Context context;
    boolean isAlive = true;

    public Line(Context context, int screenX, int screenY) {

        this.context = context;
        maxX = screenX;
        maxY = screenY;
        this.x = maxX/3;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.line);

    }


    public void update() {
        y += speed;

        if (y < -10) {
            isAlive = false;
        }
    }

}
