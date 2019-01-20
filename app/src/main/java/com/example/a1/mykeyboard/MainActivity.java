package com.example.a1.mykeyboard;

import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Canvas c = new Canvas();
        setContentView(R.layout.activity_main);

        Test.UnDockItem item= Test.getAA(Test.UnDockItem.SWITCH_ITEM.matchCode());
        if(item!=null){
            item.doUpdate();
        }
    }
}
