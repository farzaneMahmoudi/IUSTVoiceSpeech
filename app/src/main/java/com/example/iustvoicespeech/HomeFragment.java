package com.example.iustvoicespeech;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iustvoicespeech.speech.MessageDialogFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements MessageDialogFragment.Listener {
    private static final int REQUEST_PERMISSION_CODE = 1000;


    private Button mRecord;
    private Button mStopRecorder;
    private Button mPlay;
    private Button mStop;
    private Button mNext;
    private Button mPrev;

    private TextView mTextView;
    private String outputFile;
    private MediaRecorder myAudioRecorder;
    private MediaPlayer mMediaPlayer;
    private TextView mTextViewVoice;

    private String[] mTextArray;
    private int index = 0;

    String mSavedPath;
    File mDestinationFile;
    private File mDestinationDirectory;


    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSavedPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "IUST Recording Folder";
        mDestinationDirectory = new File(mSavedPath);
        if (!mDestinationDirectory.exists()) {
            mDestinationDirectory.mkdirs();
        }

        mTextArray = new String[25];
        mTextArray = new String[]{"تهران یکی از آلوده ترین شهر های ایران است.",
                "آرامگاه سعدی در شیراز است.",
                "امروز چرا ناراحتی؟",
                "به خانه برمی گردیم",
                "استرالیا بزرگترین قاره جهان نیست.",
                "بهار زیباترین فصل سال است.",
                "سلام.خوبی؟",
                "اقیانوس آرام بزرگترین اقیانوس جهان است.",
                "امروز کجا می روی؟",
                "فردا هوا بارانی است.",
                "قاره آسیا در نیم کره شمالی واقع است.",
                "همه ی کشور های خاورمیانه عرب زبان نیستند.",
                "کشور هندوستان یکی از پرجمعیت ترین کشورهای جهان است.",
                "کاش فردا برف ببارد.",
                "دیدگاهتان را تغییر دهید!!!",
                "احتمال پیروزی او زیاد است!",
                "وی در مسابقات والیبال مقام اول را کسب کرد.",
                "آسمان امروز ابریست.",
                "توانایی یادگیری هر فرد متفاوت است.",
                "شطرنج بازی مورد علاقه اوست.",
                "کتاب شاهنامه از فردوسی است."};
    }

    public HomeFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initUI(view);
        if (!checkPermissionFromDevice())
            requestPermission();
        mTextView.setText(mTextArray[0]);

        mStop.setEnabled(false);
        mStopRecorder.setEnabled(false);
        mPlay.setEnabled(false);
        mRecord.setEnabled(true);
        mRecord.setBackground(getResources().getDrawable(R.drawable.ic_record));

        clickListener();
        return view;
    }

    private File setAudioFileName() {
        outputFile =
                "sentence_" + index +"_"+ UUID.randomUUID().toString()+ ".wav";

        mDestinationFile = new File(mDestinationDirectory, outputFile);

        try {
            FileOutputStream outputStream = new FileOutputStream(mDestinationFile);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mDestinationFile;
    }

    private void clickListener() {
        mNext.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                mStop.setEnabled(false);
                mStopRecorder.setEnabled(false);
                mPlay.setEnabled(false);
                mRecord.setEnabled(true);
                mRecord.setBackground(getResources().getDrawable(R.drawable.ic_record));
                index++;
                mTextView.setText(mTextArray[index % mTextArray.length]);
            }
        });

        mPrev.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                mStop.setEnabled(false);
                mStopRecorder.setEnabled(false);
                mPlay.setEnabled(false);
                mRecord.setEnabled(true);
                mRecord.setBackground(getResources().getDrawable(R.drawable.ic_record));
                if (index == 0)
                    index = mTextArray.length - 1;
                index--;
                mTextView.setText(mTextArray[index]);
            }
        });

        mRecord.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (checkPermissionFromDevice()) {

                    mRecord.setEnabled(false);
                    mStopRecorder.setEnabled(true);
                    mStop.setEnabled(false);
                    mPlay.setEnabled(false);
                    mRecord.setBackground(getResources().getDrawable(R.drawable.ic_disabled_record));

                        setUpMediaRecorder(setAudioFileName());

                    try {
                        myAudioRecorder.prepare();
                        myAudioRecorder.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else {
                    requestPermission();
                }
            }
        });

        mStop.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                mRecord.setEnabled(true);
                mRecord.setBackground(getResources().getDrawable(R.drawable.ic_record));
                mPlay.setEnabled(true);
                mStopRecorder.setEnabled(false);
                mStop.setEnabled(false);

                if (mMediaPlayer != null) {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    setUpMediaRecorder(mDestinationFile);
                }
            }
        });

        mPlay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                mStop.setEnabled(true);
                mStopRecorder.setEnabled(false);
                mRecord.setEnabled(false);
                mRecord.setBackground(getResources().getDrawable(R.drawable.ic_disabled_record));

                mMediaPlayer = new MediaPlayer();
                try {
                   String s= mSavedPath+"/" +outputFile;
                    mMediaPlayer.setDataSource(s);
                    mMediaPlayer.prepare();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                if (mMediaPlayer.isPlaying())
                    mMediaPlayer.stop();
                mMediaPlayer.start();
            }
        });

        mStopRecorder.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                myAudioRecorder.stop();
                mStopRecorder.setEnabled(false);
                mPlay.setEnabled(true);
                mRecord.setEnabled(true);
                mStop.setEnabled(false);
                mRecord.setBackground(getResources().getDrawable(R.drawable.ic_record));
            }
        });
    }

    private void setUpMediaRecorder(File destination) {
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(Uri.parse(String.valueOf(destination)).toString());

    }

    private void initUI(View view) {
        mRecord = view.findViewById(R.id.record_icon);
        mPlay = view.findViewById(R.id.play_icon);
        mTextView = view.findViewById(R.id.text_view);
        mStopRecorder = view.findViewById(R.id.stop_recorder_icon);
        mTextViewVoice = view.findViewById(R.id.text_view_voice);
        mStop = view.findViewById(R.id.stop_recorded_file);
        mNext = view.findViewById(R.id.next);
        mPrev = view.findViewById(R.id.prev);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mTextViewVoice.setText(result.get(0));
                }
                break;
        }

        if (requestCode == 1111) {
            File folder = new File(Environment.getExternalStorageDirectory(), "/Sounds");
            long folderModi = folder.lastModified();

            FilenameFilter filter = new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return (name.endsWith("3ga"));
                }
            };

            File[] folderList = folder.listFiles(filter);

            String recentName = "";

            for (int i = 0; i < folderList.length; i++) {
                long fileModi = folderList[i].lastModified();

                if (folderModi == fileModi) {
                    recentName = folderList[i].getName();
                }
            }
        }
    }

    private boolean checkPermissionFromDevice() {
        int WRITE_EXTERNAL_STORAGE_RESULT = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int RECORD_AUDIO_RESULT = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO);
        return WRITE_EXTERNAL_STORAGE_RESULT == PackageManager.PERMISSION_GRANTED &&
                RECORD_AUDIO_RESULT == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(getActivity(), "permission granted", Toast.LENGTH_SHORT);
                else Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_SHORT);
            }
            break;
        }
    }

    @Override
    public void onMessageDialogDismissed() {

    }
}
