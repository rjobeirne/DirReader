package com.sail.dirreader;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BookListAdapter extends ArrayAdapter<Book> {

    public BookListAdapter(Activity context, ArrayList<Book> books) {

        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listBookView = convertView;
        if (listBookView == null) {
            listBookView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_item, parent, false);
        }

        Book currentBook = getItem(position);

        TextView nameBookTextView = listBookView.findViewById(R.id.book_name);
        nameBookTextView.setText(currentBook.getBook());

        return listBookView;

    }
}
