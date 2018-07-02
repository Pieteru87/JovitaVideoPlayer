package pieter.co.id.jovitavideoplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class PlayVidActivity extends AppCompatActivity {
    VideoView vv;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // hide statusbar of Android
        // could also be done later
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play_vid);


        vv = findViewById(R.id.videoView2);
        MediaController mediaCtrl= new MediaController(this);
        mediaCtrl.setAnchorView(vv);
        vv.setMediaController(mediaCtrl);
        final File fl = new File(Environment.getExternalStorageDirectory() + File.separator + "JovVideo");
        final String[] fls = fl.list();
        i=getIntent().getIntExtra("IDXPILIH",0);
        PutarVid(fl,fls,i,vv);

        View.OnClickListener onClickListenerNext = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=i+1;
                if (fls.length==i){
                    i=0;
                }
                PutarVid(fl,fls, i,vv);
            }
        };
        View.OnClickListener onClickListenerPrevious = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=i-1;
                if (i<=0){
                    i=fls.length-1;
                }
                PutarVid(fl,fls, i,vv);
            }
        };
        mediaCtrl.setPrevNextListeners(onClickListenerNext,onClickListenerPrevious);

        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                i=i+1;
                if (fls.length==i){
                    i=0;
                }
                PutarVid(fl,fls, i,vv);
            }
        });

        vv.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(PlayVidActivity.this,"Tidak bisa memutar video",Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    public void PutarVid(File fl1,String[] fls1,int idx, VideoView vv1)
    {
        if (fls1.length>0 && idx < fls1.length)
        {
            if (fls1[idx].toUpperCase().endsWith(".MP4") || fls1[idx].toUpperCase().endsWith(".3GP")){
                Uri uriJov = Uri.parse(fl1 + File.separator + fls1[idx]);
                Toast tst=Toast.makeText(PlayVidActivity.this,fls1[idx],Toast.LENGTH_LONG);
                tst.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                tst.show();
                vv1.setVideoURI(uriJov);
                vv1.start();
            }
        }
    }
}
