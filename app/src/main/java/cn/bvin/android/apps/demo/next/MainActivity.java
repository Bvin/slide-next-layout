package cn.bvin.android.apps.demo.next;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

import cn.bvin.android.lib.widget.next.SlideNextLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SlideNextLayout snl = (SlideNextLayout) findViewById(R.id.snl);
        /*snl.setOnSlideCallback(new SlideNextLayout.OnSlideCallback() {
            @Override
            public boolean onSlideToNext(MotionEvent ev) {
                return false;
            }

            @Override
            public boolean onSlideToPrevious(MotionEvent ev) {
                return false;
            }
        });*/
    }
}
