package com.sail.dirreader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListChapterActivity extends AppCompatActivity {

    RecyclerView listChapterView;
    ChapterListAdapter chapterListAdapter;
    Context context;

    Integer chapterNumber;
    String bookTitle;
    String nameChapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapter_list);

        Intent intent = getIntent();
//        Integer bookPosition = intent.getIntExtra("bookPosition", 1);
        bookTitle = intent.getStringExtra("bookName");

//        Log.e("bookNamei ", bookTitle);


        getChapters(bookTitle);

    }

    public void getChapters(String mBookTitle) {
        context = ListChapterActivity.this;
        listChapterView = findViewById(R.id.chapter_list_view);
        ArrayList<ChapterModel> allChapters = getChapterList(context, mBookTitle);

        chapterListAdapter = new ChapterListAdapter(context, allChapters);
        listChapterView.setLayoutManager(new LinearLayoutManager(context));
        listChapterView.setAdapter(chapterListAdapter);

    }

    public ArrayList<ChapterModel> getChapterList(final Context context, String mBookTitle) {

        final ArrayList<ChapterModel> tempChapterList = new ArrayList<>();

        String path = Environment.getExternalStorageDirectory().toString() + "/AudioBooks/" + mBookTitle;

        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: " + files.length);

        for (int i = 0; i < files.length; i++) {

            nameChapter = files[i].getName();
//            Log.d("Files", "FileName:" + files[i].getName());
            ChapterModel chapterModel = new ChapterModel();

            chapterModel.setaChapter(nameChapter);

            tempChapterList.add(chapterModel);
        }

        return tempChapterList;
    }

}
