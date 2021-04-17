package kr.kpu.ac.kr.game.s2015180040.myapplication.Game;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

import kr.kpu.ac.kr.game.s2015180040.myapplication.Frame.Animation;
import kr.kpu.ac.kr.game.s2015180040.myapplication.Frame.main;
import kr.kpu.ac.kr.game.s2015180040.myapplication.R;
import kr.kpu.ac.kr.game.s2015180040.myapplication.Frame.GameBitmap;
import kr.kpu.ac.kr.game.s2015180040.myapplication.UI.GameView;


public class Monster implements GameObject {
    private static final float FRAMES_PER_SECOND = 8.0f;
    private static final int[] RESOURCE_IDS = {
            R.mipmap.enemy_01, R.mipmap.enemy_02, R.mipmap.enemy_03, R.mipmap.enemy_04, R.mipmap.enemy_05,
            R.mipmap.enemy_06, R.mipmap.enemy_07, R.mipmap.enemy_08, R.mipmap.enemy_09, R.mipmap.enemy_10,
            R.mipmap.enemy_11, R.mipmap.enemy_12, R.mipmap.enemy_13, R.mipmap.enemy_14, R.mipmap.enemy_15,
            R.mipmap.enemy_16, R.mipmap.enemy_17, R.mipmap.enemy_18, R.mipmap.enemy_19, R.mipmap.enemy_20,
    };
    private static final String TAG = Monster.class.getSimpleName();
    private float x;
    private GameBitmap bitmap;
    private int level;
    private float y;
    private int speed;

    private Monster() {
        Log.d(TAG, "Enemy constructor");
    }

    public static Monster get(int level, int x, int y, int speed) {
        main game = main.get();
        Monster enemy = (Monster) game.get(Monster.class);
        if (enemy == null) {
            enemy = new Monster();
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

        this.bitmap = new Animation(resId, FRAMES_PER_SECOND, 0);
    }

    @Override
    public void update() {
        main game = main.get();
        y += speed * game.frameTime;

        if (y > GameView.view.getHeight()) {
            game.remove(this);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        bitmap.draw(canvas, x, y);
    }

    /*@Override
    public void getBoundingRect(RectF rect) {
        bitmap.getBoundingRect(x, y, rect);
    }

    @Override
    public void recycle() {
        // 재활용통에 들어가는 시점에 불리는 함수. 현재는 할일없음.
    }*/
}
