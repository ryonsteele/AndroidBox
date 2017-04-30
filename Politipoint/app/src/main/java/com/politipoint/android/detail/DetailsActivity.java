package com.politipoint.android.detail;

import android.app.Activity;
import android.os.Bundle;

import com.politipoint.android.models.SenateDetail;

/**
 * Created by ryon on 4/29/2017.
 */

public class DetailsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SenateDetail temp =(SenateDetail) getIntent().getSerializableExtra("MyResult");
    }
}
