package com.beone.flagggaming;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.beone.flagggaming.usuario.LoginActivity;

public class MainActivity extends AppCompatActivity {

    VideoView vwLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        vwLogo =findViewById(R.id.vwLogo);
        //Codigo para que no haya un fondo negro antes de iniciarse el video
        vwLogo.setZOrderOnTop(true);
        //Ruta video logo animado
        String path = "android.resource://" + getPackageName() + "/" + R.raw.logoanimado;
        vwLogo.setVideoURI(Uri.parse(path));
        //Ejecuto el video del logo animado
        vwLogo.start();

        Thread thread = new Thread(){
            @Override
            public void run(){
                try{
                    sleep(5000);
                } catch (Exception e){
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
        thread.start();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (vwLogo.isPlaying()) {
            vwLogo.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        vwLogo.stopPlayback();
    }
}