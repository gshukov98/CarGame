package fmi.uni.cargame;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Rect;

public class Player {

    Context context;
    int maxX;
    int maxY;
    int x;
    int y;
    int lives = 1;
    boolean isAlive = true;

    Bitmap bitmap;
    Rect detectCollision;

    public Player(Context context, int sizeX, int sizeY) {

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);

        this.context = context;
        maxX = sizeX - bitmap.getWidth();
        maxY = sizeY;


        x = maxX / 2;
        y = sizeY - bitmap.getHeight() - 100;

        detectCollision = new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());

    }

    public void addLife() {
        lives++;
    }

    public void removeLife() {
        lives--;
    }

    public void update(int moveX) {
        x += moveX;


        if (x < maxX / 3) {
            x = maxX / 6;
        } else if (x > maxX - (maxX / 3)) {
            x = (maxX / 3) + (maxX / 2);
        }


        detectCollision.left = x;
        detectCollision.right = x + bitmap.getWidth();

        if (lives < 1) {
            isAlive = false;
        }


    }
}
