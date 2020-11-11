package fmi.uni.cargame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

public class Hole {
    Bitmap bitmap;
    Context context;
    int maxX;
    int maxY;
    int x;
    int y;
    int speed = 10;
    boolean isAlive = true;

    Random random = new Random();
    Rect detectCollision;

    public Hole(Context context,int sizeX,int sizeY){

        this.context = context;
        maxX = sizeX;
        maxY = sizeY;

        int line1 = maxX / 6;
        int line2 = maxX / 2;
        int line3 = (maxX / 3) + (maxX / 2);
        int[] lines = {line1, line2, line3};

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.hole);

        x = lines[random.nextInt(lines.length)] - bitmap.getWidth() / 2;
        y = -2000;

        detectCollision = new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
    }

    public void update() {
        y += speed;

        int line1 = maxX / 6;
        int line2 = maxX / 2;
        int line3 = (maxX / 3) + (maxX / 2);
        int[] lines = {line1, line2, line3};

        if (y > maxY) {
            y = -2000;
            x = lines[random.nextInt(lines.length)] - bitmap.getWidth();

            detectCollision.left = x;
            detectCollision.right = x + bitmap.getWidth();

            isAlive = false;

        }

        detectCollision.top = y;
        detectCollision.bottom = y + bitmap.getHeight();

    }
}
