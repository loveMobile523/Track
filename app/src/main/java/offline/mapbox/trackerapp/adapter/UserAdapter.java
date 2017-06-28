package offline.mapbox.trackerapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import offline.mapbox.trackerapp.R;
import offline.mapbox.trackerapp.model.DBUser;

public class UserAdapter extends BaseAdapter {

    private ArrayList<DBUser> modelMessageArray;
    private LayoutInflater inflater;

    public UserAdapter(Context context, ArrayList<DBUser> modelMessageArray) {
        this.modelMessageArray = modelMessageArray;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return modelMessageArray.size();
    }

    @Override
    public Object getItem(int position) {
        return modelMessageArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_message, parent, false);
        }

        DBUser dbUser = (DBUser) getItem(position);
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);

        tvUsername.setText(dbUser.getUsername());
        return convertView;
    }
}
