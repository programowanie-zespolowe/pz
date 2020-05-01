package com.example.programowaniezespolowe.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.programowaniezespolowe.Data.Building;
import com.example.programowaniezespolowe.R;

import java.util.ArrayList;

public class BuildingAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private ArrayList<Building> buildings;
    private ArrayList<Building> filterList;
    private CustomFilter filter;

    public BuildingAdapter(Context context, ArrayList<Building> buildings) {
        this.context = context;
        this.buildings = buildings;
        this.filterList = buildings;
    }

    @Override
    public int getCount() {
        return buildings.size();
    }

    @Override
    public Object getItem(int position) {
        return buildings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.building_raw, parent, false);
        }
        byte[] decodeString = Base64.decode(buildings.get(position).getImageBuilding(), Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        ImageView imageView = convertView.findViewById(R.id.buildingImage);
        imageView.setImageBitmap(bmp);
        TextView textView = convertView.findViewById(R.id.buildingName);
        textView.setText(buildings.get(position).getNameBuilding());
        return convertView;
    }

    @Override
    public Filter getFilter() {

        if(filter == null){
            filter = new CustomFilter();
        }
        return filter;
    }
    class CustomFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if(constraint != null && constraint.length() > 0){
                constraint = constraint.toString().toUpperCase();
                ArrayList<Building> filter = new ArrayList<>();

                for(int i = 0; i < filterList.size(); i++){
                    if(filterList.get(i).getNameBuilding().toUpperCase().contains(constraint)){
                        Building b = new Building(filterList.get(i
                        ).getImageBuilding(), filterList.get(i).getNameBuilding());
                        filter.add(b);
                    }
                }
                results.count = filter.size();
                results.values=filter;
            }else {
                results.count = filterList.size();
                results.values=filterList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            buildings= (ArrayList<Building>) results.values;
            notifyDataSetChanged();
        }
    }
}
