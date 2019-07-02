package at.fhooe.mc.android.applicationandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;


public class Splash_Screen_Welcome extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String logTag = "Promillometer";


    ImageView beer_stroke_one, beer_stroke_two, beer_stroke_three, beer_stroke_four,beer_handle, beer_foam;
    ImageView label;
    Animation fromtop1,fromtoptxt,fromtop2,fromtop3,fromtop,fromtop4,fromtop5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);


        label = (ImageView) findViewById(R.id.welcome_screen_label);

        beer_stroke_one = (ImageView)findViewById(R.id.welcome_screen_beerStrokeOne);
        beer_stroke_two = (ImageView)findViewById(R.id.welcome_screen_beerStrokeTwo);
        beer_stroke_three = (ImageView)findViewById(R.id.welcome_screen_beerStrokeThree);
        beer_stroke_four = (ImageView)findViewById(R.id.welcome_screen_beerStrokeFour);
        beer_handle = (ImageView)findViewById(R.id.welcome_screen_beerHandle);
        beer_foam = (ImageView)findViewById(R.id.welcome_screen_beerFoam);



        fromtoptxt = AnimationUtils.loadAnimation(this,R.anim.head_promillometer_text_icon);

        fromtop =AnimationUtils.loadAnimation(this,R.anim.stroke_one_icon);
        fromtop1 =AnimationUtils.loadAnimation(this,R.anim.stroke_two_icon);
        fromtop2 =AnimationUtils.loadAnimation(this,R.anim.strocke_three_icon);
        fromtop3 =AnimationUtils.loadAnimation(this,R.anim.handle_icon);
        fromtop4 =AnimationUtils.loadAnimation(this,R.anim.foam_icon);
        fromtop5 = AnimationUtils.loadAnimation(this,R.anim.stroke_four_icon);


        label.setAnimation(fromtoptxt);
        beer_stroke_one.setAnimation(fromtop);
        beer_stroke_two.setAnimation(fromtop1);
        beer_stroke_three.setAnimation(fromtop2);
        beer_handle.setAnimation(fromtop3);
        beer_foam.setAnimation(fromtop4);
        beer_stroke_four.setAnimation(fromtop5);



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
