package at.fhooe.mc.android.applicationandroid;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity" ;
    BottomNavigationView mMainNav;
    FrameLayout mMainFrame;

    Main_Promillometer_Start_Screen promillometer_toolbar;
    Main_Calculation_Result_Promillometer berechnen_toolbar;
    Main_Account_Promillometer mainAccount;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);


        //Toolbar unten
        mMainFrame = (FrameLayout) findViewById(R.id.main_frameLayoutA);
        mMainNav = (BottomNavigationView) findViewById(R.id.main_nav_bar);
        berechnen_toolbar = new Main_Calculation_Result_Promillometer();
        promillometer_toolbar = new Main_Promillometer_Start_Screen();
        mainAccount = new Main_Account_Promillometer();


        SharedPreferences mPreferences = getSharedPreferences("myDatabaseAccount", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        String geb = mPreferences.getString(getString(R.string.gebDatum), "0");
        String gew = mPreferences.getString(getString(R.string.gewicht), "0");
        String größ = mPreferences.getString(getString(R.string.größe), "0");

        if (gew.isEmpty() || geb.equals("") || größ.equals("") || mPreferences.getInt("LastClickGenderSpinner", 0) == 0) {
            Toast.makeText(this, "Alle Felder im Tab 'Account' müssen ausgefüllt sein!", Toast.LENGTH_SHORT).show();
            setFragment(mainAccount);
        }else{
            setFragment(berechnen_toolbar);
        }




        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_tool_berechnen:
                        mMainNav.setItemBackgroundResource(R.color.myColorBlack);
                        setFragment(berechnen_toolbar);
                        return true;

                    case R.id.nav_tool_Promillometer:
                    mMainNav.setItemBackgroundResource(R.color.myColorBlack);
                    setFragment(promillometer_toolbar);
                    return true;

                    case R.id.nav_tool_account:
                        mMainNav.setItemBackgroundResource(R.color.myColorBlack);
                        setFragment(mainAccount);
                        return true;

                    default:
                        return false;
                }
            }


        });
    }


    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frameLayoutA, fragment);
        fragmentTransaction.commit();
    }

}
