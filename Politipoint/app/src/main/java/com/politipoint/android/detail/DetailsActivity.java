package com.politipoint.android.detail;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.politipoint.android.app.R;
import com.politipoint.android.models.Role;
import com.politipoint.android.models.SenateDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryon on 4/29/2017.
 */

public class DetailsActivity extends Activity {

    private SenateDetail dataDetail;
    private List<Role> rolesList;

    private TextView vName;
    private TextView vTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);

        dataDetail =(SenateDetail) getIntent().getSerializableExtra("MyResult");
        rolesList = dataDetail.getRoles();

        fillUI();

    }

    public void fillUI(){

        vName =  (TextView) findViewById(R.id.txtDetailName);
        vTitle = (TextView)  findViewById(R.id.txtDetailTitle);

        vName.setText(dataDetail.getFirstName() + " " + dataDetail.getMiddleName() + " " + dataDetail.getLastName());
        vTitle.setText(rolesList.get(0).getTitle());

    }
}
