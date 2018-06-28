package pieter.co.id.jovitavideoplayer;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btnPlay;
    TextView txt1,txt2,txt3;
    ListView lVw;
    ProgressDialog pG;
    final File fldrExtern = new File(Environment.getExternalStorageDirectory() + File.separator + "JovVideo");
    String[] fils = fldrExtern.list();
    int drt, hrs, mnt, scd;
    String frmtd;
    int pos;
    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt1 = findViewById(R.id.textView);
        txt2 = findViewById(R.id.textView2);
        txt3 =findViewById(R.id.textView3);
        lVw = findViewById(R.id.ListVw);
        txt2.setText(fldrExtern.toString());
        txt3.setText("");

        ArrayList<String> playList= new ArrayList<>();
        int ii=1;
        if (fils.length>0){
            for (String plyLst: fils){
                if (plyLst.toUpperCase().endsWith(".MP4") || plyLst.toUpperCase().endsWith(".3GP")) {
                   playList.add(String.format("%d. %s",ii,plyLst));
                   ii=ii+1;
                }
            }
        }

        lVw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos=position;
                btnPlay.performClick();
            }
        });

        if (!playList.isEmpty()){
            ArrayAdapter<String> arAdt = new ArrayAdapter<>(this, R.layout.activity_list_vw, R.id.textView4, playList);
            //ArrayAdapter<String> arAdt = new ArrayAdapter<>(this, R.layout.activity_list_vw, R.id.textView4, fils);
            lVw.setAdapter(arAdt);
        }
        pG = new ProgressDialog(MainActivity.this);
        pG.setTitle("Buka Aplikasi");
        pG.setMessage("Menghitung total durasi...");
        pG.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pG.setCancelable(false);
        pG.show();
        new Thread(new Runnable() {
            @SuppressLint("DefaultLocale")
            public void run() {
                try {
                    int nmbFiles = fils.length;
                    int duras = 0;
                    if (nmbFiles>0) {
                        for (String fil : fils) {
                            if (fil.toUpperCase().endsWith(".MP4") || fil.toUpperCase().endsWith(".3GP")){
                                Uri JovUri = Uri.parse(fldrExtern + File.separator + fil);
                                MediaPlayer mm = MediaPlayer.create(MainActivity.this, JovUri);
                                duras = duras + mm.getDuration();
                                mm.release();
                            }
                        }
                        drt = duras / 1000;
                        hrs = drt / 3600;
                        mnt = (drt / 60) - (hrs * 60);
                        scd = drt - (hrs * 3600) - (mnt * 60);
                        frmtd = String.format("%d:%02d:%02d", hrs, mnt, scd);

                    } else{
                        frmtd="00:00:00";
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
                }

                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        txt3.setText(String.format("Total durasi: %s",frmtd));
                    }
                });
                pG.dismiss();
            }
        }).start();

        btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fldrExtern.exists()) {
                    Toast.makeText(MainActivity.this,"Direktori tidak ada",Toast.LENGTH_LONG).show();
                } else {
                    String[] fils = fldrExtern.list();
                    int numberOfFiles = fils.length;
                    if (numberOfFiles>0)
                    {
                        Intent ient = new Intent(MainActivity.this,PlayVidActivity.class);
                        ient.putExtra("IDXPILIH",pos);
                        startActivity(ient);
                    }
                }
            }
        });
    }
}
