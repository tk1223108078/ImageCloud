package site.taokai.imagecloud.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import site.taokai.imagecloud.R;

public class LauchActivity extends AppCompatActivity {
    private static final int LOAD_DISPLAY_TIME = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lauch);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                /* Create an Intent that will start the Main WordPress Activity. */
                Intent mainIntent = new Intent(LauchActivity.this, MainActivity.class);
                LauchActivity.this.startActivity(mainIntent);
                LauchActivity.this.finish();
            }
        }, LOAD_DISPLAY_TIME);
    }
}
