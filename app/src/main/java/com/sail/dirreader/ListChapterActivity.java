package com.sail.dirreader;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class ListChapterActivity extends AppCompatActivity {

    RecyclerView listChapterView;
    ChapterListAdapter chapterListAdapter;
    Context context;

    Integer chapterNumber;
    String bookTitle;
    String nameChapter;
    long dur, secs, mins;
    String seconds, minutes;

    TextView mBookTitleTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapter_list);

        Intent intent = getIntent();
//        Integer bookPosition = intent.getIntExtra("bookPosition", 1);
        bookTitle = intent.getStringExtra("bookName");

//        Log.e("bookNamei ", bookTitle);

        mBookTitleTextView = findViewById(R.id.book_name);
        mBookTitleTextView.setText(bookTitle);


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

        String dirPath = Environment.getExternalStorageDirectory().toString() + "/AudioBooks/" + mBookTitle;

        Log.d("Files", "Path: " + dirPath);
        File directory = new File(dirPath);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: " + files.length);

        for (int i = 0; i < files.length; i++) {

            nameChapter = files[i].getName();
            Log.e("  nameChapter", nameChapter);
            String duration;
            String out;

            String chapterNames = dirPath + "/" + nameChapter;
//            Log.e("*fileName",fileName);


            if(nameChapter.endsWith(".mp3")) {
//            Log.d("Files", "FileName:" + files[i].getName());

                // load data file
                MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
                String bookPath = dirPath + "/" + nameChapter;
                metaRetriever.setDataSource(bookPath);

                // get mp3 info
                duration = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                dur = Long.parseLong(duration);

                // convert duration to minute:seconds
                secs = (dur % 60000) / 1000;
                mins = dur / 60000;

                if ( secs < 10 ) {
                    seconds = "0" + String.valueOf(secs);
                } else {
                    seconds = String.valueOf(secs);
                }

                if ( mins < 10 ) {
                    minutes = "0" + String.valueOf(mins);
                } else {
                    minutes = String.valueOf(mins);
                }

                out = minutes + ":" + seconds;

                ChapterModel chapterModel = new ChapterModel();
                chapterModel.setaTrackNumber(i);
                chapterModel.setaChapter(nameChapter);
                chapterModel.setaDuration(out);
                chapterModel.setaRawDuration(dur);
                chapterModel.setaPath(dirPath);
                tempChapterList.add(chapterModel);

                // close object
                metaRetriever.release();
            }
        }
        return tempChapterList;
    }

}
