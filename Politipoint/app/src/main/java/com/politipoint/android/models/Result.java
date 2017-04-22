package com.politipoint.android.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("num_results")
    @Expose
    private String numResults;
    @SerializedName("members")
    @Expose
    private List<Member> members = null;

    public String getNumResults() {
        return numResults;
    }

    public void setNumResults(String numResults) {
        this.numResults = numResults;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}