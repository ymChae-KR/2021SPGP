package kr.kpu.ac.kr.game.s2015180040.myapplication.Game;

import android.graphics.Canvas;

import java.util.Random;

import kr.kpu.ac.kr.game.s2015180040.myapplication.Frame.main;
import kr.kpu.ac.kr.game.s2015180040.myapplication.UI.GameView;
import kr.kpu.ac.kr.game.s2015180040.myapplication.Game.Monster;


public class MonsterGenerator implements GameObject {

    private static final float INITIAL_SPAWN_INTERVAL = 2.0f;
    private static final String TAG = MonsterGenerator.class.getSimpleName();
    private float time;
    private float spawnInterval;
    private int wave;

    public MonsterGenerator() {
        time = INITIAL_SPAWN_INTERVAL;
        spawnInterval = INITIAL_SPAWN_INTERVAL;
        wave = 0;
    }
    @Override
    public void update() {
        main game = main.get();
        time += game.frameTime;
        if (time >= spawnInterval) {
            generate();
            time -= spawnInterval;
        }
    }

    private void generate() {
        wave++;
        //Log.d(TAG, "Generate now !!");
        main game = main.get();
        int tenth = GameView.view.getWidth() / 10;
        Random r = new Random();
        for (int i = 1; i <= 9; i += 2) {
            int x = tenth * i;
            int y = 0;
            int level = wave / 10 - r.nextInt(3);
            if (level < 1) level = 1;
            if (level > 20) level = 20;
            Monster enemy = Monster.get(level, x, y, 700);
            game.add(main.Layer.enemy, enemy);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        // does nothing
    }
}
