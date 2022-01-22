package com.sail.dirreader;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class AudioPlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    ArrayList chapterList;
    ArrayList<Long> durations;
    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    String mTrack, bookTitle, playStatus;
    int index, maxIndex;
    int itemPosition;
    int currentIndex = 0;
    ArrayList<String> playList;
    Timer timer;
    Boolean flagPaused = false;
    int startTrack;
    public int minTime = 15 * 60 * 1000;
    ArrayList<String> playListPaths;

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
        durations =(ArrayList<Long>) intent.getSerializableExtra("durations");
        playStatus = intent.getStringExtra("playStatus");


//        playList = intent.getStringArrayListExtra("filepath");


//        mediaPlayer = MediaPlayer.create(this, Uri.parse(playList.get(0)));
//        mediaPlayer.start();
//        enableSeekBar();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);


        TextView audioName = findViewById(R.id.audioName);
//        Button playBtn = findViewById(R.id.playBtn);
        Button viewAllMediaBtn = findViewById(R.id.viewAllMedia);
        Button skipToPrevious = findViewById(R.id.skip_to_previous);
        Button skipToNext = findViewById(R.id.skip_to_next);

        final ToggleButton playBtn = (ToggleButton)findViewById(R.id.playBtn);

        playBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
//                    if(!flagPaused) {
                        playBtn.setBackgroundResource(R.drawable.outline_play_circle_24);
//                    }
                        mediaPlayer.pause();
                        flagPaused = true;
//                    }
                } else {
                    playBtn.setBackgroundResource(R.drawable.outline_pause_circle_24);
                    if (flagPaused) {
                        mediaPlayer.start();
                    } else {
                        mediaPlayer.pause();
//                        playChapter2(index);
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
//                playBtn.setBackgroundResource(R.drawable.outline_pause_circle_24);
//                flagPaused = false;
            }
        });

        skipToNext.setOnClickListener(v -> {
            int skip = 1;
            mediaPlayer.stop();
            playNext(skip);
//            playBtn.setBackgroundResource(R.drawable.outline_pause_circle_24);
//            flagPaused = false;
        });

        createPlayList(itemPosition);

        if (playStatus .equals("Play")) {
            playChapter2(0);
        }

    }  // end of onCreate


    public ArrayList createPlayList(int startTrack) {

        Long playTime = durations.get(startTrack);
        int playChapters = 1;
        int lastTrack = startTrack;
//        ArrayList<Integer> playList = new ArrayList<Integer>();
        playListPaths = new ArrayList<String>();

//        playList.add(startTrack);
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

            Log.e("playList1 :", String.valueOf(playListPaths));

        return playListPaths;
    }

    public void playPlayList() {

        index = 0;
        maxIndex = playListPaths.size();
        while (index < maxIndex) {
            mediaPlayer = MediaPlayer.create(this, Uri.parse((String) playListPaths.get(index)));
            mediaPlayer.start();
            enableSeekBar();
            index++;
                    }
    }

    public void playChapter2(int index) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(playListPaths.get(index));
            mediaPlayer.prepare();
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

    public void playChapter() {

        mediaPlayer = MediaPlayer.create(this, Uri.parse((String) playListPaths.get(0)));
        mediaPlayer.start();
        enableSeekBar();
        if (playListPaths.size() > 1) playNext(1);
        index = 0;
        maxIndex = playListPaths.size();
        mTrack = (String) playListPaths.get(index);
        Log.e("play-track", mTrack);

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
            Log.e("  Index ** : ", String.valueOf(index));
            mediaPlayer.setDataSource(playListPaths.get(index));
            mediaPlayer.prepare();
            seekBar.setProgress(0);   // required if skipping while paused
            if (!flagPaused) {
                mediaPlayer.start();
                enableSeekBar();
                flagPaused = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void stopPlaying(){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
        }
    }

    public void enableSeekBar(){
        seekBar =  findViewById(R.id.seekBar);
        seekBar.setMax(mediaPlayer.getDuration());

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
