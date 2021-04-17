package kr.kpu.ac.kr.game.s2015180040.myapplication.Game;

import android.graphics.Canvas;
import android.graphics.RectF;

import kr.kpu.ac.kr.game.s2015180040.myapplication.Frame.GameBitmap;
import kr.kpu.ac.kr.game.s2015180040.myapplication.Frame.main;
import kr.kpu.ac.kr.game.s2015180040.myapplication.R;

public class Player implements GameObject {
    private static final String TAG = Player.class.getSimpleName();
    private static final int BULLET_SPEED = 1500;
    private static final float FIRE_INTERVAL = 1.0f / 7.5f;
    private static final float LASER_DURATION = FIRE_INTERVAL / 3;
    private float fireTime;
    private float x, y;
    private float tx, ty;
    private float speed;
    private GameBitmap playerBitmap;
    private GameBitmap fireBitmap;

    public Player(float x, float y) {
        this.x = x;
        this.y = y;
        this.tx = x;
        this.ty = 0;
        this.speed = 800;
        this.playerBitmap = new GameBitmap(R.mipmap.cookie);

        this.fireTime = 0.0f;
    }

    public void moveTo(float x, float y) {
        this.tx = x;
        //this.ty = this.y;
    }

    public void update() {
        main game = main.get();
        float dx = speed * game.frameTime;
        if (tx < x) { // move left
            dx = -dx;
        }
        x += dx;
        if ((dx > 0 && x > tx) || (dx < 0 && x < tx)) {
            x = tx;
        }

        fireTime += game.frameTime;

        }

    public void draw(Canvas canvas) {
        playerBitmap.draw(canvas, x, y);
        if (fireTime < LASER_DURATION) {
            fireBitmap.draw(canvas, x, y - 50);
        }
    }

}