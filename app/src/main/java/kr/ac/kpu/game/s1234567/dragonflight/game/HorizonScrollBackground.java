package kr.ac.kpu.game.s1234567.dragonflight.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import kr.ac.kpu.game.s1234567.dragonflight.Scene.MainGame;
import kr.ac.kpu.game.s1234567.dragonflight.framework.GameBitmap;
import kr.ac.kpu.game.s1234567.dragonflight.framework.GameObject;
import kr.ac.kpu.game.s1234567.dragonflight.ui.view.GameView;

public class HorizonScrollBackground implements GameObject {
    private final Bitmap bitmap;
    private final float speed;
    private float scroll;

    private Rect srcRect = new Rect();
    private RectF dstRect = new RectF();
    public HorizonScrollBackground(int resId, int speed) {
        this.speed = speed * GameView.MULTIPLIER;
        bitmap = GameBitmap.load(resId);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        srcRect.set(0, 0, w, h);
        float l = 0;//x - w / 2 * GameView.MULTIPLIER;
        float t = 0; //y - h / 2 * GameView.MULTIPLIER;
        float r = GameView.view.getWidth();
        float b = r * h / w;
        dstRect.set(l, t, r, b);
    }
    @Override
    public void update() {
        MainGame game = MainGame.get();
        float amount = speed * game.frameTime;
        scroll -= amount;
        //dstRect.left += amount;
        //dstRect.right += amount;
    }

    @Override
    public void draw(Canvas canvas) {
        int vw = GameView.view.getWidth();
        int vh = GameView.view.getHeight();
        int iw = bitmap.getWidth();
        int ih = bitmap.getHeight();

        int dw = vh * iw / ih;
        int currh = (int)scroll % dw;
        if (currh > 0) currh -= dw;

        while (currh < vw) {
            dstRect.set(currh,0 , currh + dw, vh );
            canvas.drawBitmap(bitmap, srcRect, dstRect, null);
            currh += dw;
        }
    }
}
