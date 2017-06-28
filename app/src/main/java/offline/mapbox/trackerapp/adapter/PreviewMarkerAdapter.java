package offline.mapbox.trackerapp.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;
import offline.mapbox.trackerapp.NewMessageActivity;
import offline.mapbox.trackerapp.R;
import offline.mapbox.trackerapp.constant.Constant;
import offline.mapbox.trackerapp.database.DBMarkerTable;
import offline.mapbox.trackerapp.model.DBMarker;
import offline.mapbox.trackerapp.model.PreviewMarkerModel;

import static offline.mapbox.trackerapp.MainActivity.dbMarkers;

public class PreviewMarkerAdapter extends BaseAdapter {
    private TextView[] tvCustomFields = new TextView[Constant.MAX_NUMBER_CUSTOM_FIELDS];
    private int[] tvCustomFieldResources = {R.id.tvField1, R.id.tvField2, R.id.tvField3,
            R.id.tvField4, R.id.tvField5, R.id.tvField6, R.id.tvField7, R.id.tvField8, R.id.tvField9,
            R.id.tvField10, R.id.tvField11, R.id.tvField12, R.id.tvField13, R.id.tvField14, R.id.tvField15};

    private ArrayList<DBMarker> modelPreviewMarkerArray;
    private LayoutInflater inflater;
    private Activity activity;

    public PreviewMarkerAdapter(Activity activity, ArrayList<DBMarker> modelPreviewMarkerArray) {
        this.modelPreviewMarkerArray = modelPreviewMarkerArray;
        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return modelPreviewMarkerArray.size();
    }

    @Override
    public Object getItem(int position) {
        return modelPreviewMarkerArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_preview_marker, parent, false);
        }

        final DBMarker dbMarker = (DBMarker) getItem(position);
        TextView tvContent = (TextView) convertView.findViewById(R.id.tvContent);

        tvContent.setText(dbMarker.getName());

        // -----------------------------------------------------------------------------------------
        Button btnView = (Button) convertView.findViewById(R.id.btnView);
        Button btnDel = (Button) convertView.findViewById(R.id.btnDel);

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(activity, dbMarker.getName(), Toast.LENGTH_SHORT).show();

                final Dialog dialog =  new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_preview_marker);
                dialog.setCancelable(false);

                // ---------------------------------------------------------------------------------
                // show custom fields

                String totalCustomFields = dbMarker.getCustom_fields();
                String[] separated = totalCustomFields.split(";");

                int len = separated.length;

//                Toast.makeText(activity, "count of custom fields: " + String.valueOf(len), Toast.LENGTH_SHORT).show();

                for(int i = 0; i < len/*Constant.MAX_NUMBER_CUSTOM_FIELDS*/; i++) {
                    tvCustomFields[i] = (TextView) dialog.findViewById(tvCustomFieldResources[i]);
                    tvCustomFields[i].setVisibility(View.VISIBLE);
                    tvCustomFields[i].setText(separated[i].trim().toString());
                }

                // popup view settings   -----------        ----------      ---
                Button btnClose = ((Button)dialog.findViewById(R.id.btnClose));
                TextView tvComment = (TextView) dialog.findViewById(R.id.tvComment);

                if(dbMarker.getDescription().equals("")) {
                    tvComment.setText("No Comment");
                } else {
                    tvComment.setText(dbMarker.getDescription());
                }

                // ---------------------------------------------------------------------------------
                // images
//                ImageView imgDownTest = (ImageView) dialog.findViewById(R.id.imgDownTest);
//
//                String totalImage = dbMarker.getImage();
//                String[] separatedImageLink = totalImage.split(";");
//
//                // http://maproots.com/dev/road/admin_side/assets/img/agent-icon.png
//                // assets/img/markers/Road-construction-operations-Kenya.jpg;
//                if(separatedImageLink.length > 0) {
//                    String imageURL = "http://maproots.com/dev/road/admin_side/" + separatedImageLink[0].trim();
//                    Picasso.with(activity).load(imageURL).into(imgDownTest);
//                }


                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelPreviewMarkerArray.remove(position);
//                dbMarkers.remove(position);
                notifyDataSetChanged();

                DBMarkerTable dbMarkerTable = new DBMarkerTable(activity);
                dbMarkerTable.deleteMarker(dbMarker);
            }
        });

        return convertView;
    }
}