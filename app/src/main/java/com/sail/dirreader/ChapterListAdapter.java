package com.sail.dirreader;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChapterListAdapter extends RecyclerView.Adapter<ChapterListAdapter.ChapterViewHolder> {

    private ArrayList<ChapterModel> chapterDataSet;
    private LayoutInflater mInflater;
    Context mContext;
    public String mChapterName, mChapterDuration;

    public  int minTime = 15 * 60 * 1000;
    Integer trackNumber, startTrack;
    Long duration;
    public String path, bookTitle;
    public ArrayList<String> paths = new ArrayList<String>();
    public ArrayList<Long> durations = new ArrayList<Long>();
    public String playStatus;


    public ChapterListAdapter(Context context, ArrayList<ChapterModel> chapterModelList) {

        mInflater = LayoutInflater.from(context);
        mContext = context;
        chapterDataSet = chapterModelList;
    }

    @NonNull
    @Override

    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Create a new view
        View v = mInflater.inflate(R.layout.chapter_item, viewGroup,false);

        ChapterViewHolder vh = new ChapterViewHolder(mContext, chapterDataSet,v);
        return  vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder chapterViewHolder, int i) {

        mChapterName = chapterDataSet.get(i).getaChapter();
        mChapterDuration = chapterDataSet.get(i).getaDuration();
        trackNumber = chapterDataSet.get(i).getaTrackNumber();
        duration = chapterDataSet.get(i).getaRawDuration();
        path = chapterDataSet.get(i).getaPath();
        bookTitle = chapterDataSet.get(i).getaTitle();

        if(mChapterName.contains("-")){
            mChapterName = mChapterName.substring(0, mChapterName.indexOf("."));
            chapterViewHolder.mChapterNoTextView.setText(mChapterName);
        } else {
            chapterViewHolder.mChapterNoTextView.setText("Chapter " + (i + 1));
        }
        chapterViewHolder.mChapterDurationTextView.setText(mChapterDuration);

        paths.add(path);
        durations.add(duration);

    }

    @Override
    public int getItemCount() {
        return chapterDataSet.size();
    }

    public class ChapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context nContext;
        ArrayList<ChapterModel> chapterList;
        public TextView mChapterNoTextView, mChapterDurationTextView;
        Integer trackNumber;
        List playList;

        public ChapterViewHolder(Context context, ArrayList<ChapterModel> chapterModelList, View v) {
            super(v);
            nContext = context;
            chapterList = chapterModelList;
            mChapterNoTextView = v.findViewById(R.id.chapter_number);
            mChapterDurationTextView = v.findViewById(R.id.chapter_duration);


            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int itemPosition = getAdapterPosition();

            ArrayList<ChapterModel> playList;
//            playList = createPlayList(itemPosition);
            startTrack = itemPosition;
            playStatus = "Play";

            Log.e("itemPosition ", String.valueOf(itemPosition));

            Intent intent = new Intent(nContext, AudioPlayerActivity.class);
//            intent.putExtra("filepath", playList);
            intent.putExtra("bookTitle", bookTitle);
            intent.putExtra("position", itemPosition);
            intent.putStringArrayListExtra("paths", paths);
            intent.putExtra("durations", durations);
            intent.putExtra("playStatus", playStatus);
            nContext.startActivity(intent);
        }
    }

//    public ArrayList createPlayList(Integer startTrack) {
//
//        Long playTime = chapterDataSet.get(startTrack).getaRawDuration();
//        Integer playChapters = 1;
//        Integer lastTrack = startTrack;
//        ArrayList<Integer> playList = new ArrayList<Integer>();
//        ArrayList<String> filePaths = new ArrayList<String>();
//
//        playList.add(startTrack);
//        filePaths.add(chapterDataSet.get(startTrack).getaPath());
//
//        while(playTime < minTime) {
//            lastTrack = lastTrack + 1;
//            if(lastTrack < chapterDataSet.size()) {
//                playTime = playTime + chapterDataSet.get(lastTrack).getaRawDuration();
//                playChapters = playChapters + 1;
//                filePaths.add(chapterDataSet.get(lastTrack).getaPath());
//            } else {
//                break;
//            }
//        }
//
//            Log.e("playList1 :", String.valueOf(filePaths));
//
//        return filePaths;
//    }

}



