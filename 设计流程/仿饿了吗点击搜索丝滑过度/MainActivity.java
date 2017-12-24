package com.example.jasonchen.testproject_1;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    Dialog mDialog;
    private TextView mSearchBGTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSearchBGTxt = (TextView) findViewById(R.id.tv_search_bg);
        mSearchBGTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                int location[] = new int[2];
                mSearchBGTxt.getLocationOnScreen(location);
                intent.putExtra("x",location[0]);
                intent.putExtra("y",location[1]);
                startActivity(intent);
                Logger.d("____XY");
                overridePendingTransition(0,0);
            }
        });
    }
}
