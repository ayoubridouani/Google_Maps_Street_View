package com.anonsurf.monument.monument;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.anonsurf.monument.databinding.RowItemForVerticalListBinding;

import java.util.ArrayList;

public class VerticalAdapter extends ArrayAdapter<MonumentModel> implements Filterable {

    private Context context;
    private int ressource;
    private ArrayList<MonumentModel> listP;
    private ArrayList<MonumentModel> mData;
    private ValueFilter valueFilter;
    private RowItemForVerticalListBinding rowItemForVerticalList;
    LayoutInflater inflater;

    public VerticalAdapter(Context context, int ressource, ArrayList<MonumentModel> listP) {
        super(context,ressource,listP);
        this.context = context;
        this.ressource = ressource;
        this.listP = listP;
        this.mData = listP;
    }

    @Override
    public int getCount() {
        return listP.size();
    }

    @Override
    public MonumentModel getItem(int position) {
        return listP.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VerticalAdapter.ViewHolder holder;

        holder = new VerticalAdapter.ViewHolder();
        if (inflater == null) inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        rowItemForVerticalList = DataBindingUtil.inflate(inflater, ressource, parent, false);

        holder.vh_id = rowItemForVerticalList.idP;
        holder.vh_title = rowItemForVerticalList.titleP;
        holder.vh_about = rowItemForVerticalList.aboutP;
        holder.vh_image = rowItemForVerticalList.imageP;

        holder.vh_id.setText(String.valueOf(listP.get(position).getId()));
        holder.vh_title.setText(listP.get(position).getTitle());
        holder.vh_about.setText(listP.get(position).getAbout());
        holder.vh_image.setImageBitmap(listP.get(position).getBmp());

        return rowItemForVerticalList.getRoot();
    }

    private class ViewHolder {
        private TextView vh_id;
        private TextView vh_title;
        private TextView vh_about;
        private ImageView vh_image;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<MonumentModel> filterList = new ArrayList<>();
                for (int i = 0; i < mData.size(); i++) {
                    if ((mData.get(i).getTitle().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(mData.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mData.size();
                results.values = mData;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,FilterResults results) {
            listP = (ArrayList<MonumentModel>) results.values;
            notifyDataSetChanged();
        }
    }
}
