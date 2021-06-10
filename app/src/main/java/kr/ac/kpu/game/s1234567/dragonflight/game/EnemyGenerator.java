package kr.ac.kpu.game.s1234567.dragonflight.game;

import android.graphics.Canvas;
import android.util.Log;

import java.util.Random;

import kr.ac.kpu.game.s1234567.dragonflight.Scene.MainGame;
import kr.ac.kpu.game.s1234567.dragonflight.framework.GameObject;
import kr.ac.kpu.game.s1234567.dragonflight.ui.view.GameView;

public class EnemyGenerator implements GameObject {

    private static float INITIAL_SPAWN_INTERVAL = 3.0f;
    private static final String TAG = EnemyGenerator.class.getSimpleName();
    private float time;
    private float spawnInterval;
    private int wave;

    public EnemyGenerator() {
        time = INITIAL_SPAWN_INTERVAL;
        spawnInterval = INITIAL_SPAWN_INTERVAL;
        wave = 0;
    }
    @Override
    public void update() {
        MainGame game = MainGame.get();
        time += game.frameTime;
        if (time >= spawnInterval) {
            generate();
            time -= spawnInterval;
        }
    }

    private void generate() {
        wave++;
        //Log.d(TAG, "Generate now !!");
        MainGame game = MainGame.get();
        int tenth = GameView.view.getWidth()/* / 10*/;
        Random r = new Random();
        /*for (int i = 1; i <= 9; i += 2) {
            int x = tenth * i;
            //Log.d(TAG, "generate: " + x);
            int y = 0;
            int level = wave / 10 - r.nextInt(3);
            if (level < 1) level = 1;
            if (level > 20) level = 20;
            Enemy enemy = Enemy.get(level, x, y, 700);
            game.add(MainGame.Layer.enemy, enemy);
        }*/
        for(int i = 0; i < r.nextInt(5)+3; ++ i)
        {
            int x= r.nextInt(tenth + 1);
            int y= 0;
            Enemy enemy = Enemy.get(1, x, y, r.nextInt(700)+400);
            game.add(MainGame.Layer.enemy, enemy);
        }
        INITIAL_SPAWN_INTERVAL -= 0.5f;
        if (INITIAL_SPAWN_INTERVAL < 1.0f)
            INITIAL_SPAWN_INTERVAL = 1.0f;
    }

    @Override
    public void draw(Canvas canvas) {
        // does nothing
    }
}
