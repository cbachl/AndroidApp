package at.fhooe.mc.android.applicationandroid;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<SpinnerData> {

    private Context context;
    private List<SpinnerData> spinnerData;


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return myCustomSpinnerView(position,convertView,parent);}

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return myCustomSpinnerView(position,convertView,parent);
    }

    public CustomSpinnerAdapter(@NonNull Context context, int resource, List<SpinnerData>spinnerData) {
        super(context, resource,spinnerData);
        this.context = context;
        this.spinnerData = spinnerData;


    }

    private View myCustomSpinnerView(int position, @Nullable View myView ,@NonNull ViewGroup parent){
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.spinneritem,parent,false);
        TextView textView = (TextView)customView.findViewById(R.id.textItemSpinner);
        ImageView imageView = (ImageView)customView.findViewById(R.id.imageItemSpinner);
        textView.setText(spinnerData.get(position).getIconName());
        imageView.setImageResource(spinnerData.get(position).getIcon());


        return  customView;
    }
}

