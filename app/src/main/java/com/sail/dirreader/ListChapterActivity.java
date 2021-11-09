package com.sail.dirreader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListChapterActivity extends AppCompatActivity {

    RecyclerView listChapterView;
    ChapterListAdapter chapterListAdapter;
    Context context;

    Integer chapterNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapter_list);

        Intent intent = getIntent();
        Integer bookPosition = intent.getIntExtra("bookPosition", 1);

        Log.e("bookPositioni ", String.valueOf(bookPosition));


//        getChapters();

    }

    public void getChapters(String mBookTitle) {
        context = ListChapterActivity.this;
        listChapterView = findViewById(R.id.chapter_list_view);
        List<ChapterModel> allChapters = getChapterList(context, mBookTitle);

        chapterListAdapter = new ChapterListAdapter(context, allChapters);
        listChapterView.setLayoutManager(new LinearLayoutManager(context));
        listChapterView.setAdapter(chapterListAdapter);

    }

    public List<ChapterModel> getChapterList(final Context context, String mBookTitle) {

        final List<ChapterModel> tempChapterList = new ArrayList<>();


        String path = Environment.getExternalStorageDirectory().toString() + "/AudioBooks/" + mBookTitle;


        return tempChapterList;
    }

}
