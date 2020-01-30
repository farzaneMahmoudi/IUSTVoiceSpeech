package com.example.iustvoicespeech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecordedFilesActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ListAdapter mListAdapter;
    private String mSavedPath;
    private String[] mStringNames;
    private TextView mTextView;


    public static Intent newIntent(Context context) {
        return new Intent(context, RecordedFilesActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorded_files);

        initUi();
        getStringRecordedNames();
        setAdapter();

    }

    private void getStringRecordedNames() {
        mSavedPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "IUST Recording Folder";
        File file = new File(mSavedPath);
        mStringNames = file.list();
    }

    private void setAdapter() {
        if (mStringNames.length == 0)
            mTextView.setVisibility(View.VISIBLE);
        else {
            mTextView.setVisibility(View.GONE);
            mListAdapter = new ListAdapter(this);
            mListAdapter.setStringNames(mStringNames);
            mRecyclerView.setAdapter(mListAdapter);
        }
    }

    private void initUi() {
        mRecyclerView = findViewById(R.id.recycler_recorded_files);
        mTextView = findViewById(R.id.text_no_files);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ItemViewHolder> {

        private Context mContext;

        private String[] mStringNames;

        public void setStringNames(String[] stringNames) {
            mStringNames = stringNames;
        }

        public ListAdapter(Context context) {
            mContext = context;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.record_item, parent, false);
            return new ListAdapter.ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            holder.bind(mStringNames[position], position);
        }

        @Override
        public int getItemCount() {
            return mStringNames.length;
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            private TextView mTextView;
            private TextView mTextView_Date;
            private String mStringName;
            private MediaPlayer mMediaPlayer;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                findView(itemView);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMediaPlayer = new MediaPlayer();
                        try {
                            String s = mSavedPath + "/" + mStringName;
                            mMediaPlayer.setDataSource(s);
                            mMediaPlayer.prepare();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (mMediaPlayer.isPlaying())
                            mMediaPlayer.stop();
                        mMediaPlayer.start();
                    }
                });
            }

            private void findView(@NonNull View itemView) {
                mTextView = itemView.findViewById(R.id.text_voice_name);
                mTextView_Date = itemView.findViewById(R.id.text_voice_date);
            }


            public void bind(String stringName, int position) {
                mStringName = stringName;
                int end = stringName.lastIndexOf("_");
                mTextView.setText(stringName.substring(0, end));
                mTextView_Date.setText(stringName.substring(0, end) + "_" + position);
            }
        }
    }


}
