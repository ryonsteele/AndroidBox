package com.politipoint.android.Util;


import com.politipoint.android.models.CongressResults;
import com.politipoint.android.models.DetailResults;
import com.politipoint.android.models.Member;
import com.politipoint.android.models.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MemberService {

//    @GET("candidates")
//    Call<List<Candidate>> loadCandidates();

//    @GET("80-115/senate/members.json")
//     Call<CongressResults> loadSenate();

    @GET("members/senate/{state}/current.json")
    Call<CongressResults> loadSenate(@Path(value = "state", encoded = true) String state);

    @GET("members/{member_id}.json")
    Call<DetailResults> loadSenateDetail(@Path(value = "member_id", encoded = true) String memberid);

}
