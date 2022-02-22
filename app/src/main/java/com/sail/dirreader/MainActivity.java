package com.sail.dirreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView audioListView;
    BookListAdapter bookListAdapter;
    Context context;

    ListBookActivity theList;
    
    // Put lots of comments here

    // UI widgets
    private ImageButton listBooksBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button listBooksBtn = findViewById(R.id.list_books);
        Button continueListeningBtn = findViewById(R.id.continue_listen);

        listBooksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ListBookActivity.class));
            }
        });

    }



//    public void getBooks() {
//        String path = Environment.getExternalStorageDirectory().toString() + "/AudioBooks";
//        Log.d("Files", "Path: " + path);
//        File directory = new File(path);
//        File[] files = directory.listFiles();
//        Log.d("Files", "Size: " + files.length);
//
//        listBooks = new ArrayList<>();
//
//        for (int i = 0; i < files.length; i++) {
//            Log.d("Files", "FileName:" + files[i].getName());
//            nameBook = files[i].getName();
//
//            listBooks.add(nameBook);
//        }
//        Log.e("List", String.valueOf(listBooks));
//    }


}