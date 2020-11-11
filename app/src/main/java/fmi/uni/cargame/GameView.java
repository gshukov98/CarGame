package fmi.uni.cargame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;


public class GameView extends SurfaceView implements Runnable {

    boolean alive;

    Player player;
    Info info;

    ArrayList<Line> lines = new ArrayList<>();
    ArrayList<Enemy> enemies = new ArrayList<>();
    ArrayList<Hole> holes = new ArrayList<>();
    ArrayList<Heart> hearts = new ArrayList<>();

    Paint paint;
    SurfaceHolder surfaceHolder;
    Canvas canvas;
    Thread gameThread;
    int score = 0;
    int highScore;
    int boost = 0;
    int lives = 1;


    int lineWarmUP = 0;
    final int LINE_COOLDOWN = 20;

    int holeWarmUP = 0;
    final int HOLE_COOLDOWN = 150;

    int enemyWarmUP = 0;
    final int ENEMY_COOLDOWN = 200;

    int sizeX;
    int sizeY;

    private float x1, x2;
    static final int MIN_DISTANCE = 150;


    SharedPreferences.Editor editor = getContext().getSharedPreferences("Highscore", Context.MODE_PRIVATE).edit();
    SharedPreferences pref = getContext().getSharedPreferences("Highscore", Context.MODE_PRIVATE);


    public GameView(Context context, int sizeX, int sizeY) {
        super(context);

        alive = true;
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        player = new Player(context, sizeX, sizeY);
        info = new Info(context, sizeX, sizeY);
        enemies.add(new Enemy(context, sizeX, sizeY, 0));

        surfaceHolder = getHolder();
        paint = new Paint();

        gameThread = new Thread(this);
        gameThread.start();


        highScore = pref.getInt("maxScore", 0);

    }

    @Override
    public void run() {

        while (alive) {

            frameRate();
            update();
            draw();
            score += 1;
            lineWarmUP++;
            enemyWarmUP++;
            holeWarmUP++;

            if (score % 1000 == 0) boost += 3;

            if (score % 2000 == 0) {
                hearts.add(new Heart(getContext(), sizeX, sizeY));
            }

            if (enemyWarmUP >= ENEMY_COOLDOWN) {
                enemies.add(new Enemy(getContext(), sizeX, sizeY, boost));
                if (score > 4000) enemies.add(new Enemy(getContext(), sizeX, sizeY, boost));
                enemyWarmUP = 0;
            }

            if (score % 300 == 0) {
                enemies.add(new Enemy(getContext(), sizeX, sizeY, boost));
            }

            if (score % 1200 == 0) {
                enemies.add(new Enemy(getContext(), sizeX, sizeY, boost));
            }

            if (holeWarmUP == HOLE_COOLDOWN) {
                holes.add(new Hole(getContext(), sizeX, sizeY));
                holeWarmUP = 0;
            }

            if (lineWarmUP == LINE_COOLDOWN) {
                lines.add(new Line(getContext(), sizeX, sizeY));
                lineWarmUP = 0;
            }

        }

        if (score > highScore) {
            highScore = score;
            editor.putInt("maxScore", highScore);

            editor.apply();
            editor.commit();

        }


    }

    public void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();

            canvas.drawColor(Color.DKGRAY);

            paint.setColor(Color.WHITE);
            paint.setTextSize(40);

            canvas.drawBitmap(
                    info.bitmap,
                    info.x,
                    info.y,
                    paint);

            for (Heart heart : hearts) {
                canvas.drawBitmap(
                        heart.bitmap,
                        heart.x,
                        heart.y,
                        paint
                );
                if (Rect.intersects(heart.detectCollision, player.detectCollision)) {
                    heart.isAlive = false;
                    lives++;
                    player.addLife();
                }

            }

            for (Hole hole : holes) {
                canvas.drawBitmap(
                        hole.bitmap,
                        hole.x,
                        hole.y,
                        paint
                );


                if (Rect.intersects(hole.detectCollision, player.detectCollision)) {
                    if (score < 1000) {
                        score -= 5;
                    }
                    if (score > 1000) {
                        score -= 7;
                    }
                    if (score > 3000) {
                        score -= 9;
                    }
                    if (score > 5000) {
                        score -= 11;
                    }
                }
            }

            for (Enemy enemy : enemies) {
                canvas.drawBitmap(
                        enemy.bitmap,
                        enemy.x,
                        enemy.y,
                        paint
                );

                if (Rect.intersects(enemy.detectCollision, player.detectCollision)) {
                    lives--;
                    player.removeLife();
                }
            }


            for (Line line : lines) {
                canvas.drawBitmap(line.bitmap, line.x, line.y, paint);
                canvas.drawBitmap(line.bitmap, (line.x) * 2, line.y, paint);
            }


            canvas.drawBitmap(player.bitmap, player.x, player.y, paint);

            canvas.drawText("Score: " + score, 50, 100, paint);
            canvas.drawText("High score: " + highScore, 50, 150, paint);
            canvas.drawText("Lives: " + lives, 50, 200, paint);


            if (!alive) {
                paint.setTextSize(70);
                paint.setColor(Color.RED);
                String gameOver = "GAME OVER!";
                canvas.drawText(gameOver, canvas.getWidth() / 3, canvas.getHeight() / 2, paint);
                String game = "TAP ON SCREEN";
                canvas.drawText(game, 50, 310, paint);
                String game1 = "TO START GAME AGAIN!";
                canvas.drawText(game1, 50, 380, paint);
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void update() {

        info.update(8);

        for (Line line : lines) {
            line.update();
        }

        for (Hole hole : holes) {
            hole.update();
        }

        for (Heart heart : hearts) {
            heart.update();
        }

        for (Heart heart : hearts) {
            for (Hole hole : holes) {
                for (Enemy enemy : enemies) {
                    if (Rect.intersects(hole.detectCollision, heart.detectCollision)) {
                        hole.y = -100;
                    }
                    if (Rect.intersects(enemy.detectCollision, heart.detectCollision)) {
                        heart.y = -200;
                    }
                    if (Rect.intersects(hole.detectCollision, enemy.detectCollision)) {
                        hole.y = -250;
                    }
                }
            }
        }

        for (int k = 0; k < hearts.size(); k++) {
            if (hearts.get(k).isAlive == false) {
                hearts.remove(k);
            }
        }

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).isAlive == false) {
                lines.remove(i);
            }
        }

        for (int g = 0; g < holes.size(); g++) {
            if (holes.get(g).isAlive == false) {
                holes.remove(g);
            }
        }

        for (int j = 0; j < enemies.size(); j++) {
            if (enemies.get(j).isAlive == false) {
                enemies.remove(j);
            }
        }

        for (Enemy enemy : enemies) {
            enemy.update();

            if (Rect.intersects(enemy.detectCollision, player.detectCollision)) {
                enemy.y = 5000;
            }
        }

        if (score < 0 || lives < 1) {
            alive = false;
        }
    }

    private void frameRate() {
        try {
            gameThread.sleep(33);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();

                if (!alive) {

                    Intent intent = new Intent(getContext(), GameActivity.class);
                    getContext().startActivity(intent);

                }

                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float x = x2 - x1;

                if (Math.abs(x) > MIN_DISTANCE) {

                    if (x2 > x1) {
                        player.update((getWidth() / 3) - (player.bitmap.getWidth() / 2));
                    } else {
                        player.update((-getWidth() / 3) + (player.bitmap.getWidth() / 2));
                    }

                } else {
                    Toast.makeText(getContext(),"Swipe more to switch the line!",Toast.LENGTH_SHORT).show();
                }

                break;


        }
        return true;
    }

}
