package ua.kpi.comsys.IV8209;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class GalleryActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("ResourceType")

    private ImageView imageViewWork;
    ArrayList<ImageView> images = new ArrayList<ImageView>();
    ArrayList<String> URIs = new ArrayList<String>();
    ConstraintLayout constraintLayout;
    ConstraintSet set = new ConstraintSet();

    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        String[] URIsArray = new String[URIs.size()];
        for (int i = 0; i < URIs.size(); i++){
            URIsArray[i] = URIs.get(i);
        }
        outState.putStringArray("URIs", URIsArray);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.getTabAt(3).select();
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    Intent intent = new Intent(GalleryActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (tab.getPosition() == 1) {
                    Intent intent = new Intent(GalleryActivity.this, DrawingActivity.class);
                    startActivity(intent);
                } else if (tab.getPosition() == 2) {
                    Intent intent = new Intent(GalleryActivity.this, ListActivity.class);
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

        constraintLayout = findViewById(R.id.constraint);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        if (savedInstanceState != null){
            String[] URIsList = savedInstanceState.getStringArray("URIs");
            URIs.addAll(Arrays.asList(URIsList));
            drawFromArray(URIsList);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED && requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                URIs.add(selectedImage.toString());
                drawFromUri(selectedImage);
            }
        }
    }

    private void drawFromArray(String[] URIs){
        for (String i : URIs){
            Uri uri = Uri.parse(i);
            drawFromUri(uri);
        }
    }

    private void drawFromUri(Uri selectedImage){
        images.add(new ImageView(this));
        int size = images.size();
        ImageView image = images.get(size - 1);
        image.setImageURI(selectedImage);
        image.setId(View.generateViewId());
        if (size % 8 == 1){
            constraintLayout.addView(image, getDisplay().getWidth() / 4, getDisplay().getWidth() / 4);
            set.clone(constraintLayout);
            if (size > 8){
                set.connect(image.getId(), ConstraintSet.TOP, images.get(size - 2).getId(), ConstraintSet.BOTTOM);}
        }
        if (size % 8 == 2){
            constraintLayout.addView(image, getDisplay().getWidth() * 3 / 4, getDisplay().getWidth() * 3 / 4);
            set.clone(constraintLayout);
            set.connect(image.getId(), ConstraintSet.LEFT, images.get(size - 2).getId(), ConstraintSet.RIGHT);
            set.connect(image.getId(), ConstraintSet.TOP, images.get(size - 2).getId(), ConstraintSet.TOP);
        }
        if (size % 8 == 3){
            constraintLayout.addView(image, getDisplay().getWidth() / 4, getDisplay().getWidth() / 4);
            set.clone(constraintLayout);
            set.connect(image.getId(), ConstraintSet.TOP, images.get(size - 3).getId(), ConstraintSet.BOTTOM);
        }
        if (size % 8 == 4 || size % 8 == 5){
            constraintLayout.addView(image, getDisplay().getWidth() / 4, getDisplay().getWidth() / 4);
            set.clone(constraintLayout);
            set.connect(image.getId(), ConstraintSet.TOP, images.get(size - 2).getId(), ConstraintSet.BOTTOM);
        }
        if (size % 8 == 0 || size % 8 >= 6){
            constraintLayout.addView(image, getDisplay().getWidth() / 4, getDisplay().getWidth() / 4);
            set.clone(constraintLayout);
            set.connect(image.getId(), ConstraintSet.TOP, images.get(size - 2).getId(), ConstraintSet.TOP);
            set.connect(image.getId(), ConstraintSet.LEFT, images.get(size - 2).getId(), ConstraintSet.RIGHT);
        }
        set.applyTo(constraintLayout);
    }
}
