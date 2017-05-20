package com.politipoint.android.models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Member implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("party")
    @Expose
    private String party;
    @SerializedName("times_topics_url")
    @Expose
    private String timesTopicsUrl;
    @SerializedName("twitter_id")
    @Expose
    private String twitterId;
    @SerializedName("youtube_id")
    @Expose
    private String youtubeId;
    @SerializedName("seniority")
    @Expose
    private String seniority;
    @SerializedName("next_election")
    @Expose
    private String nextElection;
    @SerializedName("api_uri")
    @Expose
    private String apiUri;
    private final static long serialVersionUID = 1509728427648709982L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Member() {
    }

    /**
     *
     * @param id
     * @param timesTopicsUrl
     * @param name
     * @param gender
     * @param role
     * @param nextElection
     * @param seniority
     * @param youtubeId
     * @param twitterId
     * @param party
     * @param apiUri
     */
    public Member(String id, String name, String role, String gender, String party, String timesTopicsUrl, String twitterId, String youtubeId, String seniority, String nextElection, String apiUri) {
        super();
        this.id = id;
        this.name = name;
        this.role = role;
        this.gender = gender;
        this.party = party;
        this.timesTopicsUrl = timesTopicsUrl;
        this.twitterId = twitterId;
        this.youtubeId = youtubeId;
        this.seniority = seniority;
        this.nextElection = nextElection;
        this.apiUri = apiUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getTimesTopicsUrl() {
        return timesTopicsUrl;
    }

    public void setTimesTopicsUrl(String timesTopicsUrl) {
        this.timesTopicsUrl = timesTopicsUrl;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }

    public String getSeniority() {
        return seniority;
    }

    public void setSeniority(String seniority) {
        this.seniority = seniority;
    }

    public String getNextElection() {
        return nextElection;
    }

    public void setNextElection(String nextElection) {
        this.nextElection = nextElection;
    }

    public String getApiUri() {
        return apiUri;
    }

    public void setApiUri(String apiUri) {
        this.apiUri = apiUri;
    }

}
