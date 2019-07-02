package at.fhooe.mc.android.applicationandroid;



import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Main_Account_Promillometer extends Fragment implements View.OnClickListener{


    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    EditText mGeburtstag;
    EditText mGröße, mGewicht ;
    private Button btnSave,btnclear;

    TextView ergebnis;
    Spinner GenderSpinner;






    public Main_Account_Promillometer() {


    }




    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {





        //Shared Preferences
        View view = inflater.inflate(R.layout.fragment_main_account, container, false);
        final  View view2 = inflater.inflate(R.layout.fragment_main_calculation, container,false);


        mGeburtstag = (EditText) view.findViewById(R.id.get_text_geburtstag_fragment);
        mGewicht = (EditText) view.findViewById(R.id.get_text_gewicht_fregment);
        mGröße = (EditText) view.findViewById(R.id.get_text_größe_fregment);
        btnSave = (Button) view.findViewById(R.id.button);
        btnclear = (Button) view.findViewById(R.id.button_Account_clear);

        ergebnis = (TextView)view2.findViewById(R.id.berechnung_ausgabe);
        btnSave.setOnClickListener(this);
        btnclear.setOnClickListener(this);

        //Spinner
        //SAVE
        mPreferences = getActivity().getSharedPreferences("myDatabaseAccount", MODE_PRIVATE);
        mEditor = mPreferences.edit();
        //POSITION BEKOMMEN
        int LastClickGenderSpinner = mPreferences.getInt("LastClickGenderSpinner",0);


        GenderSpinner = (Spinner)view.findViewById(R.id.spinner_male_female);

        final List<SpinnerData>CustomList= new ArrayList<>();
        CustomList.add(new SpinnerData(R.drawable.gender_spinner_blank,"Auswählen"));
        CustomList.add(new SpinnerData(R.drawable.gender_spinner_male,"Männlich"));
        CustomList.add(new SpinnerData(R.drawable.gender_spinner_female,"Weiblich"));

        CustomSpinnerAdapter customSpinnerAdapter =
                new CustomSpinnerAdapter(getActivity(),R.layout.spinneritem,CustomList);

        GenderSpinner.setAdapter(customSpinnerAdapter);

        //SAVEEEEEE
        saveSpinnerGender(LastClickGenderSpinner);
        ///SPINNER ENDE




        checkSharedPreferences();




        return view;
    }

    private void checkSharedPreferences(){

        String gebDatum = mPreferences.getString(getString(R.string.gebDatum), "");
        String gewicht = mPreferences.getString(getString(R.string.gewicht), "");
        String größe = mPreferences.getString(getString(R.string.größe), "");


        mGeburtstag.setText(gebDatum);
        mGewicht.setText(gewicht);
        mGröße.setText(größe);



    }
    @Override
    public void onClick(View v){
        if(v.getId()==R.id.button) {

            Toast.makeText(getActivity(),"Daten gespeichert!",Toast.LENGTH_SHORT).show();

            //save geb
            String geburtsdatum = mGeburtstag.getText().toString();
            mEditor.putString(getString(R.string.gebDatum), geburtsdatum);
            mEditor.commit();

            //save gewicht
            String gewicht = mGewicht.getText().toString();
            mEditor.putString(getString(R.string.gewicht), gewicht);
            mEditor.commit();

            //save größe
            String größe = mGröße.getText().toString();
            mEditor.putString(getString(R.string.größe), größe);
            mEditor.commit();
            checkSharedPreferences();




        }else if (v.getId()==R.id.button_Account_clear){


            //save geb
            mEditor.putString(getString(R.string.gebDatum), "");
            mEditor.commit();

            //save gewicht
            mEditor.putString(getString(R.string.gewicht), "");
            mEditor.commit();

            //save größe
            mEditor.putString(getString(R.string.größe), "");
            mEditor.commit();

            saveSpinnerGender(0);

            checkSharedPreferences();

           // Main_Calculation_Result_Promillometer.reset();

        }


    }
    public void saveSpinnerGender(int LastClickGenderSpinner){

        GenderSpinner.setSelection(LastClickGenderSpinner);
        GenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //SAVEEEEE

                mEditor.putInt("LastClickGenderSpinner",position).commit();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();
        checkSharedPreferences();
    }
}