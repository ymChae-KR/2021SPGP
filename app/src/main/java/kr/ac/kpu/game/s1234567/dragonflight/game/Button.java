package kr.ac.kpu.game.s1234567.dragonflight.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import kr.ac.kpu.game.s1234567.dragonflight.R;
import kr.ac.kpu.game.s1234567.dragonflight.framework.AnimationGameBitmap;
import kr.ac.kpu.game.s1234567.dragonflight.framework.BoxCollidable;
import kr.ac.kpu.game.s1234567.dragonflight.framework.GameBitmap;
import kr.ac.kpu.game.s1234567.dragonflight.framework.GameObject;
import kr.ac.kpu.game.s1234567.dragonflight.ui.view.GameView;

public class Button implements GameObject, BoxCollidable {
    private static final float FRAMES_PER_SECOND = 8.0f;
    private static final int[] RESOURCE_IDS = {
            R.mipmap.start, R.mipmap.exit,R.mipmap.attack,R.mipmap.def,
    };

    private final GameBitmap bitmap;
    private final int right;
    private final int top;
    private float cooltime;
    public boolean IsActive = true;

    private Rect src = new Rect();
    private RectF dst = new RectF();

    public Button(int right, int top, int resID, boolean bAni) {
        //bitmap = GameBitmap.load(R.mipmap.start);
        resID = RESOURCE_IDS[resID];
        if(bAni)
            bitmap = new AnimationGameBitmap(resID, FRAMES_PER_SECOND, 0);
        else
            bitmap = new GameBitmap(resID);

        this.right = right;
        this.top = top;
    }
    @Override
    public void update() {
        if(!IsActive)
            cooltime += System.currentTimeMillis();

        if(cooltime > 1.0) {
            IsActive = true;
            cooltime = 0;
        }
    }

    @Override
    public void draw(Canvas canvas) {

        bitmap.draw(canvas, right, top);

       /* int nw = bitmap.getWidth();
        int nh = bitmap.getHeight();
        int x = right;
        int dw = (int) (nw * GameView.MULTIPLIER);
        int dh = (int) (nh * GameView.MULTIPLIER);
        x -= dw;

        dst.set(x + (nw+20) * (1), top, x +(nw+20) * (1+1), top + dh);
        canvas.drawBitmap(bitmap, null, dst, null);*/

    }

    @Override
    public void getBoundingRect(RectF rect) {
        bitmap.getBoundingRect(right, top, rect);
    }
}


