package com.example.zhangxing.mediaplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;

public class MusicService extends Service {
    public MediaPlayer mediaPlayer;
    public final IBinder binder = new MyBinder();
    public int isEnd = 0;
    public class MyBinder extends Binder {
        @Override
        protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {

            switch (code){
                case 0:{
                    if(mediaPlayer!=null){
                        reply.writeInt(mediaPlayer.getDuration());
                    }
                    break;
                }
                case 1:{
                    if(mediaPlayer!=null){
                        playMusic();
                    }
                    break;
                }
                case 2: {
                    if (mediaPlayer != null) {
                        stopMusic();
                    }
                    break;
                }
                case 3:{
                    if(mediaPlayer!=null){
                        mediaPlayer.seekTo(data.readInt());
                    }
                    break;
                }
                case 4:{
                    if(mediaPlayer!=null){
                        reply.writeInt(mediaPlayer.getCurrentPosition());
                    }
                    break;
                }
                case 5:{
                    if(mediaPlayer!=null){
                        setMusic(data.readString());
                    }
                    break;
                }
                case 6:{
                    if (mediaPlayer!=null){
                        reply.writeInt(isEnd);
                    }
                }
            }
            return super.onTransact(code, data, reply, flags);
        }
    }
    public MusicService(){
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource("/storage/emulated/0/山高水长.mp3");
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isEnd = 1;
            }
        });
    }
    public void playMusic(){
        isEnd=0;
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }else{
            mediaPlayer.start();
        }
    }
    public void stopMusic(){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            try {
                mediaPlayer.prepare();
                mediaPlayer.seekTo(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void setMusic(String str){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            try {
                mediaPlayer.prepare();
                mediaPlayer.seekTo(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(str);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
