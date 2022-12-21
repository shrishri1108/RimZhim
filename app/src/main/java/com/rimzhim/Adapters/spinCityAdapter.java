package com.rimzhim.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rimzhim.ModelClasses.StateModel.CitiesItem;

import java.util.ArrayList;


public class spinCityAdapter extends ArrayAdapter {

    // Your sent context
    private Context context;
    private final int textViewResourceId;
    private final ArrayList<CitiesItem> list;
    private Boolean hideTop;
    // Your custom values for the spinner (Staff)


    public spinCityAdapter(@NonNull Context context, int resource, Context context1, int textViewResourceId, ArrayList<CitiesItem> list, Boolean hideTop) {
        super(context, resource);
        this.context = context1;
        this.textViewResourceId = textViewResourceId;
        this.list = list;
        this.hideTop = hideTop;
    }

    @Override
    public int getCount(){
        return list.size();
    }

    @Override
    public CitiesItem getItem(int position){
        return list.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item

        @SuppressLint("ViewHolder") View view= LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item,parent,false);
        TextView label;
        label=view.findViewById(android.R.id.text1);
        label.setTextColor(Color.BLACK);
        label.setText(list.get(position).getName());
       /* if (hideTop){
            if (position==0){
                view.setLayoutParams(new AbsListView.LayoutParams(-1,0));
                view.setVisibility(View.GONE);
            }
        }*/

        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        // label.setText(list.get(position).getName());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(list.get(position).getName());
        return label;
    }
}
