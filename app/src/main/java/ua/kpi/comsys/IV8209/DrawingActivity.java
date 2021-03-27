package ua.kpi.comsys.IV8209;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class DrawingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);
        TabLayout tabs = findViewById(R.id.tabs);
        TabLayout tabsDrawing = findViewById(R.id.tabsDrawing);
        DrawingView drawingView = findViewById(R.id.drawing);
        tabs.getTabAt(1).select();
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    Intent intent = new Intent(DrawingActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (tab.getPosition() == 2) {
                    Intent intent = new Intent(DrawingActivity.this, ListActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabsDrawing.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    drawingView.setNumberOfDrawing(0);
                    drawingView.invalidate();
                } else {
                    drawingView.setNumberOfDrawing(1);
                    drawingView.invalidate();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}