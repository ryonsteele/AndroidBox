package com.politipoint.android.Login;

import android.os.Handler;

import com.politipoint.android.models.Member;

import java.util.List;

public class SplashInteractorImpl implements SplashInteractor {

    public List<Member> members;
    public boolean error = false;

    @Override
    public void GoHome(final OnSplashFinishedListener listener) {

        new Handler().postDelayed(new Runnable() {
            @Override public void run() {

                listener.onSuccess();
            }
        }, 8000);
    }
}
