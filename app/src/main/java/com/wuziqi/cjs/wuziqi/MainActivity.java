package com.wuziqi.cjs.wuziqi;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    private Button btn_restare;
    private Wuziqipanel wuziqipanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        btn_restare= (Button) findViewById(R.id.restare);
        wuziqipanel=(Wuziqipanel)findViewById(R.id.wuziqi);
        btn_restare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wuziqipanel.start();
            }
        });
    }

}
