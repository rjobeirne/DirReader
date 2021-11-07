package com.sail.dirreader;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class ListBookActivity extends AppCompatActivity {

    RecyclerView listBookView;
    BookListAdapter bookListAdapter;
    Context context;

//    ArrayList listBooks = new ArrayList();
    String nameBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);


        //Checks permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                return;
            }
        }

        // Find all books in the /AudioBook directory
        getBooks();

    }


    public void getBooks() {
        String path = Environment.getExternalStorageDirectory().toString() + "/AudioBooks";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: " + files.length);

        ArrayList listBooks = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            Log.d("Files", "FileName:" + files[i].getName());
            nameBook = files[i].getName();

            listBooks.add(nameBook);
        }
        Log.e("List", String.valueOf(listBooks));
    }



}
