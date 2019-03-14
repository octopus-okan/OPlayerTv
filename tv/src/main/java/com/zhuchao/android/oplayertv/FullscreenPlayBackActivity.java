package com.zhuchao.android.oplayertv;


import android.annotation.SuppressLint;
import android.app.Activity;

import android.app.AlertDialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;

import android.os.CountDownTimer;

import android.util.Log;

import android.view.KeyEvent;

import android.view.SurfaceView;
import android.view.View;

import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zhuchao.android.callbackevent.PlayerCallBackInterface;
import com.zhuchao.android.video.Video;

import java.util.Map;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenPlayBackActivity extends Activity implements PlayerCallBackInterface {
    private static final String TAG = "FullscreenPlayBackActivity-->";
    private static SurfaceView mSurfaceView;
    private static Video mvideo = null;
    private static CountDownTimer mCountDownTimer;
    private static CountDownTimer mCountDownTimer1;
    private TextView mtextView;
    private ProgressBar mProgressBar = null;
    private static int Counter = 0;
    private static HomeWatcherReceiver mHomeKeyReceiver = null;
    //MyPlayer OPlayer = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_fullscreen);
        mSurfaceView = findViewById(R.id.surfaceView);
        mtextView = findViewById(R.id.textView);
        mSurfaceView.setVisibility(View.INVISIBLE);
        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);


        try {
            mvideo = (Video) getIntent().getSerializableExtra("Video");
            mvideo.setmCallback(this);
            mtextView.setText(mvideo.getmMovie().getMovieName().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mCountDownTimer = new CountDownTimer(7000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Counter++;
                if (mSurfaceView.getVisibility() != View.VISIBLE && Counter >= 2) {
                    mSurfaceView.setVisibility(View.VISIBLE);
                    Counter = 0;
                }
            }

            @Override
            public void onFinish() {
                if (mvideo.getmNextVideo() != null) {
                    mvideo = mvideo.getmNextVideo();
                    playVideo(mvideo);
                } else if (mvideo.getmPreVideo() != null) {
                    mvideo = mvideo.getmPreVideo();
                    playVideo(mvideo);
                } else {
                    //FullscreenPlayBackActivity.this.finish();
                }
            }
        };

        mCountDownTimer1 = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // do something
            }

            @Override
            public void onFinish() {
                if (mvideo != null)
                    playVideo(mvideo);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        playVideo(mvideo);
    }

    private void playVideo(final Video video) {
        Map<String, String> map;
        if (video != null) {
            mProgressBar.setVisibility(View.VISIBLE);
            mtextView.setText(video.getmMovie().getMovieName().toString());
            //Log.d(TAG, "SourceID=" + video.getmMovie().getSourceId() + ", " + video.getmMovie().getSourceUrl());
            try {
                video.with(this).playInto(mSurfaceView);
                mCountDownTimer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerHomeKeyReceiver(this);
        if(mvideo != null)
            if(mvideo.getmOPlayer() != null)
                mvideo.getmOPlayer().playPause();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterHomeKeyReceiver(this);
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");
        if (mvideo != null)
            mvideo.stopPlayer();
        this.finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCountDownTimer.cancel();
        mCountDownTimer1.cancel();
        if (mvideo != null)
            mvideo.stopPlayer();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //if(mShapeLoadingDialog !=null && mShapeLoadingDialog.isShowing()) return super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.i(TAG, "onKeyDown KeyEvent.KEYCODE_BACK");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("退出提示：");
            builder.setMessage("您真的要退出吗？");

            builder.setNegativeButton("不，继续观看", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.show();
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (mvideo.getmPreVideo() != null) {
                mvideo = mvideo.getmPreVideo();
                mtextView.setText(mvideo.getmMovie().getMovieName().toString());
                mCountDownTimer1.cancel();
                mCountDownTimer1.start();
            }
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (mvideo.getmNextVideo() != null) {
                mvideo = mvideo.getmNextVideo();
                mtextView.setText(mvideo.getmMovie().getMovieName().toString());
                mCountDownTimer1.cancel();
                mCountDownTimer1.start();
            }
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            mvideo.getmOPlayer().playPause();
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN_LEFT) {

        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void OnEventCallBack(int i, long l, long l1, float v, int i1, int i2, int i3, float v1) {
        if (mvideo.getmOPlayer() == null) return;
        //Log.d(TAG,"l="+l +", ll=" + l1+", v="+ v +", i1="+ i1  +", i2="+ i2  +", i3="+ i3  +", v1="+ v1);
        if (mvideo.getmOPlayer().getPlayerState() >= 3 && mvideo.getmOPlayer().getPlayerState() <= 6 && l != 0) {
            if (mProgressBar != null)
                mProgressBar.setVisibility(View.GONE);
            mCountDownTimer.cancel();
        } else {

        }
    }


    class HomeWatcherReceiver extends BroadcastReceiver {
        private static final String LOG_TAG = "HomeReceiver";
        private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
        private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        private static final String SYSTEM_DIALOG_REASON_LOCK = "lock";
        private static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(LOG_TAG, "onReceive: action: " + action);
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                // android.intent.action.CLOSE_SYSTEM_DIALOGS
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);

                Log.i(LOG_TAG, "reason: " + reason);

                if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
                    // 短按Home键
                    Log.i(LOG_TAG, "homekey");
                    FullscreenPlayBackActivity.this.finish();
                } else if (SYSTEM_DIALOG_REASON_RECENT_APPS.equals(reason)) {
                    // 长按Home键 或者 activity切换键
                    Log.i(LOG_TAG, "long press home key or activity switch");
                    FullscreenPlayBackActivity.this.finish();
                } else if (SYSTEM_DIALOG_REASON_LOCK.equals(reason)) {
                    // 锁屏
                    FullscreenPlayBackActivity.this.finish();
                    Log.i(LOG_TAG, "lock");
                } else if (SYSTEM_DIALOG_REASON_ASSIST.equals(reason)) {
                    // samsung 长按Home键
                    FullscreenPlayBackActivity.this.finish();
                    Log.i(LOG_TAG, "assist");
                }
            }
        }
    }

    @SuppressLint("LongLogTag")
    private void registerHomeKeyReceiver(Context context) {
        Log.i(TAG, "registerHomeKeyReceiver");
        mHomeKeyReceiver = new HomeWatcherReceiver();
        final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

        context.registerReceiver(mHomeKeyReceiver, homeFilter);
    }

    @SuppressLint("LongLogTag")
    private void unregisterHomeKeyReceiver(Context context) {
        Log.i(TAG, "unregisterHomeKeyReceiver");
        if (null != mHomeKeyReceiver) {
            context.unregisterReceiver(mHomeKeyReceiver);
        }
    }

    public Map<String, String> JsonToMap(String Jsonstr) {
        Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
        try {
            return gson.fromJson(Jsonstr, new TypeToken<Map<String, String>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

}