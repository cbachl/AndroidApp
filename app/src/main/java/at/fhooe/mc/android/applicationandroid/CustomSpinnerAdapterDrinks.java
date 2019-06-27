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

public class CustomSpinnerAdapterDrinks extends ArrayAdapter<SpinnerDataDrinks> {

    private Context context;
    private List<SpinnerDataDrinks> spinnerDataDrinks;


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return myCustomSpinnerView(position,convertView,parent);}

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return myCustomSpinnerView(position,convertView,parent);
    }

    public CustomSpinnerAdapterDrinks(@NonNull Context context, int resource, List<SpinnerDataDrinks>spinnerDataDrinks) {
        super(context, resource,spinnerDataDrinks);
        this.context = context;
        this.spinnerDataDrinks = spinnerDataDrinks;


    }

    private View myCustomSpinnerView(int position, @Nullable View myView ,@NonNull ViewGroup parent){
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.spinneritemdrinks,parent,false);
        TextView textView = (TextView)customView.findViewById(R.id.textItemSpinner);
        textView.setText(spinnerDataDrinks.get(position).getDrinkName());



        return  customView;
    }
}

