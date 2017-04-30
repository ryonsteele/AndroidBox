package com.politipoint.android.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetailResults {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("results")
    @Expose
    private List<SenateDetail> results = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SenateDetail> getResults() {
        return results;
    }

    public void setResults(List<SenateDetail> results) {
        this.results = results;
    }

}
