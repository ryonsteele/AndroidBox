package com.politipoint.android.Login;

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


                MemberService serviceAPI = RestClient.getClient();
                Call<CongressResults> loadSizeCall = serviceAPI.loadSenate();
                loadSizeCall.enqueue(new Callback<CongressResults>() {
                    @Override
                    public void onResponse(Call<CongressResults> call, Response<CongressResults > response) {

                        CongressResults test = response.body();
                        Result result = test.getResults().get(0);
                        members = result.getMembers();

                        for(Member temp : members){
                            System.out.println(temp.getFirstName() + " "  + temp.getLastName() + " :" + temp.getId());
                        }
                        if (members != null){
                            listener.onSuccess();
                        }//todo else handle
                    }

                    @Override
                    public void onFailure(Call<CongressResults> call, Throwable t) {
                        System.out.println(t.getMessage());
                        error = true;
                    }
                });
            }
        }, 2000);
    }
}
