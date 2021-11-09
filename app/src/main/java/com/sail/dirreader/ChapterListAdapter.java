package com.sail.dirreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChapterListAdapter extends RecyclerView.Adapter<ChapterListAdapter.ChapterViewHolder> {

    private List<ChapterModel> chapterDataSet;
    private LayoutInflater mInflater;
    Context mContext;
    public String mBookTitle;
    public Integer mChapterNumber;

    public ChapterListAdapter(Context context, List<ChapterModel> chapterModelList) {

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

        mChapterNumber = chapterDataSet.get(i).getaChapter();
        chapterViewHolder.mTextView.setText(mChapterNumber);
    }

    @Override
    public int getItemCount() {
        return chapterDataSet.size();
    }

    public static class ChapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context nContext;
        List<ChapterModel> chapterList;
        public TextView mTextView;

        public ChapterViewHolder(Context context, List<ChapterModel> chapterModelList, View v) {
            super(v);
            nContext = context;
            chapterList = chapterModelList;
            mTextView = v.findViewById(R.id.book_name);

        }

        @Override
        public void onClick(View v) {

        }
    }


}



