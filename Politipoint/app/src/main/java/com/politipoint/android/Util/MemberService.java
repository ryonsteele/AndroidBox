package com.politipoint.android.Util;


import com.politipoint.android.models.CongressResults;
import com.politipoint.android.models.Member;
import com.politipoint.android.models.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MemberService {

//    @GET("candidates")
//    Call<List<Candidate>> loadCandidates();

    @GET("80-115/senate/members.json")
     Call<CongressResults> loadSenate();

}
