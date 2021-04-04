package ua.kpi.comsys.IV8209;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;

public class BookInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        String id = getIntent().getStringExtra("id");

        ImageView imageView = findViewById(R.id.imageView);
        TextView title = findViewById(R.id.booktitle);
        TextView subtitle = findViewById(R.id.subtitle);
        TextView desc = findViewById(R.id.description);
        TextView authors = findViewById(R.id.authors);
        TextView publisher = findViewById(R.id.publisher);
        TextView isbn13 = findViewById(R.id.isbn13);
        TextView pages = findViewById(R.id.pages);
        TextView year = findViewById(R.id.year);
        TextView rating = findViewById(R.id.rating);
        TextView price = findViewById(R.id.price);

        try {
            InputStream inputStream = getAssets().open(id + ".txt");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            String str = new String(buffer);
            try {
                JSONObject bookJson = new JSONObject(str);
                Book book = new Book(
                        bookJson.getString("title"), bookJson.getString("subtitle"),
                        bookJson.getString("authors"),
                        bookJson.getString("publisher"), bookJson.getString("isbn13"),
                        bookJson.getInt("pages"), bookJson.getInt("year"),
                        bookJson.getInt("rating"), bookJson.getString("desc"),
                        bookJson.getString("price"), bookJson.getString("image"));

                title.setText("Title: " + book.getTitle());
                subtitle.setText("Subtitle: " + book.getSubtitle());
                authors.setText("Authors: " + book.getAuthors());
                publisher.setText("Publisher: " + book.getPublisher());
                isbn13.setText("ISBN-13: " + book.getIsbn13());
                pages.setText("Pages: " + book.getPages());
                year.setText("Year: " + book.getYear());
                rating.setText("Rating: " + book.getRating() + "/5");
                desc.setText("Description: " + book.getDesc());
                price.setText("Price: " + book.getPrice());

                inputStream = getAssets().open(book.getImage());
                Drawable drawable = Drawable.createFromStream(inputStream, null);
                imageView.setImageDrawable(drawable);
                //imageView.set

            } catch (JSONException e) {
                //e.printStackTrace();
            }
        } catch (IOException e) {
            //
        }
        //isbn13.setText("ISBN-13: " + id);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(BookInfoActivity.this, ListActivity.class);
        startActivity(intent);
    }
}