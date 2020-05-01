package com.hku.cs_cinema;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Thankyou extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);
        final Button btn_Exit = (Button)findViewById(R.id.exit);
         btn_Exit.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btn_Exit.setVisibility(View.VISIBLE);

            }
        }, 5000);
        btn_Exit.setOnClickListener(Thankyou.this);
    }
    public void onClick(View v){
        System.exit(0);
    }
}
