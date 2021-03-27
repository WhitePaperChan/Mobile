package ua.kpi.comsys.IV8209;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
        //table.setColumnShrinkable(0, true);
        //table.setColumnShrinkable(1, true);
        //table.setStretchAllColumns(true);
        //table.setShrinkAllColumns(true);
        TableRow[] tableRows = new TableRow[books.length];
        TextView[] bookTitles = new TextView[books.length];
        TextView[] bookSubtitles = new TextView[books.length];
        for (int i = 0; i < books.length; i++){
            tableRows[i] = new TableRow(this);
            tableRows[i].setPadding(10, 10, 10, 10);
            bookTitles[i] = new TextView(this);
            bookTitles[i].setText(books[i].getTitle());
            bookTitles[i].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            bookSubtitles[i] = new TextView(this);
            bookSubtitles[i].setText(books[i].getSubtitle());
            tableRows[i].addView(bookTitles[i]);
            tableRows[i].addView(bookSubtitles[i]);
            table.addView(tableRows[i]);
        }
        //table.setShrinkAllColumns(true);
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
}