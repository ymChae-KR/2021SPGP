package kr.ac.kpu.game.s1234567.dragonflight.Scene;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.HashMap;

import kr.ac.kpu.game.s1234567.dragonflight.R;
import kr.ac.kpu.game.s1234567.dragonflight.framework.GameObject;
import kr.ac.kpu.game.s1234567.dragonflight.framework.Recyclable;
import kr.ac.kpu.game.s1234567.dragonflight.framework.Sound;
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


public class MainGame {
        private static final String TAG = MainGame.class.getSimpleName();
    // singleton
    private static MainGame instance;
    private Player player;
    private Score score;
    private HP hp;
    private Button bAttack;
    private Button bHide;


    public static MainGame get() {
        if (instance == null) {
            instance = new MainGame();
        }
        return instance;
    }
    public float frameTime;
    private boolean initialized;

//    Player player;
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
        bg1, enemy, bullet, player, bg2, ui, controller, ENEMY_COUNT, effect,
    }
    public boolean initResources() {
        if (initialized) {
            return false;
        }
        int w = GameView.view.getWidth();
        int h = GameView.view.getHeight();

        initLayers(Layer.ENEMY_COUNT.ordinal());

        player = new Player(w/2, h - 280);
        //layers.get(Layer.player.ordinal()).add(player);
        add(Layer.player, player);
        add(Layer.controller, new EnemyGenerator());

        int margin = (int) (20 * GameView.MULTIPLIER);
        score = new Score(w - margin, margin);
        score.setScore(0);
        add(Layer.ui, score);

        hp = new HP(200, margin);
        hp.addScore(player.iLife);
        add(Layer.ui, hp);

        bAttack = new Button(100 ,  h -500, 2, false);
        add(MainGame.Layer.ui, bAttack);

        bHide = new Button(100 ,  h - 800 , 3, false);
        add(MainGame.Layer.ui, bHide);

        HorizonScrollBackground bg = new HorizonScrollBackground(R.mipmap.ms_bg2, 10);
        add(Layer.bg1, bg);


        initialized = true;
        return true;
    }

    private void initLayers(int layerCount) {
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

        ArrayList<GameObject> playerz = layers.get(Layer.player.ordinal());
        ArrayList<GameObject> enemies = layers.get(Layer.enemy.ordinal());
        ArrayList<GameObject> bullets = layers.get(Layer.bullet.ordinal());
        for (GameObject o1: enemies) {
            Enemy enemy = (Enemy) o1;
            boolean collided = false;
            for (GameObject o2: bullets) {
                Bullet bullet = (Bullet) o2;
                if (CollisionHelper.collides(enemy, bullet)) {
                    remove(bullet, false);
                    remove(enemy, false);
                    score.addScore(10);
                    collided = true;
                    break;
                }
            }

            // 플레이어 몬스터 충돌 시 플레이어 체력 깎기
            if (CollisionHelper.collides(enemy, player))
            {
                if(!player.getDefend()) {
                    remove(enemy, false);
                    //score.addScore(10);
                    collided = true;
                    player.iLife -= 1;
                    if (player.iLife < 0) {
                        Sound.play(R.raw.die);
                        GameView.view.pauseGame();
                    }
                    hp.subScore(1);
                    Log.d(TAG, "플레이어 체력: " + player.iLife);
                }

                //Effect ef = new Effect();
                //add(Layer.effect, ef);
            }

            if (collided) {
                break;
            }
        }

//        for (GameObject o1 : objects) {
//            if (!(o1 instanceof Enemy)) {
//                continue;
//            }
//            Enemy enemy = (Enemy) o1;
//            boolean removed = false;
//            for (GameObject o2 : objects) {
//                if (!(o2 instanceof Bullet)) {
//                    continue;
//                }
//                Bullet bullet = (Bullet) o2;
//
//                if (CollisionHelper.collides(enemy, bullet)) {
//                    //Log.d(TAG, "Collision!" + o1 + " - " + o2);
//                    remove(enemy);
//                    remove(bullet);
//                    //bullet.recycle();
//                    //recycle(bullet);
//                    removed = true;
//                    break;
//                }
//            }
//            if (removed) {
//                continue;
//            }
//            if (CollisionHelper.collides(enemy, player)) {
//                Log.d(TAG, "Collision: Enemy - Player");
//            }
//        }
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
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            RectF rect = new RectF(event.getX(), event.getY(), event.getX() + 10, event.getY() + 10);
            RectF skillRect = new RectF(player.getPlayerX(), 0, player.getPlayerX() + 100, GameView.view.getHeight());

            if (CollisionHelper.collides_b(bAttack, rect)) {
                ArrayList<GameObject> enemies = layers.get(Layer.enemy.ordinal());
                for (GameObject o1 : enemies) {
                    Enemy enemy = (Enemy) o1;
                    if (CollisionHelper.collides_b(enemy, skillRect))
                    {
                        remove(enemy, false);
                        score.addScore(10);
                        break;
                    }
                }

            }
            else if (CollisionHelper.collides_b(bHide, rect))
            {
                player.setDefend(true);
            }
            else {
                player.moveTo(event.getX(), event.getY());
            }
            return true;

        }

        if (action == MotionEvent.ACTION_BUTTON_PRESS){

        }

        return false;
    }

    public void add(Layer layer, GameObject gameObject) {
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

    public int getScore()
    {
        return score.getScore();
    }

    public void addatScore(int _a)
    {
        score.addScore(_a);
    }


    public void resetGame()
    {
        score.setScore(0);
        player.iLife = 3;
        hp.addScore(player.iLife );

    }

}
