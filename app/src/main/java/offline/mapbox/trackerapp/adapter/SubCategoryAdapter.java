package offline.mapbox.trackerapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import offline.mapbox.trackerapp.R;
import offline.mapbox.trackerapp.model.DBSubCategory;

public class SubCategoryAdapter extends ArrayAdapter<DBSubCategory> {
    LayoutInflater flater;

    public SubCategoryAdapter(Activity context, int resouceId, int textviewId, List<DBSubCategory> list){

        super(context, resouceId, textviewId, list);
        flater = context.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DBSubCategory dbSubCategory = getItem(position);

//        View rowview = flater.inflate(R.layout.row_category_spinner, null, true);
        if(convertView == null){
            convertView = flater.inflate(R.layout.row_sub_category_spinner,parent, false);
        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(dbSubCategory.getSub_category());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = flater.inflate(R.layout.row_sub_category_spinner,parent, false);
        }
        DBSubCategory dbSubCategory = getItem(position);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(dbSubCategory.getSub_category());

        return convertView;
    }
}
