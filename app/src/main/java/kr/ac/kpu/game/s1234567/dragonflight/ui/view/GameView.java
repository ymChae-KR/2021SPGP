package kr.ac.kpu.game.s1234567.dragonflight.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Choreographer;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import kr.ac.kpu.game.s1234567.dragonflight.R;
import kr.ac.kpu.game.s1234567.dragonflight.Scene.EndGame;
import kr.ac.kpu.game.s1234567.dragonflight.Scene.PreMain;
import kr.ac.kpu.game.s1234567.dragonflight.framework.Sound;
import kr.ac.kpu.game.s1234567.dragonflight.Scene.MainGame;
import android.media.MediaPlayer;

public class GameView extends View {
    private static final String TAG = GameView.class.getSimpleName();
    public static float MULTIPLIER = 2;
    private boolean running;

    public enum game_status {
        start, run, end
    }
    private game_status state;
    private long lastFrame;
    public static GameView view;
    private final MediaPlayer mediaPlayer;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        GameView.view = this;
        Sound.init(context);
        state = game_status.start;
        running = true;

        mediaPlayer = MediaPlayer.create(GameView.view.getContext(), R.raw.lobby);
        mediaPlayer.seekTo(0);
        mediaPlayer.start();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSize: " + w + "," + h);

        MainGame game = MainGame.get();
        boolean justInitialized = game.initResources();
        if (justInitialized) {
            requestCallback();
        }

        PreMain pGame = PreMain.get();
        boolean pjustInitialized = pGame.initResources();
        if (pjustInitialized) {
            requestCallback();
        }

        EndGame eGame = EndGame.get();
        boolean ejustInitialized = eGame.initResources();
        if (ejustInitialized) {
            requestCallback();
        }
    }

    private void update() {
        switch (state)
        {
            case start:

                PreMain pre = PreMain.get();
                pre.update();
                state = pre.gState;
                invalidate();

                break;
            case run:
                if(running) {
                    MainGame game = MainGame.get();
                    game.update();
                    invalidate();
                }
                break;
            case end:
                EndGame end = EndGame.get();
                end.update();
                state = end.gState;
                invalidate();

                break;

        }

        /*if(state == game_status.run)
        {
            running = true;
        }
        else
        {
            running = false;
        }*/
    }

    private void requestCallback()
    {
        /*if (!running) {
            Log.d(TAG, "Not running. Not calling Choreographer.postFrameCallback()");
            return;
        }*/
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long time) {
                if (lastFrame == 0) {
                    lastFrame = time;
                }

                PreMain pGame = PreMain.get();
                pGame.frameTime = (float) (time - lastFrame) / 1_000_000_000;

                MainGame game = MainGame.get();
                game.frameTime = (float) (time - lastFrame) / 1_000_000_000;

                EndGame eGame = EndGame.get();
                eGame.frameTime = (float) (time - lastFrame) / 1_000_000_000;

                update();
                lastFrame = time;
                requestCallback();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (state)
        {
            case start:
                PreMain pGame = PreMain.get();
                pGame.draw(canvas);
                break;
            case run:
                MainGame game = MainGame.get();
                game.draw(canvas);
                break;
            case end:
                EndGame eGame = EndGame.get();
                eGame.draw(canvas);
                break;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (state)
        {
            case start:
                PreMain pMain = PreMain.get();
                return pMain.onTouchEvent(event);
            case run:
                MainGame game = MainGame.get();
                return game.onTouchEvent(event);
            case end:
                EndGame eGame = EndGame.get();
                return eGame.onTouchEvent(event);
        }
        return false;
    }

    public void pauseGame() {
        //running = false;
        state = game_status.end;
        EndGame.get().SetScore(MainGame.get().getScore());
    }

    public void resumeGame() {
        if (!running)
        {
            state = game_status.run;
            running = true;
            lastFrame = 0;
            requestCallback();
        }
    }

    public void restart()
    {
        state = game_status.run;
        running = true;
        lastFrame = 0;
        MainGame.get().resetGame();
        requestCallback();
    }

    public void setState(game_status gs)
    {
        state =gs;
    }

}













