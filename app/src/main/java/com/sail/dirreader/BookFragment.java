package com.sail.dirreader;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.ArrayList;

public class BookFragment extends Fragment {

    ArrayList listBooks = new ArrayList();
    String nameBook;

    public BookFragment() {

    }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_main, container, false);

            final ArrayList<String> books = new ArrayList<>();

            String path = Environment.getExternalStorageDirectory().toString() + "/AudioBooks";
            File directory = new File(path);
            File[] files = directory.listFiles();

            for (int i = 0; i < files.length; i++) {
                Log.d("Files", "FileName:" + files[i].getName());
                nameBook = files[i].getName();

                books.add(nameBook);
            }

            return rootView;
        }

}
