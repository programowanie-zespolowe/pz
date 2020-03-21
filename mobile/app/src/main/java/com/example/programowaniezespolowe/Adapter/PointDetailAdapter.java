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

import com.example.programowaniezespolowe.Data.PointDetail;
import com.example.programowaniezespolowe.R;

import java.util.ArrayList;

public class PointDetailAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private ArrayList<PointDetail> pointDetailList;
    private ArrayList<PointDetail> filterList;
    private CustomFilter filter;

    public PointDetailAdapter(Context context, ArrayList<PointDetail> pointDetailList) {
        this.context = context;
        this.pointDetailList = pointDetailList;
        this.filterList=pointDetailList;
    }

    @Override
    public int getCount() {
        return pointDetailList.size();
    }

    @Override
    public Object getItem(int position) {
        return pointDetailList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.point_detail_raw,parent, false);
        }
        byte[] decodeString;
        if(pointDetailList.get(0).getIdGroup() == 5) {
            decodeString = Base64.decode(pointDetailList.get(1).getImagePoint(), Base64.DEFAULT);
        }else{
            decodeString = Base64.decode(pointDetailList.get(0).getImagePoint(), Base64.DEFAULT);
        }
//        decodeString = Base64.decode(pointDetailList.get(position).getImagePoint(), Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        ImageView imageView = convertView.findViewById(R.id.iconPoint);
        imageView.setImageBitmap(bmp);
        TextView textView = convertView.findViewById(R.id.pointName);
        textView.setText(pointDetailList.get(position).getNamePoint());
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

            if(constraint != null && constraint.length()  > 0){
                constraint = constraint.toString().toUpperCase();
                ArrayList<PointDetail> filter = new ArrayList<>();

                for(int i = 0; i < filterList.size(); i++){
                    if(filterList.get(i).getNamePoint().toUpperCase().contains(constraint)){
                        PointDetail p = new PointDetail(filterList.get(0).getNamePoint(), filterList.get(i).getImagePoint());
                        filter.add(p);
                    }
                }
                results.count = filter.size();
                results.values = filter;
            }else {
                results.count = filterList.size();
                results.values = filterList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            pointDetailList = (ArrayList<PointDetail>) results.values;
            notifyDataSetChanged();
        }
    }
}
