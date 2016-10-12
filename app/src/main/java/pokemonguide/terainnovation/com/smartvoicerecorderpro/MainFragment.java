package pokemonguide.terainnovation.com.smartvoicerecorderpro;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainFragment extends Fragment implements MediaPlayer.OnPreparedListener {

    Button recordButton;

    Button playButton;

    private static String mFileName = null;

    private MediaRecorder mRecorder = null;

    private PlayButton   mPlayButton = null;
    private MediaPlayer mPlayer = null;

    boolean mStartRecording = false;

    boolean mStartPlaying = true;

    SeekBar seekBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.mp3";

        recordButton = (Button) rootView.findViewById(R.id.recordButton);

        seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    if(mPlayer != null && mPlayer.isPlaying()){
                        mPlayer.seekTo(progress);
                    }

                }
            }
        });

        playButton = (Button) rootView.findViewById(R.id.helpButton);
        playButton.setEnabled(false);

        recordButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onRecord(!mStartRecording);
                if (!mStartRecording) {
                    playButton.setEnabled(false);
                    recordButton.setText("Stop");
                } else {
                    playButton.setEnabled(true);
                    recordButton.setText("Record");
                }
                mStartRecording = !mStartRecording;
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    playButton.setText("Stop playing");
                    recordButton.setEnabled(false);
                } else {
                    playButton.setText("Start playing");
                    recordButton.setEnabled(true);
                }
                mStartPlaying = !mStartPlaying;
            }
        });

        return rootView;
    }

    private Runnable onEverySecond = new Runnable() {
        @Override
        public void run(){
            if(!mStartPlaying){
                if(seekBar != null) {
                    seekBar.setProgress(mPlayer.getCurrentPosition());
                }

                if(mPlayer.isPlaying()) {
                    seekBar.postDelayed(onEverySecond, 1000);
                }
            }else{
                seekBar.setProgress(0);
            }
        }
    };

    @Override
    public void onPrepared(MediaPlayer arg0) {
        // TODO Auto-generated method stub
        duration = mPlayer.getDuration();
        seekBar.setMax(duration);
        seekBar.postDelayed(onEverySecond, 1000);
    }

    private int duration = 0;



    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("", "prepare() failed", e);
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        mPlayer.setOnPreparedListener(this);
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            seekBar.setEnabled(true);
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    playButton.setText("Start playing");
                    recordButton.setEnabled(true);
                    mStartPlaying = true;
                    seekBar.setEnabled(false);
                    mp.release();
                    mPlayer = null;
                }

            });
        } catch (IOException e) {
            Log.e("LOG", "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    class RecordButton extends Button {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }

    class PlayButton extends Button {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }
}