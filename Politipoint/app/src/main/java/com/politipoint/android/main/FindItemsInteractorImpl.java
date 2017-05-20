package com.politipoint.android.main;

import android.os.Handler;
import com.politipoint.android.Util.MemberService;
import com.politipoint.android.Util.RestClient;
import com.politipoint.android.models.CongressResults;
import com.politipoint.android.models.Member;
import com.politipoint.android.models.Result;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindItemsInteractorImpl implements FindItemsInteractor {

    public List<Member> members = new ArrayList<Member>();

    @Override public void findItems(final OnFinishedListener listener) {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {

                MemberService serviceAPI = RestClient.getClient();
                List<String> states = new ArrayList<String>();
                states.add("AL");
                states.add("FL");

                for(String state : states) {
                    Call<CongressResults> loadSizeCall = serviceAPI.loadSenate(state);
                    loadSizeCall.enqueue(new Callback<CongressResults>() {
                        @Override
                        public void onResponse(Call<CongressResults> call, Response<CongressResults> response) {

                            CongressResults test = response.body();
                            Result result = test.getResults().get(0);
//                        members = result.getMembers();
                            members.addAll(result.getMembers());

//                        if (members != null){
//                            listener.onFinished(members);

//                        }//todo else handle
                        }

                        @Override
                        public void onFailure(Call<CongressResults> call, Throwable t) {
                            System.out.println(t.getMessage());
                        }
                    });
                }

//                if (members != null)
//                    listener.onFinished(members);
            }
        }, 2000);

        if (members != null)
            listener.onFinished(members);
    }
}
