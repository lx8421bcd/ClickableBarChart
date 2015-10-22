package com.shawnlin.clickablehistogram.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shawnlin.clickablehistogram.ClickableHistogram;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ClickableHistogram clickableHistogram = (ClickableHistogram) findViewById(R.id.clickableHistogram);
        ArrayList<ClickableHistogram.ColumnData> dataSource = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ClickableHistogram.ColumnData data = clickableHistogram.new ColumnData();
            data.setName("2015-9-1" + i);
            data.setValue(i + 20);
            dataSource.add(data);
        }
        clickableHistogram.setDataSource(dataSource);
    }
}
