package kr.kpu.ac.kr.game.s2015180040.myapplication.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Choreographer;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import kr.kpu.ac.kr.game.s2015180040.myapplication.Frame.main;


public class GameView extends View {
    private static final String TAG = GameView.class.getSimpleName();

    public static float MULTIPLIER = 2;
    private boolean running;

    private long lastFrame;
    public static GameView view;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        GameView.view = this;
        Sound.init(context);
        running = true;
//        startUpdating();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSize: " + w + "," + h);
        main game = main.get();
        boolean justInitialized = game.initResources();
        if (justInitialized) {
            requestCallback();
        }
    }

    private void update() {
        main game = main.get();
        game.update();

        invalidate();
    }

    private void requestCallback() {
        if (!running) {
            Log.d(TAG, "Not running. Not calling Choreographer.postFrameCallback()");
            return;
        }
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long time) {
                if (lastFrame == 0) {
                    lastFrame = time;
                }
                main game = main.get();
                game.frameTime = (float) (time - lastFrame) / 1_000_000_000;
                update();
                lastFrame = time;
                requestCallback();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        main game = main.get();
        game.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        main game = main.get();
        return game.onTouchEvent(event);
    }

    public void pauseGame() {
        running = false;
    }

    public void resumeGame() {
        if (!running) {
            running = true;
            lastFrame = 0;
            requestCallback();
        }
    }
}












