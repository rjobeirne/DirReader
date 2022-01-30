package com.sail.dirreader;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;

public class ListBookActivity extends AppCompatActivity {

    RecyclerView listBookView;
    BookListAdapter bookListAdapter;
    Context context;
    String nameBookDirectory, coverPath, author;

    String bookDirectory, bookTitle;

    ArrayList<BookModel> allBookDirectories;
    BookProgress updateProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);

        updateProgress = new BookProgress();

        ImageButton goBack = findViewById(R.id.go_back_button);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // Find all books in the /AudioBook directory
        getBooks();
    }

    public void getBooks() {
        context = ListBookActivity.this;
        listBookView = findViewById(R.id.book_list_view);
        allBookDirectories = getBookList(context);

        bookListAdapter = new BookListAdapter(context, allBookDirectories);
        listBookView.setLayoutManager(new LinearLayoutManager(context));
        listBookView.setAdapter(bookListAdapter);
    }

    public ArrayList<BookModel> getBookList(final Context context) {

        final ArrayList<BookModel> tempBookList = new ArrayList<>();

        String path = Environment.getExternalStorageDirectory().toString() + "/AudioBooks";

        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] books = directory.listFiles();
        Log.d("Files", "Size: " + books.length);

        // load data file
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();

        for (int i = 0; i < books.length; i++) {
            bookDirectory = books[i].getName();

                String intPath = path + "/" + bookDirectory;
                File intDir = new File(intPath);
                File[] intFiles = intDir.listFiles();

                coverPath = null;

                for (int j = 0; j < intFiles.length; j++) {
                    nameBookDirectory = intFiles[j].getName();
                    if (nameBookDirectory.endsWith(".jpg")) {
                        coverPath = intDir + "/" + nameBookDirectory;
                    }
                }

                for (int k = 0; k < books.length; k++) {
                    String audFile = intFiles[k].getName();
                    if (audFile.endsWith(".mp3")) {
                        String filePath = intDir + "/" + audFile;
                        metaRetriever.setDataSource(filePath);
                        bookTitle = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                        if (bookTitle == null) {
                            bookTitle = nameBookDirectory;
                        }
                        author = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                        if (author == null) {
                            author = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
                        }
                        if (author == null) {
                            author = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR);
                        }
                        break;
                    }
                }

                if (books[i].isDirectory()) {
                    BookModel bookModel = new BookModel();

                    bookModel.setaTitle(bookTitle);
                    bookModel.setaCover(coverPath);
                    bookModel.setaAuthor(author);
                    bookModel.setaPath(bookDirectory);

                    tempBookList.add(bookModel);
                    addBookData(bookTitle);

                    // Sort the library alphabetically
                    Collections.sort(tempBookList, (obj1, obj2) -> obj1.getaTitle().compareToIgnoreCase(obj2.getaTitle()));
                }
            }

        return tempBookList;
    }

    public void addBookData(String title) {

        File files = getFilesDir();
        File newBook = new File(files, title);
        if (!newBook.exists()) {

            try {
                FileOutputStream fos = new FileOutputStream(newBook);
                OutputStreamWriter book = new OutputStreamWriter(fos);
                book.write("-1");
                book.close();
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        int bookNo = item.getGroupId();
        switch (item.getItemId())
        {
            case 121:
            playBook(bookNo, "Listen to book");
            return true;

            case 122:
            resetBook(bookNo, "Resetting ");
            return true;

            case 123:
            displayMessage("Delete book");
            return true;

            case 124:
            displayMessage("Return");
            return true;

            default:
                return super.onContextItemSelected(item);
        }

    }

    private void displayMessage(String message) {

//        Snackbar.make(findViewById(R.id.libraryView),message,Snackbar.LENGTH_SHORT).show();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();;
    }

    private void resetBook(int itemPosition, String message) {

        String title = allBookDirectories.get(1).getaTitle();
        Toast.makeText(this, message + title, Toast.LENGTH_SHORT).show();
        updateProgress.addBookProgress(getFilesDir(), title, -1);
    }

   private void playBook(int itemPosition, String message) {

        Toast.makeText(this, message , Toast.LENGTH_SHORT).show();
    }

    private void deleteBook(int itemPosition) {

    }

}
