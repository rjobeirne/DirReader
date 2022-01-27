package com.sail.dirreader;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListBookActivity extends AppCompatActivity {

    RecyclerView listBookView;
    BookListAdapter bookListAdapter;
    Context context;
    String nameFile, coverPath, author;

    String nameBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);

        ImageButton goBack = findViewById(R.id.go_back_button);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Find all books in the /AudioBook directory
        getBooks();
    }

    public void getBooks() {
        context = ListBookActivity.this;
        listBookView = findViewById(R.id.book_list_view);
        ArrayList<BookModel> allBookDirectories = getBookList(context);

        bookListAdapter = new BookListAdapter(context, allBookDirectories);
        listBookView.setLayoutManager(new LinearLayoutManager(context));
        listBookView.setAdapter(bookListAdapter);
    }

    public ArrayList<BookModel> getBookList(final Context context) {

        final ArrayList<BookModel> tempBookList = new ArrayList<>();

        String path = Environment.getExternalStorageDirectory().toString() + "/AudioBooks";

        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] books = directory.listFiles();
        Log.d("Files", "Size: " + books.length);

        // load data file
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();

        for (int i = 0; i < books.length; i++) {
            nameBook = books[i].getName();

            String intPath = path + "/" + nameBook;
            File intDir = new File(intPath);
            File[] intFiles = intDir.listFiles();

            coverPath = null;

            for (int j = 0; j < intFiles.length; j++) {
                nameFile = intFiles[j].getName();
                if (nameFile.endsWith(".jpg")) {
                    coverPath = intDir + "/" + nameFile;
                }
            }

            for (int k = 0; k <books.length; k++) {
                String audFile = intFiles[k].getName();
                if (audFile.endsWith(".mp3")) {
                    String filePath = intDir + "/" + audFile;
                    metaRetriever.setDataSource(filePath);
                    author = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                    if (author == null) {
                        author = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
                    }
                    if (author == null) {
                        author = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR);
                    }
                    break;
                }
            }

            if(books[i].isDirectory()) {
                BookModel bookModel = new BookModel();

                bookModel.setaTitle(nameBook);
                bookModel.setaCover(coverPath);
                bookModel.setaAuthor(author);

                tempBookList.add(bookModel);

                // Sort the library alphabetically
                Collections.sort(tempBookList, (obj1, obj2) -> obj1.getaTitle().compareToIgnoreCase(obj2.getaTitle()));
            }
        }


        return tempBookList;
    }

}
