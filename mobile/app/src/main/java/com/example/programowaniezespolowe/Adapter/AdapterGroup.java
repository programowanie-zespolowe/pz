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

import com.example.programowaniezespolowe.Data.Group;
import com.example.programowaniezespolowe.R;

import java.util.ArrayList;

public class AdapterGroup extends BaseAdapter implements Filterable {
    private Context context;
    private ArrayList<Group> groups;
    private ArrayList<Group> filterList;
    private CustomFilter filter;

    public AdapterGroup(Context context, ArrayList<Group> groups) {
        this.context = context;
        this.groups = groups;
        this.filterList=groups;
    }

    @Override
    public int getCount() {
       return groups.size();
    }

    @Override
    public Object getItem(int position) {
        return groups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.category_raw, parent, false);
        }
        byte[] decodeString = Base64.decode(groups.get(position).getImageGroup(), Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        ImageView imageView = convertView.findViewById(R.id.iconCategory);
        imageView.setImageBitmap(bmp);
        TextView textView = convertView.findViewById(R.id.categoryName);
        textView.setText(groups.get(position).getNameGroup());
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
                ArrayList<Group> filter = new ArrayList<>();

                for(int i = 0; i < filterList.size(); i++){
                    if(filterList.get(i).getNameGroup().toUpperCase().contains(constraint)){
                        Group g = new Group(filterList.get(i).getImageGroup(), filterList.get(i).getNameGroup());
                        filter.add(g);
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
            groups= (ArrayList<Group>) results.values;
            notifyDataSetChanged();
        }
    }
}
