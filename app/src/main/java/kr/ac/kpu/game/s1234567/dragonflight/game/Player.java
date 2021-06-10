package kr.ac.kpu.game.s1234567.dragonflight.game;

import android.graphics.Canvas;
import android.graphics.RectF;

import kr.ac.kpu.game.s1234567.dragonflight.R;
import kr.ac.kpu.game.s1234567.dragonflight.Scene.MainGame;
import kr.ac.kpu.game.s1234567.dragonflight.framework.AnimationGameBitmap;
import kr.ac.kpu.game.s1234567.dragonflight.framework.BoxCollidable;
import kr.ac.kpu.game.s1234567.dragonflight.framework.GameBitmap;
import kr.ac.kpu.game.s1234567.dragonflight.framework.GameObject;

public class Player implements GameObject, BoxCollidable {
    private static final String TAG = Player.class.getSimpleName();
    private static final int BULLET_SPEED = 1500;
    private static final float FIRE_INTERVAL = 1.0f / 7.5f;
    private static final float LASER_DURATION = FIRE_INTERVAL / 3;
    private static final float FRAMES_PER_SECOND = 12.0f;
    private float fireTime;
    private float x, y;
    private float tx, ty;
    private float speed;
    private GameBitmap planeBitmap;
    private GameBitmap fireBitmap;
    private GameBitmap Bitmap_R;
    private GameBitmap Bitmap_L;
    private GameBitmap skill_R;
    private GameBitmap skill_L;
    private boolean defend;
    private float fDefendTime;


    public int iLife;

    public Player(float x, float y) {
        this.x = x;
        this.y = y;
        this.tx = x;
        this.ty = 0;
        this.speed = 800;
        //this.planeBitmap = new GameBitmap(R.mipmap.pmove);
        this.planeBitmap = new AnimationGameBitmap(R.mipmap.pmr,FRAMES_PER_SECOND,4);
        this.Bitmap_R = new AnimationGameBitmap(R.mipmap.pmr,FRAMES_PER_SECOND,4);
        this.Bitmap_L = new AnimationGameBitmap(R.mipmap.pml,FRAMES_PER_SECOND,4);
        this.skill_R = new AnimationGameBitmap(R.mipmap.skr,FRAMES_PER_SECOND,3);
        this.skill_L = new AnimationGameBitmap(R.mipmap.skl,FRAMES_PER_SECOND,3);

        this.fireBitmap = new GameBitmap(R.mipmap.laser_0);
        this.fireTime = 0.0f;

        this.iLife = 3;
        this.defend = false;
        this.fDefendTime = 0.0f;
    }

    public void moveTo(float x, float y) {
        this.tx = x;
        //this.ty = this.y;
    }

    public void update() {
        MainGame game = MainGame.get();
        float dx = speed * game.frameTime;
        if (tx < x) { // move left
            dx = -dx;
            if(!defend)
                planeBitmap = Bitmap_L;
            else
                planeBitmap = skill_L;
        }
        x += dx;
        if ((dx > 0 && x > tx) || (dx < 0 && x < tx)) {
            x = tx;
            if(!defend)
                planeBitmap = Bitmap_R;
            else
                planeBitmap = skill_R;
        }

        fireTime += game.frameTime;
        if(defend)
        {
            fDefendTime += game.frameTime;
            if(fDefendTime > 1.0f)
            {
                defend = false;
                fDefendTime = 0.0f;
            }

        }
        /*if (fireTime >= FIRE_INTERVAL) {
            fireBullet();
            fireTime -= FIRE_INTERVAL;
        }*/
    }

    private void fireBullet() {
        Bullet bullet = Bullet.get(this.x, this.y, BULLET_SPEED);
        MainGame game = MainGame.get();
        game.add(MainGame.Layer.bullet, bullet);
    }

    public void draw(Canvas canvas) {
        planeBitmap.draw(canvas, x, y);
        if (fireTime < LASER_DURATION) {
            fireBitmap.draw(canvas, x, y - 50);
        }
    }

    @Override
    public void getBoundingRect(RectF rect) {
        planeBitmap.getBoundingRect(x, y, rect);
    }

    public float getPlayerX(){return this.x;}

    public boolean getDefend(){return this.defend;}

    public void setDefend(boolean _de){this.defend = _de;}
}
