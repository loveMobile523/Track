package offline.mapbox.trackerapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import offline.mapbox.trackerapp.R;
import offline.mapbox.trackerapp.model.DBCategory;

public class CategoryAdapter extends ArrayAdapter<DBCategory> {
    LayoutInflater flater;

    public CategoryAdapter(Activity context, int resouceId, int textviewId, List<DBCategory> list){

        super(context, resouceId, textviewId, list);
        flater = context.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DBCategory dbCategory = getItem(position);

//        View rowview = flater.inflate(R.layout.row_category_spinner, null, true);
        if(convertView == null){
            convertView = flater.inflate(R.layout.row_category_spinner,parent, false);
        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(dbCategory.getCategory());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = flater.inflate(R.layout.row_category_spinner,parent, false);
        }
        DBCategory dbCategory = getItem(position);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(dbCategory.getCategory());

        return convertView;
    }
}
