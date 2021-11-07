package com.sail.dirreader;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookViewHolder> {

    private List<BookModel> bookDataSet;
    private LayoutInflater mInflater;
    Context mContext;

    public BookListAdapter(Context context, List<BookModel> bookModelList) {

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
        bookViewHolder.mTextView.setText(bookDataSet.get(i).getaTitle());
    }

    @Override
    public int getItemCount() {
        return bookDataSet.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context nContext;
        List<BookModel> bookList;
        public TextView mTextView;

        public BookViewHolder(Context context, List<BookModel> bookModelList, View v) {
            super(v);
            nContext = context;
            bookList = bookModelList;
            mTextView = v.findViewById(R.id.book_name);

        }

        @Override
        public void onClick(View v) {

        }
    }


}



