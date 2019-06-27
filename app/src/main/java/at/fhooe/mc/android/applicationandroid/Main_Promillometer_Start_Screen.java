package at.fhooe.mc.android.applicationandroid;


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

import me.aflak.arduino.Arduino;
import me.aflak.arduino.ArduinoListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class Main_Promillometer_Start_Screen extends Fragment implements View.OnClickListener, ArduinoListener {

    TextView tv_connected;
    TextView tv_value;
    Button b;
    Arduino arduino;
    boolean connected;

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
        tv_connected.setText("Verbunden: NEIN");
        tv_value = view.findViewById(R.id.promillometer_value_Tv);

        if(connected){

        }else {
            Toast.makeText(getActivity(), "Stecke den Promillometer in dein Smartphone", Toast.LENGTH_LONG).show();
        }

        b = view.findViewById(R.id.promillometer_restart_Bt);
        b.setOnClickListener(this);
        b = view.findViewById(R.id.promillometer_save_Bt);
        b.setOnClickListener(this);


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_promillometer, container, false);
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
        tv_connected.setText("Verbunden: JA");
        connected = true;

    }

    @Override
    public void onArduinoDetached() {
        tv_connected.setText("Verbunden: NEIN");
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
        Toast.makeText(getActivity(),"Du musst die Erlaubnis erteilen, \nsonst kann die App nicht funktionieren... \nNeue Anfrage in 3 Sekunden",Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                arduino.reopen();
            }
        }, 3000);
    }




    public void display(final String message){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_value.setText(message+"\n");
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case (R.id.promillometer_restart_Bt):{
                byte[] b = {0,0,0}; //000 Code for Restart
                arduino.send(b);
            }
        }
    }











}
