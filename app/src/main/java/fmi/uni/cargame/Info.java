package fmi.uni.cargame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Info {

    Context context;
    int maxX;
    int maxY;
    int x;
    int y;

    Bitmap bitmap;

    public Info(Context context, int sizeX, int sizeY) {

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.info_text);

        this.context = context;
        maxX = sizeX - bitmap.getWidth();
        maxY = sizeY;

        x = maxX / 2;
        y = sizeY - bitmap.getHeight() - 450;

    }

    public void update(int move) {
        y += move;
    }
}