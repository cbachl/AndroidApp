package at.krmmr.promillometer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;


public class Splash_Screen_Welcome extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String logTag = "Promillometer";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);


        //set status bar color
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.mySplashScreenColor));



        //set splashscreen video
        VideoView view = (VideoView)findViewById(R.id.VideoView);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.splashscreenblack;
        view.setVideoURI(Uri.parse(path));
        view.start();



        SharedPreferences mPreferences = getSharedPreferences("myDatabaseAccount", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        long lastUpdate = mPreferences.getLong("lastFirebaseUpdate", System.currentTimeMillis());
        long now = System.currentTimeMillis();

        boolean firstRun = mPreferences.getBoolean("firstRun",true);

        if(firstRun == true || now - lastUpdate >= 86400000) { //update only every day


            DocumentReference docRef = db.collection("all").document("drinks");
            Source source = Source.SERVER;
            docRef.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        // Document found in the offline cache
                        DocumentSnapshot document = task.getResult();
                        Log.d(logTag, "Cached document data: " + document.getData());
                        String s = "" + document.getData();
                        SharedPreferences prefs = getSharedPreferences("myDatabaseAccount", MODE_PRIVATE);
                        SharedPreferences.Editor editor = getSharedPreferences("myDatabaseAccount", MODE_PRIVATE).edit();
                        editor.putString("drinks_local", s).commit();
                        editor.putBoolean("firstRun", false).commit();
                        editor.putLong("lastFirebaseUpdate", System.currentTimeMillis()).commit();

                    } else {
                        Log.d(logTag, "Cached get failed: ", task.getException());
                    }
                }
            });


        }








        //to Activity_Main
        final Intent i = new Intent(this,MainActivity.class);

        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(5000);

                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };

        timer.start();

    }


}
