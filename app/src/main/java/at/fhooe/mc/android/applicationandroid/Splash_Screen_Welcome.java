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


    ImageView beerstrich1,beerstrich2,beerstrich3,beerhenkel,beerschaum;
    ImageView ueberschrift;
    Animation fromtop1,fromtoptxt,fromtop2,fromtop3,fromtop,fromtop4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);


        ueberschrift = (ImageView) findViewById(R.id.welcome_screen_ueberschrift);

        beerstrich1 = (ImageView)findViewById(R.id.welcome_screen_beerStrich1);
        beerstrich2 = (ImageView)findViewById(R.id.welcome_screen_beerStrich2);
        beerstrich3 = (ImageView)findViewById(R.id.welcome_screen_beerStrich3);
        beerhenkel = (ImageView)findViewById(R.id.welcome_screen_bierHenkel);
        beerschaum = (ImageView)findViewById(R.id.welcome_screen_bierschaum);



        fromtoptxt = AnimationUtils.loadAnimation(this,R.anim.head_promillometer_text_icon);

        fromtop =AnimationUtils.loadAnimation(this,R.anim.stroke_one_icon);
        fromtop1 =AnimationUtils.loadAnimation(this,R.anim.stroke_two_icon);
        fromtop2 =AnimationUtils.loadAnimation(this,R.anim.strocke_three_icon);
        fromtop3 =AnimationUtils.loadAnimation(this,R.anim.handle_icon);
        fromtop4 =AnimationUtils.loadAnimation(this,R.anim.foam_icon);


        ueberschrift.setAnimation(fromtoptxt);
        beerstrich1.setAnimation(fromtop);
        beerstrich2.setAnimation(fromtop1);
        beerstrich3.setAnimation(fromtop2);
        beerhenkel.setAnimation(fromtop3);
        beerschaum.setAnimation(fromtop4);



//Firebase Test

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
                    editor.putString("drinks_local", s);
                    editor.commit();
                } else {
                    Log.d(logTag, "Cached get failed: ", task.getException());
                }
            }
        });










        //to Activity_Main
        final Intent i = new Intent(this,MainActivity.class);

        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(4000);

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
