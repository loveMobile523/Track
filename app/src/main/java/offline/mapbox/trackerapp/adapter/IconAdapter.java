package offline.mapbox.trackerapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import offline.mapbox.trackerapp.R;
import offline.mapbox.trackerapp.model.DBIcon;

public class IconAdapter extends ArrayAdapter<DBIcon> {
    LayoutInflater flater;

    public IconAdapter(Activity context, int resouceId, int textviewId, List<DBIcon> list){

        super(context, resouceId, textviewId, list);
        flater = context.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DBIcon dbIcon = getItem(position);

//        View rowview = flater.inflate(R.layout.row_category_spinner, null, true);
        if(convertView == null){
            convertView = flater.inflate(R.layout.row_icon_spinner,parent, false);
        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(dbIcon.getName());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = flater.inflate(R.layout.row_category_spinner,parent, false);
        }
        DBIcon dbIcon = getItem(position);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(dbIcon.getName());

        return convertView;
    }
}
