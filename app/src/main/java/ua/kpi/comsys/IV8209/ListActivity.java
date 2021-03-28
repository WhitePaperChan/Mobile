package ua.kpi.comsys.IV8209;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import org.json.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.getTabAt(2).select();
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    Intent intent = new Intent(ListActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (tab.getPosition() == 1) {
                    Intent intent = new Intent(ListActivity.this, DrawingActivity.class);
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
        Book[] books = this.loadBookArray();
        TableLayout table = findViewById(R.id.table);
        TableRow[] tableRows = new TableRow[books.length];
        TextView[] bookInfo = new TextView[books.length];
        ImageView[] bookImages = new ImageView[books.length];
        ConstraintLayout.LayoutParams params;
        for (int i = 0; i < books.length; i++){
            tableRows[i] = new TableRow(this);
            tableRows[i].setPadding(10, 10, 10, 10);
            bookImages[i] = this.createBookImage(books[i]);
            tableRows[i].addView(bookImages[i]);
            bookInfo[i] = new TextView(this);
            bookInfo[i].setText(
                    books[i].getTitle() + "\n" + books[i].getSubtitle() + "\n" +
                            books[i].getIsbn13() + "\n" + books[i].getPrice()
            );
            bookInfo[i].setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));
            bookInfo[i].setPadding(10, 10, 10, 10);
            tableRows[i].addView(bookInfo[i]);
            table.addView(tableRows[i]);

        }
    }

    private Book[] loadBookArray() {
        String str = "";
        try {
            InputStream inputStream = getAssets().open("BooksList.txt");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);

            str = new String(buffer);
            System.out.println(str);
        } catch (IOException e){
            e.printStackTrace();
        }
        Book[] books = new Book[0];
        try {
            JSONObject booksJson = new JSONObject(str);
            JSONArray booksJsonArray = booksJson.getJSONArray("books");
            books = new Book[booksJsonArray.length()];
            JSONObject book;
            for (int i = 0; i < booksJsonArray.length(); i++) {
                book = booksJsonArray.getJSONObject(i);
                books[i] = new Book(
                        book.getString("title"),
                        book.getString("subtitle"),
                        book.getString("isbn13"),
                        book.getString("price"),
                        book.getString("image")
                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return books;
    }

    private ImageView createBookImage(Book book) {
        ImageView bookImage = new ImageView(this);
        try {
            InputStream inputStream = getAssets().open(book.getImage());
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            bookImage.setImageDrawable(drawable);
        } catch (IOException e) {
            //e.printStackTrace();
        }
        bookImage.setContentDescription(book.getTitle());
        bookImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        bookImage.setMinimumWidth(200);
        bookImage.setMinimumHeight(200);
        bookImage.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));
        return bookImage;
    }
}

