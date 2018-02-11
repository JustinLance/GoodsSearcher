package com.lixianjie.goodssearcher.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.lixianjie.goodssearcher.R;
import com.lixianjie.goodssearcher.db.GoodsDao;
import com.lixianjie.goodssearcher.db.GoodsBean;
import com.lixianjie.goodssearcher.util.ToastUitl;
import com.mining.app.zxing.camera.CameraManager;
import com.mining.app.zxing.decoding.CaptureActivityHandler;
import com.mining.app.zxing.decoding.InactivityTimer;
import com.mining.app.zxing.view.ViewfinderView;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import dmax.dialog.SpotsDialog;

/**
 * @Author lixianjie
 * @Des 查詢商品
 * @Time 2017/1/26/0026
 */

public class QueryGoodsActivity extends Activity implements SurfaceHolder.Callback, Handle {
    private static final String TAG = QueryGoodsActivity.class.getSimpleName();
    private CaptureActivityHandler handler;
    private ViewfinderView mViewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private GoodsDao mGoodsDao;
    private Button mManualQuery;
    private Button mBtnBack;
    private SpotsDialog spotsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        CameraManager.init(getApplication());
        initView();
        initEvent();

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        mGoodsDao = new GoodsDao();
    }

    private void initView() {
        mBtnBack = (Button) findViewById(R.id.button_back);
        mManualQuery = (Button) findViewById(R.id.btn_manual_query);
        mViewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
    }

    private void initEvent() {
        mBtnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                QueryGoodsActivity.this.finish();
            }
        });

        mManualQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QueryGoodsActivity.this, ManualQueryActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
        hideLoading();
    }

    private void hideLoading() {
        if (spotsDialog != null) {
            spotsDialog.dismiss();
        }
    }


    /**
     * 处理扫描结果
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, final Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        final String resultString = result.getText();
        if (resultString.equals("")) {
            Toast.makeText(QueryGoodsActivity.this, "扫描失败!", Toast.LENGTH_SHORT).show();
        } else {
            if (spotsDialog != null) {
                spotsDialog.dismiss();
            }
            spotsDialog = new SpotsDialog(QueryGoodsActivity.this);
            spotsDialog.show();
//            HashMap<String, String> map = new HashMap<>();
//            map.put(GoodsBean.KEY_BAR_CODE, resultString);
//            List<GoodsBean> query = mGoodsDao.query(map);
            new BmobQuery<GoodsBean>().addWhereEqualTo("barCode", resultString).findObjects(new FindListener<GoodsBean>() {
                @Override
                public void done(List<GoodsBean> query, BmobException e) {
                    if (query.size() > 0) {
                        //有数据
                        ToastUitl.showShort("有数据");
                        Intent intent = new Intent(QueryGoodsActivity.this, GoodsDetailsActivity.class);
                        intent.putExtra("GoodsBean", query.get(0));
                        startActivity(intent);
                        QueryGoodsActivity.this.finish();
                    } else {
                        //没有数据
//                ToastUitl.showShort("没有数据");
                        hideLoading();
                        AlertDialog.Builder builder = new AlertDialog.Builder(QueryGoodsActivity.this);
                        builder.setMessage("该商品还没添加到数据库中，是否添加到数据库？");
                        builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(QueryGoodsActivity.this, AddGoodsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("barcode", resultString);
                                bundle.putParcelable("bitmap", barcode);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                QueryGoodsActivity.this.finish();
                                dialog.dismiss();
                            }
                        });

                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                QueryGoodsActivity.this.recreate();
                            }
                        });
                        builder.show();

                        if (e == null) {
                            Log.e(TAG, "查询成功 : " + query);
                        } else {
                            Log.e(TAG, "查询失败...", e);
                        }
                    }
                }
            });
            //Intent resultIntent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putString("result", resultString);
//            bundle.putParcelable("bitmap", barcode);
//            resultIntent.putExtras(bundle);
//            this.setResult(RESULT_OK, resultIntent);
        }
//        QueryGoodsActivity.this.finish();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return mViewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        mViewfinderView.drawViewfinder();
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };
}
