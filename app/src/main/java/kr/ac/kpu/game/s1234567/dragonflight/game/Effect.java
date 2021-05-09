package kr.ac.kpu.game.s1234567.dragonflight.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import kr.ac.kpu.game.s1234567.dragonflight.R;
import kr.ac.kpu.game.s1234567.dragonflight.framework.GameBitmap;
import kr.ac.kpu.game.s1234567.dragonflight.framework.GameObject;
import kr.ac.kpu.game.s1234567.dragonflight.ui.view.GameView;


public class Effect implements GameObject {
    private final Bitmap bitmap;
    private final int right;
    private final int top;

    private final int iCnt;
    private int anint;


    private Rect src = new Rect();
    private RectF dst = new RectF();

    public Effect(int right, int top, int iCnt) {
        bitmap = GameBitmap.load(R.mipmap.effect1);
        this.right = right;
        this.top = top;
        this.iCnt = iCnt;
        this.anint = 0;
    }

    @Override
    public void update() {
        if(this.anint == this.iCnt)
        {
            MainGame.get().remove(this, false);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        int nw = bitmap.getWidth() / this.anint;
        int nh = bitmap.getHeight();
        int x = right;
        int dw = (int) (nw * GameView.MULTIPLIER);
        int dh = (int) (nh * GameView.MULTIPLIER);
        x -= dw;
        dst.set(x + (nw+20) * (this.anint), top, x +(nw+20) * (this.anint+1), top + dh);
        canvas.drawBitmap(bitmap, null, dst, null);
        this.anint += 1;

    }


}
