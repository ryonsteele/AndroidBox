package com.politipoint.android.Login;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.politipoint.android.Util.MemberService;
import com.politipoint.android.Util.RestClient;
import com.politipoint.android.models.CongressResults;
import com.politipoint.android.models.Member;
import com.politipoint.android.models.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashInteractorImpl implements SplashInteractor {

    public List<Member> members;
    public boolean error = false;

    @Override
    public void login(final OnLoginFinishedListener listener) {

        new Handler().postDelayed(new Runnable() {
            @Override public void run() {

                listener.onSuccess();
            }
        }, 8000);
    }
}
