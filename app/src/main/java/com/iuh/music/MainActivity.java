package com.iuh.music;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private ImageButton btn_play, btn_forward, btn_rewind, btn_rebeat;
    private TextView tv_name,  tv_remainingTime, tv_songDuration;
    private ServiceConnection connection;
    private  MusicPlayerService musicPlayerService;
    private Handler updateHandler = new Handler();
    private boolean rebeat = false;
    private SeekBar seekBarMusic;
    private boolean isServicerunning = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_play = findViewById(R.id.btn_play);
        btn_forward = findViewById(R.id.btn_forward);
        btn_rewind = findViewById(R.id.btn_rewind);
        tv_name= findViewById(R.id.tv_namemusic);
        btn_rebeat = findViewById(R.id.btn_rebeat);
        tv_name.setText("");
        tv_remainingTime = findViewById(R.id.tv_remainingTime);
        tv_songDuration = findViewById(R.id.tv_songDuration);
        Intent intent = new Intent(this, MusicPlayerService.class);
        tv_remainingTime.setText(String.format("%02d:%02d",0, 0));
        tv_songDuration.setText(String.format("%02d:%02d",0, 0));
        seekBarMusic = findViewById(R.id.seekBarMusic);
        seekBarMusic.setMax(100);

        btn_rebeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rebeat) {
                    btn_rebeat.setImageResource(R.drawable.icons8_replace_50px_4);
                    Toast.makeText(MainActivity.this,
                            "Đã TẮT tự động chuyển bài, bài hát sẽ được lặp lại !!", Toast.LENGTH_SHORT).show();
                    rebeat = true;
                }
                else {
                    btn_rebeat.setImageResource(R.drawable.icons8_replace_50px);
                    Toast.makeText(MainActivity.this,
                            "Đã BẬT tự động chuyển bài !!", Toast.LENGTH_SHORT).show();

                    rebeat = false;
                }
                Log.d("check", "trạng thái rebeat"+rebeat);
                checkName();
            }
        });

        connection  = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d("check", "khởi tạo");
                MusicPlayerService.MyBinder binder = (MusicPlayerService.MyBinder) service;
                musicPlayerService = binder.getService();
                isServicerunning = true;
                Log.d("check", "running"+isServicerunning);

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        seekBarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                    musicPlayerService.getMediaPlayer().seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isServicerunning){
                    Log.d("check", "running"+isServicerunning);
                    if(!musicPlayerService.checkplaying()){
                        musicPlayerService.play();
                        tv_name.setText(musicPlayerService.getNameSongplaying());
                        int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(musicPlayerService.getMediaPlayer().getDuration());
                        int seconds = (int)TimeUnit.MILLISECONDS.toSeconds(musicPlayerService.getMediaPlayer().getDuration()) - minutes * 60;
                        tv_songDuration.setText(String.format("%02d:%02d",minutes, seconds));
                        seekBarMusic.setMax((int) musicPlayerService.getMediaPlayer().getDuration() );
                        btn_play.setImageResource(R.drawable.icons8_pause_50px);
                        checkName();
                        updateHandler.postDelayed(updater, 100);
                    }else{
                        updateHandler.removeCallbacks(updater);
                        musicPlayerService.pause();
                        btn_play.setImageResource(R.drawable.icons8_play_50px);
                    }

                }
            }
        });

        btn_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayerService.stop();
                musicPlayerService.forward();
                tv_name.setText(musicPlayerService.getNameSongplaying());
                int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(musicPlayerService.getMediaPlayer().getDuration());
                int seconds = (int)TimeUnit.MILLISECONDS.toSeconds(musicPlayerService.getMediaPlayer().getDuration()) - minutes * 60;
                tv_songDuration.setText(String.format("%02d:%02d",minutes, seconds));
                seekBarMusic.setMax((int) musicPlayerService.getMediaPlayer().getDuration() );
                btn_play.setImageResource(R.drawable.icons8_pause_50px);
                checkName();
                updateHandler.postDelayed(updater, 100);
            }
        });
        btn_rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayerService.stop();
                musicPlayerService.rewind();
                tv_name.setText(musicPlayerService.getNameSongplaying());
                int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(musicPlayerService.getMediaPlayer().getDuration());
                int seconds = (int)TimeUnit.MILLISECONDS.toSeconds(musicPlayerService.getMediaPlayer().getDuration()) - minutes * 60;
                tv_songDuration.setText(String.format("%02d:%02d",minutes, seconds));
                seekBarMusic.setMax((int) musicPlayerService.getMediaPlayer().getDuration() );
                btn_play.setImageResource(R.drawable.icons8_pause_50px);
                checkName();
                updateHandler.postDelayed(updater, 100);
            }
        });
    }

    private  Runnable updater = new Runnable() {
        @Override
        public void run() {
            long currentDuration =  musicPlayerService.getMediaPlayer().getCurrentPosition();
            seekBarMusic.setProgress((int) currentDuration);
            long currentTime = musicPlayerService.getMediaPlayer().getCurrentPosition();
            int minutes = (int)TimeUnit.MILLISECONDS.toMinutes(currentTime);
            int seconds = (int)TimeUnit.MILLISECONDS.toSeconds(currentTime) - minutes * 60;
            tv_remainingTime.setText(String.format("%02d:%02d",minutes, seconds));
            updateHandler.postDelayed(updater, 300);
        }
    };

    public  void checkName(){
        musicPlayerService.getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(rebeat){
                    Log.d("check", "chạy rebeat");
                    musicPlayerService.rebeat();
                    checkName();
                }else{
                    Log.d("check", "chạy tiếp");
                    musicPlayerService.forward();
                    checkName();
                    tv_name.setText(musicPlayerService.getNameSongplaying());
                    int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(musicPlayerService.getMediaPlayer().getDuration());
                    int seconds = (int)TimeUnit.MILLISECONDS.toSeconds(musicPlayerService.getMediaPlayer().getDuration()) - minutes * 60;
                    tv_songDuration.setText(String.format("%02d:%02d",minutes, seconds));
                    seekBarMusic.setMax((int) musicPlayerService.getMediaPlayer().getDuration() );
                    updateHandler.postDelayed(updater, 100);
                }
            }
        });
    }



}