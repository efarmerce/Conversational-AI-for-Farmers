package e.chandrakumar.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import pl.droidsonroids.gif.GifImageView;

public class GifActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);
        GifImageView imageView=findViewById(R.id.gifImage);
        ActionBar toolbar = getSupportActionBar();
        toolbar.setTitle("Rainfall Analysis Theni District");
    }

}
