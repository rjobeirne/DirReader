package com.sail.dirreader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookViewHolder> {

    private ArrayList<BookModel> bookDataSet;
    private LayoutInflater mInflater;
    Context mContext;
    public String mBookTitle, mCoverPath;



    public BookListAdapter(Context context, ArrayList<BookModel> bookModelList) {

        mInflater = LayoutInflater.from(context);
        mContext = context;
        bookDataSet = bookModelList;
    }

    @NonNull
    @Override

    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Create a new view
        View v = mInflater.inflate(R.layout.book_item, viewGroup,false);

        BookViewHolder vh = new BookViewHolder(mContext, bookDataSet,v);
        return  vh;
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder bookViewHolder, int i) {

        mBookTitle = bookDataSet.get(i).getaTitle();
        mCoverPath = bookDataSet.get(i).getaCover();

        BitmapDrawable cover = makeCover(mCoverPath);

        bookViewHolder.mTextView.setText(mBookTitle);
        bookViewHolder.mCoverView.setImageDrawable(cover);
    }

    @Override
    public int getItemCount() {
        return bookDataSet.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context nContext;
        public ArrayList<BookModel> bookList;
        public TextView mTextView;
        public ImageView mCoverView;

        public BookViewHolder(Context context, ArrayList<BookModel> bookModelList, View v) {
            super(v);
            nContext = context;
            bookList = bookModelList;
            mTextView = v.findViewById(R.id.book_name);
            mCoverView = v.findViewById(R.id.cover_image);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // opening a new intent for chapter activity

            int itemPosition = getAdapterPosition();

            String bookName = bookList.get(itemPosition).getaTitle();
            String bookCover = bookList.get(itemPosition).getaCover();
//            Log.e("book title", bookName);

            Intent intent = new Intent(nContext, ListChapterActivity.class);
            intent.putExtra("bookName", bookName);
            intent.putExtra("coverPath", bookCover);

            nContext.startActivity(intent);

        }
    }

    /**
     * Function to add cover to player screen
     * @param coverPath
     */
    public BitmapDrawable makeCover(String coverPath) {

//        View mCoverView = mCoverView.findViewById();
        Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(coverPath));
        BitmapDrawable coverBMP = new BitmapDrawable(bitmap);
//        mCoverView.setBackground(coverBMP);
        return coverBMP;
    }


}



