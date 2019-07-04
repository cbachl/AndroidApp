package at.krmmr.promillometer;



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
    EditText mGeburtstag,mGröße, mGewicht ;
    private Button btnSave,btnclear;
    TextView ergebnis;
    Spinner GenderSpinner;


    public Main_Account_Promillometer() { }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        mPreferences = getActivity().getSharedPreferences("myDatabaseAccount", MODE_PRIVATE);
        mEditor = mPreferences.edit();
        int LastClickGenderSpinner = mPreferences.getInt("LastClickGenderSpinner",0);


        GenderSpinner = (Spinner)view.findViewById(R.id.spinner_male_female);

        final List<SpinnerData>CustomList= new ArrayList<>();
        CustomList.add(new SpinnerData(R.drawable.gender_spinner_blank,getString(R.string.account_choosegender)));
        CustomList.add(new SpinnerData(R.drawable.gender_spinner_male,getString(R.string.account_gender_male)));
        CustomList.add(new SpinnerData(R.drawable.gender_spinner_female,getString(R.string.account_gender_female)));

        CustomSpinnerAdapter customSpinnerAdapter =
                new CustomSpinnerAdapter(getActivity(),R.layout.spinneritem,CustomList);

        GenderSpinner.setAdapter(customSpinnerAdapter);
        saveSpinnerGender(LastClickGenderSpinner);

        checkSharedPreferences();
        return view;
    }

    private void checkSharedPreferences(){
        mGeburtstag.setText(mPreferences.getString("age", ""));
        mGewicht.setText(mPreferences.getString("weight", ""));
        mGröße.setText(mPreferences.getString("size", ""));
    }
    @Override
    public void onClick(View v){
        if(v.getId()==R.id.button) { //save
            Toast.makeText(getActivity(),getString(R.string.data_saved),Toast.LENGTH_SHORT).show();
            mEditor.putString("age", mGeburtstag.getText().toString()).commit();
            mEditor.putString("weight", mGewicht.getText().toString()).commit();
            mEditor.putString("size", mGröße.getText().toString()).commit();
            checkSharedPreferences();
        }else if (v.getId() == R.id.button_Account_clear){ //clear all
            mEditor.putString("age", "").commit();
            mEditor.putString("weight", "").commit();
            mEditor.putString("Size", "").commit();
            saveSpinnerGender(0);
            checkSharedPreferences();
        }
    }
    public void saveSpinnerGender(int LastClickGenderSpinner){

        GenderSpinner.setSelection(LastClickGenderSpinner);
        GenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mEditor.putInt("LastClickGenderSpinner",position).commit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        checkSharedPreferences();
    }
}