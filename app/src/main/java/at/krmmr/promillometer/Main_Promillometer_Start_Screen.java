package at.krmmr.promillometer;


import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.aflak.arduino.Arduino;
import me.aflak.arduino.ArduinoListener;

import static android.content.Context.MODE_PRIVATE;
import static at.krmmr.promillometer.Main_Calculation_Result_Promillometer.soberTimeTv;


/**
 * A simple {@link Fragment} subclass.
 */
public class Main_Promillometer_Start_Screen extends Fragment implements View.OnClickListener, ArduinoListener {

    TextView tv_connected, tv_value;
    Button b;
    Arduino arduino;
    boolean connected;
    String msg;

    public Main_Promillometer_Start_Screen() {
        // Required empty public constructor
        //Toolbar oben
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_promillometer, container, false);


        arduino = new Arduino(getActivity());
        arduino.addVendorId(6790);

        tv_connected = view.findViewById(R.id.promillometer_connected_Tv);
        tv_connected.setText(getString(R.string.promillometer_connected_no));
        tv_value = view.findViewById(R.id.promillometer_value_Tv);
        tv_value.setText("");

        if(connected){

        }else {
            Toast.makeText(getActivity(), getString(R.string.promillometer_pleaseconnect), Toast.LENGTH_LONG).show();
        }
        b = view.findViewById(R.id.promillometer_restart_Bt);
        b.setOnClickListener(this);
        b = view.findViewById(R.id.promillometer_save_Bt);
        b.setOnClickListener(this);
        return view;
    }






    @Override
    public void onStart() {
        super.onStart();
        arduino.setArduinoListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        arduino.unsetArduinoListener();
        arduino.close();
    }

    @Override
    public void onArduinoAttached(UsbDevice device) {
        arduino.open(device);
    }

    @Override
    public void onArduinoOpened() {
        tv_connected.setText(getString(R.string.promillometer_connected_yes));
        connected = true;

    }

    @Override
    public void onArduinoDetached() {
        tv_connected.setText(getString(R.string.promillometer_connected_no));
        tv_value.setText("");
        connected = false;
        arduino.close();
    }

    @Override
    public void onArduinoMessage(byte[] bytes) {
        final String s = new String(bytes);
        display(s);

    }

    @Override
    public void onUsbPermissionDenied() {
        Toast.makeText(getActivity(),getString(R.string.promillometer_permission_retry),Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                arduino.reopen();
            }
        }, 3000);
    }


    public void display(final String message){

        SharedPreferences mPreferences = getActivity().getSharedPreferences("myDatabaseAccount", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(message.startsWith("_Val:")) {
                    msg = message.substring(5);
                    float alcVal = Float.parseFloat(msg);
                    editor.putFloat("AlcLevel_Promillometer", alcVal).commit();
                    tv_value.setText(msg + getString(R.string.promillometer_promille));
                }
                else if(message.startsWith("_Preheat:")){
                    msg = message.substring(9);
                    tv_value.setText(getString(R.string.promillometer_preheating) + msg + " " + getString(R.string.seconds) + "\n");
                }
                else if(message.startsWith("_Blow:")){
                    msg = message.substring(6);
                    tv_value.setText(getString(R.string.promillometer_pleaseblow) + msg + getString(R.string.promillometer_pleaseblowseconds));
                }else {
                    tv_value.setText(getString(R.string.promillometer_undefinedstatus) + message);
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        SharedPreferences mPreferences = getActivity().getSharedPreferences("myDatabaseAccount", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        switch(view.getId()){
            case (R.id.promillometer_restart_Bt):{
                byte[] b = {0,0,0}; //000 Code for Restart
                arduino.send(b);
            }
            case (R.id.promillometer_save_Bt):{
                editor.putFloat("AlcLevel", mPreferences.getFloat("AlcLevel_Promillometer", 0)).commit();
                editor.putLong("lastUpdateTime", System.currentTimeMillis()).commit();
                double soberInMinutes = (mPreferences.getFloat("AlcLevel_Promillometer",0) / 0.15) * 60;
                Calendar cal = Calendar.getInstance(); //time now
                cal.add(Calendar.MINUTE, (int) soberInMinutes); //time when you are sober
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy  HH:mm");
                soberTimeTv.setText(dateFormat.format(cal.getTime()));
                editor.putString("soberTimeTv", dateFormat.format(cal.getTime())).commit();
            }

        }
    }
}
