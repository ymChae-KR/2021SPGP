package kr.kpu.ac.kr.game.s2015180040.spgp_termproject.UI;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import kr.kpu.ac.kr.game.s2015180040.spgp_termproject.R;
import kr.kpu.ac.kr.game.s2015180040.spgp_termproject.UI.GameView;

import androidx.appcompat.app.AppCompatActivity;

public class mainActivity extends AppCompatActivity {

    private static final String TAG = mainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        GameView.MULTIPLIER = metrics.density * 0.8f;
        Log.d(TAG, "Density: " + metrics.density + " DPI:" + metrics.densityDpi + " Multiplier:" + GameView.MULTIPLIER);
    }

    @Override
    protected void onPause() {
        GameView.view.pauseGame();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GameView.view.resumeGame();
    }
}
