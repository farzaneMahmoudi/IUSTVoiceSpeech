package com.example.iustvoicespeech;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.widget.Toast;

import java.io.IOException;

public class Recorder {
    private MediaRecorder mMediaRecorder;
    private boolean isStop=true;
    private boolean isRecord=false;
    private static Recorder instance;

    private Recorder() {

    }

    public static Recorder getInstance(){
       if(instance == null)
           instance = new Recorder();
       return instance;
    }

    public void setOutputFile(String outputFile) {
        if(mMediaRecorder!=null)
        mMediaRecorder.setOutputFile(outputFile);
    }

    public void stopRecorder() {
        if(mMediaRecorder!=null) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }
    public void record() {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mMediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();

        } catch (IllegalStateException ise) {
            // make something ...
        } catch (IOException ioe) {
            // make something
        }
    }
    public void play(String outputFile) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(outputFile);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            // make something
        }
    }
}
