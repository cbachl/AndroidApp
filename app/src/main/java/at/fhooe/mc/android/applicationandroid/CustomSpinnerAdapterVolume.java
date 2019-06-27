package at.fhooe.mc.android.applicationandroid;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomSpinnerAdapterVolume extends ArrayAdapter<String> {

    private Context context;
    private List<String> volume;


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return myCustomSpinnerView(position,convertView,parent);}

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return myCustomSpinnerView(position,convertView,parent);
    }

    public CustomSpinnerAdapterVolume(@NonNull Context context, int resource, List<String>volume) {
        super(context, resource,volume);
        this.context = context;
        this.volume = volume;


    }

    private View myCustomSpinnerView(int position, @Nullable View myView ,@NonNull ViewGroup parent){
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.spinneritemvolume,parent,false);
        TextView textView = (TextView)customView.findViewById(R.id.textItemSpinner);
        textView.setText(volume.get(position));



        return  customView;
    }
}

