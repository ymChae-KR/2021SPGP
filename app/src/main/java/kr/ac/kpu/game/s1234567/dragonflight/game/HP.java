package kr.ac.kpu.game.s1234567.dragonflight.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import kr.ac.kpu.game.s1234567.dragonflight.R;
import kr.ac.kpu.game.s1234567.dragonflight.framework.GameBitmap;
import kr.ac.kpu.game.s1234567.dragonflight.framework.GameObject;
import kr.ac.kpu.game.s1234567.dragonflight.ui.view.GameView;

public class HP implements GameObject {
    private final Bitmap bitmap;
    private final int right;
    private final int top;

    public void subScore(int score) {
        this.iHp -= score;
    }
    public void addScore(int amount) {
        this.iHp += amount;
    }

    private int iHp;
    private Rect src = new Rect();
    private RectF dst = new RectF();

    public HP(int right, int top) {
        bitmap = GameBitmap.load(R.mipmap.heart);
        this.right = right;
        this.top = top;
        this.iHp = 0;
    }
    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        int nw = bitmap.getWidth();
        int nh = bitmap.getHeight();
        int x = right;
        int dw = (int) (nw * GameView.MULTIPLIER);
        int dh = (int) (nh * GameView.MULTIPLIER);
        x -= dw;

        for(int i =0; i < this.iHp; i+=1)
        {
            dst.set(x + (nw+20) * (i), top, x +(nw+20) * (i+1), top + dh);
            canvas.drawBitmap(bitmap, null, dst, null);
        }
    }

}