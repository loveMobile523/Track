package offline.mapbox.trackerapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import offline.mapbox.trackerapp.adapter.UsernameAdapter;
import offline.mapbox.trackerapp.model.UsernameModel;

public class NewMessageActivity extends Activity {

    private ImageButton btnBack;

    private Spinner spUsername;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        initWidget();
        initSpinner();
        onClickBtn();
    }

    private void initWidget() {
        btnBack = (ImageButton) findViewById(R.id.btnBack);
    }

    private void initSpinner() {

        spUsername = (Spinner)findViewById(R.id.spUsername);
        UsernameAdapter adapter = new UsernameAdapter(NewMessageActivity.this,
                R.layout.row_username_spinner, R.id.title, MainActivity.dbUsers);
        spUsername.setAdapter(adapter);
    }

    private void onClickBtn() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
