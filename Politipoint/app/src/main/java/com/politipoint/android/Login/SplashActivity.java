package com.politipoint.android.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.politipoint.android.app.R;
import com.politipoint.android.main.MainActivity;

public class SplashActivity extends Activity implements SplashView {

    private ProgressBar progressBar;
    private SplashPresenter presenter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = (ProgressBar) findViewById(R.id.progress);
//        findViewById(R.id.button).setOnClickListener(this);

        presenter = new SplashPresenterImpl(this);

        presenter.validate();
    }

    @Override protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }


    @Override public void navigateToHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

//    @Override public void onClick(View v) {
//        presenter.validateCredentials();
//    }
}
