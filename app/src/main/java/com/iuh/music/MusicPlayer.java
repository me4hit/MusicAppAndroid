package com.iuh.music;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MusicPlayer {
    List<Music> musiclist = new ArrayList<>();
    private int music_now=0;
    private MediaPlayer mediaPlayer;
    private Context context;
    public MusicPlayer(Context context) {
        this.context = context;
        musiclist.add(new Music(R.raw.cutheroixa, "Cứ thế rời xa"));
        musiclist.add(new Music(R.raw.kedientinvaotinhyeu, "Kẻ điên tin vào tình yêu"));
        musiclist.add(new Music(R.raw.sailamcuaanh, "Sai lầm của anh"));
        music_now =0;
        mediaPlayer = MediaPlayer.create(context, musiclist.get(0).getId());
    }

    public void play() {
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(context, musiclist.get(0).getId());
        }
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }
    public void rebeat(){
        mediaPlayer = MediaPlayer.create(context, musiclist.get(music_now).getId());
        mediaPlayer.start();

    }
    public void forward(){
        music_now++;
        if(music_now >musiclist.size()-1){
            music_now = 0;
        }
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        mediaPlayer = MediaPlayer.create(context, musiclist.get(music_now).getId());
        mediaPlayer.start();
    }

    public void rewind(){
        music_now--;
        if(music_now < 0  ){
            music_now = 0 ;
        }
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        mediaPlayer = MediaPlayer.create(context, musiclist.get(music_now).getId());
    }

    public void stop(){
        mediaPlayer.stop();
    }

    public boolean checkplaying(){
        if(mediaPlayer == null){
            return false;
        }
       return mediaPlayer.isPlaying();
    }

    public MediaPlayer getMediaPlayer(){
        return  mediaPlayer;
    }

    public String getNameSongplaying(){
        return   musiclist.get(music_now).getName();
    }
}
