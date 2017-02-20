package com.example.MiCurve.other;

import android.app.Activity;
import android.os.Bundle;
import com.example.MiCurve.R;

/**
 * Created by WYJ on 2016/11/16.
 */
public class Main2Activity extends Activity {

    PoCurveView poCurveView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);

        poCurveView = (PoCurveView) findViewById(R.id.voicLine);


    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
