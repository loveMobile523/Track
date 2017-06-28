package offline.mapbox.trackerapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import offline.mapbox.trackerapp.R;
import offline.mapbox.trackerapp.model.DBUser;
import offline.mapbox.trackerapp.model.UsernameModel;

public class UsernameAdapter extends ArrayAdapter<DBUser> {
    LayoutInflater flater;

    public UsernameAdapter(Activity context, int resouceId, int textviewId, List<DBUser> list){

        super(context, resouceId, textviewId, list);
        flater = context.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DBUser dbUser = getItem(position);

//        View rowview = flater.inflate(R.layout.row_username_spinner, null, true);
        if(convertView == null){
            convertView = flater.inflate(R.layout.row_username_spinner,parent, false);
        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(dbUser.getUsername());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = flater.inflate(R.layout.row_username_spinner,parent, false);
        }
        DBUser dbUser = getItem(position);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(dbUser.getUsername());

        return convertView;
    }
}
