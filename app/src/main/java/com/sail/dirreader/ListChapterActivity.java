package com.sail.dirreader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
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

    String bookTitle, coverPath;
    String nameChapter;
    long dur, secs, mins;
    String seconds, minutes;

    TextView mBookTitleTextView;
    View mCoverView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapter_list);

        Intent intent = getIntent();

        bookTitle = intent.getStringExtra("bookName");
        coverPath = intent.getStringExtra("coverPath");
        mBookTitleTextView = findViewById(R.id.book_name);
        mCoverView = findViewById(R.id.cover_background);
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

        File directory = new File(dirPath);
        File[] files = directory.listFiles();

        for (int i = 0; i < files.length; i++) {

            nameChapter = files[i].getName();
            String duration;
            String out;
            String chapterPath = dirPath + "/" + nameChapter;
            ChapterModel chapterModel = new ChapterModel();
            // load data file
            MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();

            if(nameChapter.endsWith(".mp3")) {

                metaRetriever.setDataSource(chapterPath);

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

                chapterModel.setaTrackNumber(i);
                chapterModel.setaChapter(nameChapter);
                chapterModel.setaDuration(out);
                chapterModel.setaRawDuration(dur);
                chapterModel.setaPath(chapterPath);
                chapterModel.setaCover(coverPath);
                tempChapterList.add(chapterModel);

                // close object
                metaRetriever.release();
            }
        }
        makeCover(coverPath);
        return tempChapterList;
    }

    public void makeCover(String coverPath) {

        Bitmap bitmap = BitmapFactory.decodeFile(coverPath);

        BitmapDrawable coverBMP = new BitmapDrawable(bitmap);

//        BitmapDrawable coverBMP = theCover.makeCover(coverPath);
        mCoverView.setBackground(coverBMP);
    }
}
