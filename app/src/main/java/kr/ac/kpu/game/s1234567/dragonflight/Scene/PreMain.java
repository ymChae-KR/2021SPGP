package kr.ac.kpu.game.s1234567.dragonflight.Scene;


import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import java.io.PipedReader;
import java.util.ArrayList;
import java.util.HashMap;

import kr.ac.kpu.game.s1234567.dragonflight.R;
import kr.ac.kpu.game.s1234567.dragonflight.framework.BoxCollidable;
import kr.ac.kpu.game.s1234567.dragonflight.framework.GameObject;
import kr.ac.kpu.game.s1234567.dragonflight.framework.Recyclable;
import kr.ac.kpu.game.s1234567.dragonflight.game.Bullet;
import kr.ac.kpu.game.s1234567.dragonflight.game.Button;
import kr.ac.kpu.game.s1234567.dragonflight.game.Enemy;
import kr.ac.kpu.game.s1234567.dragonflight.game.EnemyGenerator;
import kr.ac.kpu.game.s1234567.dragonflight.game.HP;
import kr.ac.kpu.game.s1234567.dragonflight.game.HorizonScrollBackground;
import kr.ac.kpu.game.s1234567.dragonflight.game.Player;
import kr.ac.kpu.game.s1234567.dragonflight.game.Score;
import kr.ac.kpu.game.s1234567.dragonflight.ui.view.GameView;
import kr.ac.kpu.game.s1234567.dragonflight.utils.CollisionHelper;

public class PreMain {
    private static final String TAG = PreMain.class.getSimpleName();
    // singleton
    private static PreMain instance;
    private Button bStart;
    private Button bEnd;
    public GameView.game_status gState = GameView.game_status.start;


    public static PreMain get() {
        if (instance == null) {
            instance = new PreMain();
        }
        return instance;
    }

    public float frameTime;
    private boolean initialized;

    ArrayList<ArrayList<GameObject>> layers;
    private static HashMap<Class, ArrayList<GameObject>> recycleBin = new HashMap<>();

    public void recycle(GameObject object) {
        Class clazz = object.getClass();
        ArrayList<GameObject> array = recycleBin.get(clazz);
        if (array == null) {
            array = new ArrayList<>();
            recycleBin.put(clazz, array);
        }
        array.add(object);
    }
    public GameObject get(Class clazz) {
        ArrayList<GameObject> array = recycleBin.get(clazz);
        if (array == null || array.isEmpty()) return null;
        return array.remove(0);
    }

    public enum Layer {
        bg, ui
    }
    public boolean initResources() {
        if (initialized) {
            return false;
        }
        int w = GameView.view.getWidth();
        int h = GameView.view.getHeight();

        initLayers(2);

        int margin = (int) (20 * GameView.MULTIPLIER);
        bStart = new Button(w / 2 , 3 * h / 4 + 200, 0, true);
        add(PreMain.Layer.ui, bStart);

       /* bEnd = new Button(w / 2 + 250, 3 * h / 4 + 200, 1);
        add(PreMain.Layer.ui, bEnd);*/

        HorizonScrollBackground bg = new HorizonScrollBackground(R.mipmap.bga, 5);
        add(PreMain.Layer.bg, bg);

        initialized = true;
        return true;
    }

    private void initLayers(int layerCount)
    {
        layers = new ArrayList<>();
        for (int i = 0; i < layerCount; i++) {
            layers.add(new ArrayList<>());
        }
    }

    public void update() {
        //if (!initialized) return;
        for (ArrayList<GameObject> objects: layers) {
            for (GameObject o : objects) {
                o.update();
            }
        }
    }

    public void draw(Canvas canvas) {
        //if (!initialized) return;
        for (ArrayList<GameObject> objects: layers) {
            for (GameObject o : objects) {
                o.draw(canvas);
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
//        if (action == MotionEvent.ACTION_DOWN) {
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE)
        {
            RectF rect = new RectF(event.getX(),event.getY(),event.getX() + 10,event.getY() + 10);

            if (CollisionHelper.collides_b(bStart, rect))
            {
                gState = GameView.game_status.run;
            }

            /*if (CollisionHelper.collides_b(bEnd, rect))
            {
                gState = GameView.game_status.end;
            }*/
//
            return true;
        }

        return false;
    }

    public void add(PreMain.Layer layer, GameObject gameObject) {
        GameView.view.post(new Runnable() {
            @Override
            public void run() {
                ArrayList<GameObject> objects = layers.get(layer.ordinal());
                objects.add(gameObject);
            }
        });
//        Log.d(TAG, "<A> object count = " + objects.size());
    }

    public void remove(GameObject gameObject) {
        remove(gameObject, true);
    }
    public void remove(GameObject gameObject, boolean delayed) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (ArrayList<GameObject> objects: layers) {
                    boolean removed = objects.remove(gameObject);
                    if (removed) {
                        if (gameObject instanceof Recyclable) {
                            ((Recyclable) gameObject).recycle();
                            recycle(gameObject);
                        }
                        //Log.d(TAG, "Removed: " + gameObject);
                        break;
                    }
                }
            }
        };
        if (delayed) {
            GameView.view.post(runnable);
        } else {
            runnable.run();
        }
    }
}
