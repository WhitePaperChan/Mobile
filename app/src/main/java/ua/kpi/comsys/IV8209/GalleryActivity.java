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

import java.io.IOException;
import java.io.InputStream;

public class GalleryActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("ResourceType")

    private ImageView imageViewWork;
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


        TableLayout table = findViewById(R.id.table);
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        ));

        ConstraintLayout constraintLayout = new ConstraintLayout(this);
        constraintLayout.setId(View.generateViewId());
        ConstraintSet set = new ConstraintSet();

        ImageView imageView = new ImageView(this); //TODO: show image
        createImage(imageView, "Image_01.png");
        constraintLayout.addView(imageView, getDisplay().getWidth() / 4 * 3, getDisplay().getWidth() / 4 * 3);
        set.clone(constraintLayout);
        set.connect(imageView.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT);
        set.applyTo(constraintLayout);

        ImageView imageView1 = new ImageView(this); //TODO: show image
        createImage(imageView1, "Image_02.png");
        constraintLayout.addView(imageView1, getDisplay().getWidth() / 4, getDisplay().getWidth() / 4);
        set.clone(constraintLayout);
        set.connect(imageView1.getId(), ConstraintSet.RIGHT, imageView.getId(), ConstraintSet.LEFT);
        set.applyTo(constraintLayout);

        ImageView imageView2 = new ImageView(this); //TODO: show image
        createImage(imageView2, "Image_03.png");
        constraintLayout.addView(imageView2, getDisplay().getWidth() / 4, getDisplay().getWidth() / 4);
        set.clone(constraintLayout);
        set.connect(imageView2.getId(), ConstraintSet.RIGHT, imageView.getId(), ConstraintSet.LEFT);
        set.connect(imageView2.getId(), ConstraintSet.TOP, imageView1.getId(), ConstraintSet.BOTTOM);
        set.applyTo(constraintLayout);

        ImageView imageView3 = new ImageView(this);
        createImage(imageView3, "Image_05.png");
        constraintLayout.addView(imageView3, getDisplay().getWidth() / 4, getDisplay().getWidth() / 4);
        set.clone(constraintLayout);
        set.connect(imageView3.getId(), ConstraintSet.RIGHT, imageView.getId(), ConstraintSet.LEFT);
        set.connect(imageView3.getId(), ConstraintSet.TOP, imageView2.getId(), ConstraintSet.BOTTOM);
        set.applyTo(constraintLayout);

        ImageView imageView4 = new ImageView(this);
        createImage(imageView4, "Image_06.png");
        constraintLayout.addView(imageView4, getDisplay().getWidth() / 4, getDisplay().getWidth() / 4);
        set.clone(constraintLayout);
        set.connect(imageView4.getId(), ConstraintSet.TOP, imageView.getId(), ConstraintSet.BOTTOM);
        set.applyTo(constraintLayout);

        ImageView imageView5 = new ImageView(this);
        createImage(imageView5, "Image_07.png");
        constraintLayout.addView(imageView5, getDisplay().getWidth() / 4, getDisplay().getWidth() / 4);
        set.clone(constraintLayout);
        set.connect(imageView5.getId(), ConstraintSet.LEFT, imageView4.getId(), ConstraintSet.RIGHT);
        set.connect(imageView5.getId(), ConstraintSet.TOP, imageView.getId(), ConstraintSet.BOTTOM);
        set.applyTo(constraintLayout);

        ImageView imageView6 = new ImageView(this);
        createImage(imageView6, "Image_08.png");
        constraintLayout.addView(imageView6, getDisplay().getWidth() / 4, getDisplay().getWidth() / 4);
        set.clone(constraintLayout);
        set.connect(imageView6.getId(), ConstraintSet.LEFT, imageView5.getId(), ConstraintSet.RIGHT);
        set.connect(imageView6.getId(), ConstraintSet.TOP, imageView.getId(), ConstraintSet.BOTTOM);
        set.applyTo(constraintLayout);

        ImageView imageView7 = new ImageView(this);
        createImage(imageView7, "Image_10.png");
        constraintLayout.addView(imageView7, getDisplay().getWidth() / 4, getDisplay().getWidth() / 4);
        set.clone(constraintLayout);
        set.connect(imageView7.getId(), ConstraintSet.LEFT, imageView6.getId(), ConstraintSet.RIGHT);
        set.connect(imageView7.getId(), ConstraintSet.TOP, imageView.getId(), ConstraintSet.BOTTOM);
        set.applyTo(constraintLayout);

        tableRow.addView(constraintLayout);

        table.addView(tableRow);

        imageViewWork = imageView;

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED && requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                imageViewWork.setImageURI(selectedImage);
            }
        }
    }

    void createImage(ImageView imageView, String filename){
        try {
            InputStream inputStream = getAssets().open(filename);
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            imageView.setImageDrawable(drawable);
        } catch (IOException e) {
        }
        imageView.setContentDescription("sample");
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));
        imageView.setId(View.generateViewId());
    }
}
