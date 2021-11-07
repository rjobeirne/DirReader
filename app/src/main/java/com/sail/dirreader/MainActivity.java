package com.sail.dirreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView audioListView;
    Context context;

    ArrayList listBooks = new ArrayList();
    String nameBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getBooks();

        context = MainActivity.this;
//        audioListView = findViewById(R.id.book_listview);

//        View rootView = inflater.inflate(R.layout.book_item, container, false);


    }

    public void getBooks() {
        String path = Environment.getExternalStorageDirectory().toString() + "/AudioBooks";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: " + files.length);

        listBooks = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            Log.d("Files", "FileName:" + files[i].getName());
            nameBook = files[i].getName();

            listBooks.add(nameBook);
        }
        Log.e("List", String.valueOf(listBooks));
    }
}