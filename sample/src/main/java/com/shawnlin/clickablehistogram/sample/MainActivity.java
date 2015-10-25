package com.shawnlin.clickablehistogram.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.shawnlin.clickablehistogram.ClickableHistogram;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ClickableHistogram clickableHistogram = (ClickableHistogram) findViewById(R.id.clickableHistogram);
        final ArrayList<ClickableHistogram.ColumnData> dataSource = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ClickableHistogram.ColumnData data = clickableHistogram.new ColumnData();
            data.setName("2015-9-1" + i);
            data.setValue(i + 20);
            dataSource.add(data);
        }
        clickableHistogram.setDataSource(dataSource);
        clickableHistogram.setColumnOnClickListener(new ClickableHistogram.OnColumnClickListener() {
            @Override
            public void onColumnClick(View v, int position, ClickableHistogram.ColumnData data) {
                Toast.makeText(MainActivity.this, position + "  " + data.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        Button btnReload = (Button) findViewById(R.id.btnReload);
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickableHistogram.setDataSource(dataSource);
            }
        });
    }
}
