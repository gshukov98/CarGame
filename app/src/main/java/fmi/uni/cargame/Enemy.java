package fmi.uni.cargame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

public class Enemy {
    Bitmap bitmap;
    Context context;
    int maxX;
    int maxY;
    int x;
    int y;
    int speed = 10;
    int boost;
    boolean isAlive = true;

    Random random = new Random();
    Rect detectCollision;

    int skins[] = {R.drawable.enemy1, R.drawable.enemy2, R.drawable.enemy,R.drawable.enemy3,R.drawable.enemy4,R.drawable.enemy5};


    public Enemy(Context context, int sizeX, int sizeY,int boost) {
        this.context = context;
        maxX = sizeX;
        maxY = sizeY;
        this.boost = boost;
        speed += boost;

        int line1 = maxX / 6;
        int line2 = maxX / 2;
        int line3 = (maxX / 3) + (maxX / 2);
        int[] lines = {line1, line2, line3};

        int[] randomY = {-500,-200,-300,-400};


        bitmap = BitmapFactory.decodeResource(context.getResources(), skins[random.nextInt(skins.length)]);

        x = lines[random.nextInt(lines.length)] - bitmap.getWidth() / 2;
        y = randomY[random.nextInt(randomY.length)];

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



