
package at.krmmr.promillometer;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Main_Calculation_Result_Promillometer extends Fragment implements View.OnClickListener {

    static TextView alcoholLevelTv, soberTimeTv, soberTimeTvHeader;
    Button addBt, clearBt, clearAccountBt;
    Float result;
    static int ButtonClicked = 0, item;
    int lastDrinksListCounter;

    ListView lastDrinksLv;
    static ArrayList<String> arrayList;
    static ArrayAdapter<String> adapter;
    List<SpinnerDataDrinks> drinks;

    Spinner DrinkSpinner, volumeSpinner;




    public Main_Calculation_Result_Promillometer() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View calcView = inflater.inflate(R.layout.fragment_main_calculation, container, false);
        View accountView = inflater.inflate(R.layout.fragment_main_account, container, false);

        //INIT
        clearAccountBt = accountView.findViewById(R.id.button_Account_clear);
        alcoholLevelTv = calcView.findViewById(R.id.berechnung_ausgabe);
        addBt = calcView.findViewById(R.id.button_fragment_calc_hinzufü);
        clearBt = calcView.findViewById(R.id.button_clear_result);
        soberTimeTv = calcView.findViewById(R.id.textview_nuechtern_um);
        soberTimeTvHeader = calcView.findViewById(R.id.textView_nuechternUm_header);
        lastDrinksLv = calcView.findViewById(R.id.listDrinks);
        addBt.setOnClickListener(this);
        clearBt.setOnClickListener(this);


        loadData();
        updateAlcValue();


        adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, R.id.textItemSpinner, arrayList);
        // Add a header to the ListView

        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.listview_header,lastDrinksLv,false);
        lastDrinksLv.addHeaderView(header);


        lastDrinksLv.setAdapter(adapter);
        lastDrinksListCounter = 0;


        SharedPreferences mPreferences = getActivity().getSharedPreferences("myDatabaseAccount", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        alcoholLevelTv.setText(mPreferences.getString("alcoholLevelTv", "0,0"));
        soberTimeTv.setText(mPreferences.getString("soberTimeTv", ""));



        // --------------------------- Drinks Spinner ---------------------------
        // ----------------------------------------------------------------------
        String s = mPreferences.getString("drinks_local", "Please connect to Network=0");
        DrinkSpinner = calcView.findViewById(R.id.spinner_drinks_fragment_calculation);
        drinks = new ArrayList<>();
        s = s.replace("{", "");
        s = s.replace("}", "");
        String[] stringArray = s.split(", ");

        List<Pair> drinkPairs = new ArrayList<Pair>();



        final List<String> name_list = new ArrayList<>();
        for (int i = 0; i < stringArray.length; i++) {
            String[] drink = stringArray[i].split("=");
            String drinkName = drink[0];
            double val = Double.parseDouble(drink[1]);

            Pair pair = new Pair(val,drinkName);
            drinkPairs.add(pair);
        }
        //Sort the drinkList
        Collections.sort(drinkPairs, (a, b) -> {
            double val1 = (double) a.first;
            double val2 = (double) b.first;
            int result = (int) ((val1 - val2)*1000);
            return result;
        });
        for(int i = 0; i < drinkPairs.size(); i++){
            Pair p = drinkPairs.get(i);
            double val = (double) p.first;
            String drinkName = (String) p.second;
            drinks.add(new SpinnerDataDrinks(val, drinkName));
            name_list.add(drinkName);
        }

        //display the drinks in the spinner
        CustomSpinnerAdapterDrinks DrinkSpinnerAdapter =
                new CustomSpinnerAdapterDrinks(getActivity(), R.layout.spinneritemdrinks, drinks);
        DrinkSpinner.setAdapter(DrinkSpinnerAdapter);
        DrinkSpinner.setSelection(mPreferences.getInt("LastClickDrinkSpinner", 0));
        DrinkSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences mPreferences = getActivity().getSharedPreferences("myDatabaseAccount", MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putInt("LastClickDrinkSpinner", position).commit();
                editor.putString("LastClickedDrinkSpinner_Name", name_list.get(position)).commit();
                editor.putFloat("LastClickedDrinkSpinner_Value", (float) drinks.get(position).getDrinkval()).commit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


        // --------------------------- Volume Spinner ---------------------------
        // ----------------------------------------------------------------------
        volumeSpinner = calcView.findViewById(R.id.spinner_volume);
        final List<String> volume = new ArrayList<>();

        volume.add("2 cl");
        volume.add("4 cl");
        volume.add("6 cl");
        volume.add("0.1 l");
        volume.add("0.125 l");
        volume.add("0.2 l");
        volume.add("0.25 l");
        volume.add("0.33 l");
        volume.add("0.5 l");
        volume.add("1 l");

        CustomSpinnerAdapterVolume VolumeSpinnerAdapter =
                new CustomSpinnerAdapterVolume(getActivity(), R.layout.spinneritemvolume, volume);
        volumeSpinner.setAdapter(VolumeSpinnerAdapter);
        volumeSpinner.setSelection(mPreferences.getInt("LastClickVolumeSpinner", 0));
        volumeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences mPreferences = getActivity().getSharedPreferences("myDatabaseAccount", MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putInt("LastClickVolumeSpinner", position).commit();
                double volume = 0;
                switch (position) {
                    case 0:
                        volume = 0.02;
                        break;
                    case 1:
                        volume = 0.04;
                        break;
                    case 2:
                        volume = 0.06;
                        break;
                    case 3:
                        volume = 0.1;
                        break;
                    case 4:
                        volume = 0.125;
                        break;
                    case 5:
                        volume = 0.2;
                        break;
                    case 6:
                        volume = 0.25;
                        break;
                    case 7:
                        volume = 0.33;
                        break;
                    case 8:
                        volume = 0.5;
                        break;
                    case 9:
                        volume = 1;
                        break;
                }
                editor.putFloat("LastClickedVolumeSpinner_Value", (float) volume).commit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return calcView;
    }


    @Override
    public void onResume() {
        super.onResume();
        //refresh Alc Value when reopening the App
        updateAlcValue();
    }

    @Override
    public void onClick(View v) {
        calculateAlcoholLevel(v);
    }

    public void calculateAlcoholLevel(View v) {
        SharedPreferences mPreferences = getActivity().getSharedPreferences("myDatabaseAccount", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        String geb = mPreferences.getString("age", "0");
        String gew = mPreferences.getString("weight", "0");
        String größ = mPreferences.getString("size", "0");

        if (gew.isEmpty() || geb.equals("") || größ.equals("") || mPreferences.getInt("LastClickGenderSpinner", 0) == 0) {
            Toast.makeText(getActivity(), getString(R.string.calculation_everythingmustbefilled), Toast.LENGTH_SHORT).show();



        } else {
            float weight = Float.parseFloat(gew);
            double soberInMinutes, temporaryNumber;
            float AlcoholInGram;

            // -------------------------- Main Calculation --------------------------
            // ----------------------------------------------------------------------
            if (v.getId() == R.id.button_fragment_calc_hinzufü) {
                updateAlcValue();
                editor.putFloat("LastDrinkTime", System.currentTimeMillis()).commit();

                //MALE
                if (mPreferences.getInt("LastClickGenderSpinner", 0) == 1) {
                    float genderMultiplier = 0.68f;
                    AlcoholInGram = (mPreferences.getFloat("LastClickedVolumeSpinner_Value", 0) * 1000) * mPreferences.getFloat("LastClickedDrinkSpinner_Value", 0) * 0.8f;
                    temporaryNumber = AlcoholInGram / (weight * genderMultiplier);
                    result = Math.round(temporaryNumber * 100) / 100.0f;
                }
                //FEMALE
                else if (mPreferences.getInt("LastClickGenderSpinner", 0) == 2) {
                    float genderMultiplier = 0.55f;
                    AlcoholInGram = (mPreferences.getFloat("LastClickedVolumeSpinner_Value", 0) * 1000) * mPreferences.getFloat("LastClickedDrinkSpinner_Value", 0) * 0.8f;
                    temporaryNumber = AlcoholInGram / (weight * genderMultiplier);
                    result = Math.round(temporaryNumber * 100) / 100.0f;
                } else {
                    result = 0.0f;
                }

                // ---------------------------- Save Results ----------------------------
                // ----------------------------------------------------------------------
                if (ButtonClicked == 0 && mPreferences.getInt("buttonState", 0) == 0) {
                    alcoholLevelTv.setText(String.format("%.2g%n", result));
                    editor.putString("alcoholLevelTv", String.format("%.2g%n", result)).commit();
                    editor.putFloat("AlcLevel", result).commit();
                } else {
                    result = mPreferences.getFloat("AlcLevel", 0.0f) + result;
                    alcoholLevelTv.setText(String.format("%.2g%n", result));
                    editor.putString("alcoholLevelTv", String.format("%.2g%n", result)).commit();
                    editor.putFloat("AlcLevel", result).commit();
                }
                soberInMinutes = (result / 0.15) * 60; //time to be sober in minutes
                ButtonClicked++;

                // -------------------------- Print sober Time --------------------------
                // ----------------------------------------------------------------------
                Calendar cal = Calendar.getInstance(); //time now
                cal.add(Calendar.MINUTE, (int) soberInMinutes); //time when you are sober
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy  HH:mm");
                soberTimeTv.setText(dateFormat.format(cal.getTime()));
                editor.putString("soberTimeTv", dateFormat.format(cal.getTime())).commit();


                // ----------------------- Print last Drinks List -----------------------
                // ----------------------------------------------------------------------
                arrayList.add(mPreferences.getString("LastClickedDrinkSpinner_Name", ""));
                adapter.notifyDataSetChanged();
                lastDrinksListCounter++;
                //Save List
                saveData();

            }
        }
        // ------------------- Reset if Button Reset pressed --------------------
        // ----------------------------------------------------------------------
        if (v.getId() == R.id.button_clear_result) {
            reset();
        }
        //save Button State
        editor.putInt("buttonState", ButtonClicked).commit();
        //save AlcLevel
        updateAlcValue();
    }


    public void updateAlcValue() {
        SharedPreferences mPreferences = getActivity().getSharedPreferences("myDatabaseAccount", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        long lastUpdateTime = mPreferences.getLong("lastUpdateTime", System.currentTimeMillis());
        long now = System.currentTimeMillis();
        long timeDifference = (now - lastUpdateTime); // in minutes
       // long timeDifference = ((now - lastUpdateTime) / 1000) / 60; // in minutes
        float alcDifference = (timeDifference * 0.0000000416666f); //how much promille passed since last update
        //float alcDifference = timeDifference * 0.0025f; //how much promille passed since last update
        float oldAlcLevel = mPreferences.getFloat("AlcLevel", 0);
        if(oldAlcLevel - alcDifference <=0){
            editor.putFloat("AlcLevel", 0.0f).commit();
            soberTimeTv.setText("");
            editor.putString("soberTimeTv", "").commit();
        } else {
            editor.putFloat("AlcLevel", oldAlcLevel - alcDifference).commit();
        }
        if(mPreferences.getString("soberTimeTv","").equals("")){
            soberTimeTvHeader.setText("");
        } else {
            soberTimeTvHeader.setText("Nüchtern um");
        }

        Float Result = mPreferences.getFloat("AlcLevel", 0);
        alcoholLevelTv.setText(String.format("%.2g", Result)+ " ‰");
        String Time = mPreferences.getString("soberTimeTv", "");
        soberTimeTv.setText(Time);
        editor.putLong("lastUpdateTime", System.currentTimeMillis()).commit();

        if(mPreferences.getFloat("AlcLevel",0) >= 0.6){
            Toast.makeText(getActivity(), getString(R.string.calculation_attention), Toast.LENGTH_LONG).show();
        }

    }

    public void saveData() {
        SharedPreferences mPreferences = getActivity().getSharedPreferences("myDatabaseAccount", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        editor.putString("listAlcohol", json).apply();
    }

    public void loadData() {
        SharedPreferences mPreferences = getActivity().getSharedPreferences("myDatabaseAccount", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPreferences.getString("listAlcohol", null);
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        arrayList = gson.fromJson(json, type);
        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }
    }

    public void reset() {
        SharedPreferences mPreferences = getActivity().getSharedPreferences("myDatabaseAccount", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        //clear TextViews
        ButtonClicked = 0;
        alcoholLevelTv.setText("");
        editor.putString("alcoholLevelTv", "").commit();
        soberTimeTv.setText("");
        editor.putString("soberTimeTv", "").commit();
        editor.putFloat("AlcLevel", 0).commit();

        //clear lastDrinks
        for (item = 0; item < arrayList.size(); item++) {
            adapter.remove(arrayList.get(item));
            adapter.notifyDataSetChanged();
            item--;
        }
        saveData();
    }
}