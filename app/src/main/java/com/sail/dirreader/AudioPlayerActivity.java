package com.sail.dirreader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class AudioPlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    ArrayList chapterList, chapterName;
    ArrayList<Long> durations;
    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    String mTrack, bookTitle, playStatus, coverPath;
    int index, maxIndex;
    int itemPosition;
    int currentIndex = 0;
    ArrayList<String> playList;
    Timer timer;
    Boolean flagPaused = false;
    int startTrack;
    public int minTime = 15 * 60 * 1000;
    ArrayList<String> playListPaths;
    private TextView mChapterDuration, mCurrentPosition, mAudioName;
    View mCoverView;
    long chapterTime, currentPosition;


    private Handler mHandler = new Handler();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        Intent intent = getIntent();
        timer = new Timer();
        chapterList = new ArrayList<String>();

        bookTitle = intent.getStringExtra("bookTitle");
        itemPosition = intent.getIntExtra("position", 0);
        chapterList = intent.getStringArrayListExtra("paths");
        chapterName = intent.getStringArrayListExtra("chapterName");
        durations =(ArrayList<Long>) intent.getSerializableExtra("durations");
        playStatus = intent.getStringExtra("playStatus");
        coverPath = intent.getStringExtra("cover");

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);


        TextView audioName = findViewById(R.id.audioName);
        Button viewAllMediaBtn = findViewById(R.id.viewAllMedia);
        Button skipToPrevious = findViewById(R.id.skip_to_previous);
        Button skipToNext = findViewById(R.id.skip_to_next);
        mChapterDuration = findViewById(R.id.chapter_duration);
        mAudioName = findViewById(R.id.audioName);

        final ToggleButton playBtn = (ToggleButton)findViewById(R.id.playBtn);

        playBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    playBtn.setBackgroundResource(R.drawable.outline_play_circle_24);
                    mediaPlayer.pause();
                    flagPaused = true;
                } else {
                    playBtn.setBackgroundResource(R.drawable.outline_pause_circle_24);
                    if (flagPaused) {
                        mediaPlayer.start();
                    } else {
                        mediaPlayer.pause();
                    }
                    flagPaused = false;
                }
            }
        });

        skipToPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int skip = -1;
                mediaPlayer.stop();
                playNext(skip);
            }
        });

        skipToNext.setOnClickListener(v -> {
            int skip = 1;
            mediaPlayer.stop();
            playNext(skip);
        });

        createPlayList(itemPosition);
        makeCover(coverPath);

        if (playStatus .equals("Play")) {
            playChapter2(0);
        }

    }  // end of onCreate


    public ArrayList createPlayList(int startTrack) {

        Long playTime = durations.get(startTrack);
        int playChapters = 1;
        int lastTrack = startTrack;

        playListPaths = new ArrayList<String>();
        playListPaths.add((String) chapterList.get(startTrack));

        while(playTime < minTime) {
            lastTrack = lastTrack + 1;
            if(lastTrack < chapterList.size()) {
                playTime = playTime + durations.get(lastTrack);
                playChapters = playChapters + 1;
                playListPaths.add((String) chapterList.get(lastTrack));
            } else {
                break;
            }
        }
        return playListPaths;
    }

    public void playChapter2(int index) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(playListPaths.get(index));
            mediaPlayer.prepare();
            String currentChapter = (String) chapterName.get(itemPosition + index);
            mAudioName.setText(currentChapter);

        // Update chapter duration
            chapterTime = durations.get(index);
            String dspChaptTime = String.format("%2d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(chapterTime),
                        TimeUnit.MILLISECONDS.toSeconds(chapterTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(chapterTime)));
            mChapterDuration.setText(dspChaptTime);

            if (flagPaused) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
                enableSeekBar();
                flagPaused = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playNext(int skip) {
        try {

            mediaPlayer.reset();
            index = index + skip;
            if (index < 0 ) {
                index = 0;
            }
            if (index > playListPaths.size() - 1) {
                index = playListPaths.size() - 1;
            }
            mediaPlayer.setDataSource(playListPaths.get(index));
            mediaPlayer.prepare();
            String currentChapter = (String) chapterName.get(index);
            mAudioName.setText(currentChapter);
            seekBar.setProgress(0);   // required if skipping while paused

        // Update chapter duration
            chapterTime = durations.get(index);
            String dspChaptTime = String.format("%2d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(chapterTime),
                        TimeUnit.MILLISECONDS.toSeconds(chapterTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(chapterTime)));
            mChapterDuration.setText(dspChaptTime);

            if (!flagPaused) {
                mediaPlayer.start();
                enableSeekBar();
                flagPaused = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeCover(String coverPath) {

        View mCoverView = findViewById(R.id.book_cover);
        Bitmap bitmap = BitmapFactory.decodeFile(coverPath);
        BitmapDrawable coverBMP = new BitmapDrawable(bitmap);
        mCoverView.setBackground(coverBMP);

    }


    public void stopPlaying(){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
        }
    }

    public void enableSeekBar(){
        seekBar =  findViewById(R.id.seekBar);
        seekBar.setMax(mediaPlayer.getDuration());

        mHandler.postDelayed(mUpdateTimeTask, 100);


        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                if(mediaPlayer!=null && mediaPlayer.isPlaying()){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                }
            }
        }, 0, 10);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                // Update the progress depending on seek bar
                if(fromUser){
                    mediaPlayer.seekTo(progress);

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        @Override
        public void run() {

            mCurrentPosition = findViewById(R.id.current_position);
            currentPosition = mediaPlayer.getCurrentPosition();
            String dspCurrentPos = String.format("%2d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(currentPosition),
                    TimeUnit.MILLISECONDS.toSeconds(currentPosition) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentPosition)));
            mCurrentPosition.setText(dspCurrentPos);

               // Running this thread after 100 milliseconds
               mHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onBackPressed() {
        stopPlaying();
        super.onBackPressed();
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        if ( index < (playListPaths.size() - 1)) {
            playChapter2(index + 1);
            index = index + 1;
        } else {
            finish();
        }
    }
}
