package e.chandrakumar.myapplication;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private ActionBar toolbar;
    private Context context;
    StorageReference reference;
    BottomNavigationView navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = getSupportActionBar();
        context=getApplicationContext();
         navigation= (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        updateUI(false);

        toolbar.setTitle("e-farmerce");

        downloadFile("final.zip");
    }
    private void updateUI(boolean showNavigation){
        if(showNavigation){
            navigation.setVisibility(View.VISIBLE);
        }
        else {
            navigation.setVisibility(View.INVISIBLE);
        }
    }
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    File zipDir= null;
    private void downloadFile(String fileName){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();
        if(isExternalStorageWritable()) {
            String root = Environment.getExternalStorageDirectory().toString();

            try {
                zipDir = File.createTempFile("final","zip");
                final StorageReference imageRef = storageRef.child(fileName);
                imageRef.getFile(zipDir).addOnSuccessListener(
                        new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(context,
                                        "Downloaded the file",
                                        Toast.LENGTH_SHORT).show();
                                Decompress d = new Decompress(zipDir.getAbsolutePath(), zipDir.getAbsolutePath());
                                d.unzip();
                                Fragment fragment;
                                toolbar.setTitle("Ground data");
                                fragment = new UserInputFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("unziped", zipDir.getAbsolutePath());
                                fragment.setArguments(bundle);
                                loadFragment(fragment);
                                updateUI(true);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(context,
                                "Couldn't be downloaded",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            Bundle bundle;
            switch (item.getItemId()) {
                case R.id.navigation_ground_data:
                    toolbar.setTitle("Ground data");
                    fragment = new UserInputFragment();
                    bundle = new Bundle();
                    bundle.putString("unziped", zipDir.getAbsolutePath());
                    fragment.setArguments(bundle);
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_satellite:
                    toolbar.setTitle("From satellite");
                    fragment = new WorldWindFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_analysis:
                    toolbar.setTitle("Voice Assistant");
                    fragment=new VoiceAssistantFragment();
                    bundle = new Bundle();
                    bundle.putString("unziped", zipDir.getAbsolutePath());
                    fragment.setArguments(bundle);
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
