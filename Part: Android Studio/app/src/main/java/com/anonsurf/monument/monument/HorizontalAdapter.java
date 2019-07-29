package com.anonsurf.monument.monument;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.anonsurf.monument.R;

import java.util.ArrayList;

public class HorizontalAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MonumentModel> listP;

    public HorizontalAdapter(Context context, ArrayList<MonumentModel> listP) {
        this.context = context;
        this.listP = listP;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return listP.size();
    }

    @Override
    public Object getItem(int position) {
        return listP.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_item_for_horizontal_list, null, true);

            holder.vh_id = convertView.findViewById(R.id.idP);
            holder.vh_title = convertView.findViewById(R.id.titleP);
            holder.vh_image = convertView.findViewById(R.id.imageP);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.vh_id.setText(String.valueOf(listP.get(position).getId()));
        holder.vh_title.setText(listP.get(position).getTitle());
        holder.vh_image.setImageBitmap(listP.get(position).getBmp());

        return convertView;
    }

    private class ViewHolder {
        private TextView vh_id;
        private TextView vh_title;
        private ImageView vh_image;
    }
}
