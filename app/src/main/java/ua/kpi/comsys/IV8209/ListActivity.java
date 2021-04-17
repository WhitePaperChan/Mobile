package ua.kpi.comsys.IV8209;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import org.json.*;
import java.io.IOException;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
                } else if (tab.getPosition() == 3) {
                    Intent intent = new Intent(ListActivity.this, GalleryActivity.class);
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
        ArrayList<Book> books = new ArrayList<Book>();
        Collections.addAll(books, this.loadBookArray());
        TableLayout table = findViewById(R.id.table);
        TableRow[] tableRows = new TableRow[books.size()];
        TextView[] bookInfo = new TextView[books.size()];
        ImageView[] bookImages = new ImageView[books.size()];
        ConstraintLayout.LayoutParams params;

        this.redrawTable(books, tableRows, bookImages, bookInfo, table, books);

        SearchView search = findViewById(R.id.search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Book> booksFiltered = new ArrayList<Book>();
                table.removeAllViews();
                int j = 0;
                if (!newText.isEmpty()) {
                    for (int i = 0; i < books.size(); i++) {
                        if (books.get(i).getTitle().contains(newText)) {
                            booksFiltered.add(books.get(i));
                            j++;
                        }
                    }
                    redrawTable(booksFiltered, tableRows, bookImages, bookInfo, table, books);
                } else {
                    redrawTable(books, tableRows, bookImages, bookInfo, table, books);
                }
                return false;
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, AddBookActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void redrawTable(
            ArrayList<Book> booksArrayFiltered, TableRow[] tableRows, ImageView[] bookImages, TextView[] bookInfo,
            TableLayout table, ArrayList<Book> booksArrayUnfiltered
    ){
        tableRows = new TableRow[booksArrayFiltered.size()];
        bookInfo = new TextView[booksArrayFiltered.size()];
        bookImages = new ImageView[booksArrayFiltered.size()];
        List<String> bookFiles = null;
        try {
            bookFiles = Arrays.asList(getAssets().list(""));
        } catch (IOException e){
            e.printStackTrace();
        }
        for (int i = 0; i < booksArrayFiltered.size(); i++) {
            tableRows[i] = new TableRow(this);
            tableRows[i].setPadding(10, 10, 10, 10);
            bookImages[i] = this.createBookImage(booksArrayFiltered.get(i));
            tableRows[i].addView(bookImages[i]);
            bookInfo[i] = new TextView(this);
            bookInfo[i].setText(
                    booksArrayFiltered.get(i).getTitle() + "\n" + booksArrayFiltered.get(i).getSubtitle() + "\n" +
                            booksArrayFiltered.get(i).getIsbn13() + "\n" + booksArrayFiltered.get(i).getPrice()
            );
            bookInfo[i].setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));
            bookInfo[i].setPadding(10, 10, 10, 10);
            tableRows[i].addView(bookInfo[i]);
            String isbn13 = booksArrayFiltered.get(i).getIsbn13();
            TableRow tableRow = tableRows[i];
            Book book = booksArrayFiltered.get(i);
            if (bookFiles.contains(isbn13 + ".txt")) {
                tableRows[i].setOnTouchListener(new OnSwipeTouchListener(ListActivity.this) {
                    @Override
                    public void onClick() {
                        Intent intent = new Intent(ListActivity.this, BookInfoActivity.class);
                        intent.putExtra("id", isbn13);
                        startActivity(intent);
                    }

                    @Override
                    public void onSwipeLeft() {
                        swipeLeftMethod(table, tableRow, booksArrayUnfiltered, booksArrayFiltered, book);
                    }
                });
            } else {
                tableRows[i].setOnTouchListener(new OnSwipeTouchListener(ListActivity.this) {
                    @Override
                    public void onSwipeLeft() {
                        swipeLeftMethod(table, tableRow, booksArrayUnfiltered, booksArrayFiltered, book);
                    }
                });
            }
        //}
        table.addView(tableRows[i]);
        }
        if (booksArrayFiltered.isEmpty()){
            TableRow tableRowNoBooks = new TableRow(this);
            TextView textViewNoBooks = new TextView(this);
            textViewNoBooks.setText("Книг не знайдено");
            tableRowNoBooks.addView(textViewNoBooks);
            table.addView(tableRowNoBooks);
        }
    }

    private Book[] loadBookArray() {
        String str = "";
        JSONObject booksJson = null;
        try {
            ObjectInputStream inputStream = new ObjectInputStream(openFileInput("BooksListUser.txt"));
            booksJson = new JSONObject((String) inputStream.readObject());
        }
        catch (IOException e) {
            try {
                InputStream inputStream = getAssets().open("BooksList.txt");
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read(buffer);

                str = new String(buffer);
                booksJson = new JSONObject(str);
                ObjectOutputStream stream = new ObjectOutputStream(openFileOutput("BooksListUser.txt", MODE_PRIVATE));
                stream.writeObject(booksJson.toString());
                stream.close();
            } catch (IOException | JSONException e2) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Book[] books = new Book[0];
        try {
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

    private void swipeLeftMethod(TableLayout table,
                                 TableRow tableRow,
                                 ArrayList<Book> booksArrayUnfiltered,
                                 ArrayList<Book> booksArrayFiltered,
                                 Book book){
        table.removeView(tableRow);
        booksArrayUnfiltered.remove(book);
        booksArrayFiltered.remove(book);
        JSONArray booksJsonArray = new JSONArray();
        for (int j = 0; j < booksArrayUnfiltered.size(); j++) {
            try {
                booksJsonArray.put(new JSONObject().
                        put("title", booksArrayUnfiltered.get(j).getTitle()).
                        put("subtitle", booksArrayUnfiltered.get(j).getSubtitle()).
                        put("isbn13", booksArrayUnfiltered.get(j).getIsbn13()).
                        put("price", booksArrayUnfiltered.get(j).getPrice()).
                        put("image", booksArrayUnfiltered.get(j).getImage()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            JSONObject booksJson = new JSONObject().put("books", booksJsonArray);
            ObjectOutputStream stream = new ObjectOutputStream(openFileOutput("BooksListUser.txt", MODE_PRIVATE));
            stream.writeObject(booksJson.toString());
            stream.close();
        } catch (JSONException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

