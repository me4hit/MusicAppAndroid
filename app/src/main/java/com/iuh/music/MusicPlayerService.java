package com.iuh.music;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MusicPlayerService extends Service {
    private IBinder binder;
    private MusicPlayer musicPlayer;
    public MusicPlayerService() {
    }


    @Override
    public void onCreate() {
        Log.d("check", "khởi tạo onCreate");

        super.onCreate();
        binder = new MyBinder();
        musicPlayer = new MusicPlayer(this);

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("check", "khởi tạo onBind");

        return  binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("check", "khởi tạo onUnbind");

        musicPlayer.stop();
        return super.onUnbind(intent);
    }

    public void play(){
        musicPlayer.play();

    }
    public void rebeat(){
        musicPlayer.rebeat();
    }
    public void pause(){
        musicPlayer.pause();
    }

    public void forward(){
        musicPlayer.forward();
    }

    public void rewind(){
        musicPlayer.rewind();
    }
    public void stop(){
        musicPlayer.stop();
    }
    public boolean checkplaying(){
        return musicPlayer.checkplaying();
    }

    public MediaPlayer getMediaPlayer(){
        return musicPlayer.getMediaPlayer();
    }
    public String getNameSongplaying(){
        return musicPlayer.getNameSongplaying();
    }
    public class MyBinder extends Binder {

        // phương thức này trả về đối tượng MyService
        public MusicPlayerService getService() {

            return MusicPlayerService.this;
        }
    }


}