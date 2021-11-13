package com.sail.dirreader;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class AudioPlayerActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    AudioModel audio;
    SeekBar seekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        Intent intent = getIntent();

        ArrayList<String> playList = intent.getStringArrayListExtra("filepath");
        Log.e("player-path :", String.valueOf(playList));

        for (int index = 0; index < playList.size(); index++) {
            String playTrack = (String) playList.get(index);
            Log.e("play-track", playTrack);
        }


        audio = (AudioModel) intent.getSerializableExtra("playList");

        TextView audioName = findViewById(R.id.audioName);
        Button playBtn = findViewById(R.id.playBtn);
        Button viewAllMediaBtn = findViewById(R.id.viewAllMedia);

        if(audio!=null){
            audioName.setText(audio.getaName());
        }else{
            audioName.setText("Piano.wav");
        }


        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlaying();
            }
        });

        viewAllMediaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlaying();
//                startActivity(new Intent(getApplicationContext(),MusicListActivity.class));
                //finish();
            }
        });

    }

    public void startPlaying(){
        if(mediaPlayer!=null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }

        if(audio!=null){

            Toast.makeText(getApplicationContext(),"Playing from device; "+audio.getaPath(), Toast.LENGTH_SHORT).show();
            mediaPlayer = MediaPlayer.create(AudioPlayerActivity.this, Uri.parse(audio.getaPath()));
            mediaPlayer.start();

        }else{

            Toast.makeText(getApplicationContext(),"Playing from app", Toast.LENGTH_SHORT).show();
            try {
                AssetFileDescriptor afd = getAssets().openFd("piano.wav");
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                afd.close();
                mediaPlayer.prepare();
                mediaPlayer.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        enableSeekBar();
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

}
