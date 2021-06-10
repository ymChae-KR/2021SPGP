package kr.ac.kpu.game.s1234567.dragonflight.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

import kr.ac.kpu.game.s1234567.dragonflight.R;
import kr.ac.kpu.game.s1234567.dragonflight.Scene.MainGame;
import kr.ac.kpu.game.s1234567.dragonflight.framework.AnimationGameBitmap;
import kr.ac.kpu.game.s1234567.dragonflight.framework.BoxCollidable;
import kr.ac.kpu.game.s1234567.dragonflight.framework.GameBitmap;
import kr.ac.kpu.game.s1234567.dragonflight.framework.GameObject;
import kr.ac.kpu.game.s1234567.dragonflight.framework.Recyclable;
import kr.ac.kpu.game.s1234567.dragonflight.ui.view.GameView;

public class Enemy implements GameObject, BoxCollidable, Recyclable {
    private static final float FRAMES_PER_SECOND = 8.0f;
    private static final int[] RESOURCE_IDS = {
            R.mipmap.p_en_1,
    };
    private static final String TAG = Enemy.class.getSimpleName();
    private float x;
    private GameBitmap bitmap;
    private int level;
    private float y;
    private int speed;

    private Bitmap elseBit;

    private Enemy() {
        Log.d(TAG, "Enemy constructor");
    }

    public static Enemy get(int level, int x, int y, int speed) {
        MainGame game = MainGame.get();
        Enemy enemy = (Enemy) game.get(Enemy.class);
        if (enemy == null) {
            enemy = new Enemy();
        }

        enemy.init(level, x, y, speed);
        return enemy;
    }

    private void init(int level, int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.level = level;

        int resId = RESOURCE_IDS[level - 1];

        this.bitmap = new AnimationGameBitmap(resId, FRAMES_PER_SECOND, 3);
    }

    @Override
    public void update() {
        MainGame game = MainGame.get();
        y += speed * game.frameTime;

        if (y > GameView.view.getHeight() - 220) {
            Enemy enemy = Enemy.get(level, (int)this.x, (int)this.y, 0);
            //game.add(MainGame.Layer.enemy, enemy);
            MainGame.get().addatScore(10);

            game.remove(this);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        bitmap.draw(canvas, x, y);
    }

    @Override
    public void getBoundingRect(RectF rect) {
        bitmap.getBoundingRect(x, y, rect);
    }

    @Override
    public void recycle() {
        // 재활용통에 들어가는 시점에 불리는 함수. 현재는 할일없음.
    }


    public void SetEnemyBitmap(Bitmap bit)
    {
        this.elseBit = bit;
    }

}
