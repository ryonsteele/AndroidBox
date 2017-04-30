package com.politipoint.android.models;

/**
 * Created by ryon on 4/29/2017.
 */

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SenateDetail implements Serializable{

    @SerializedName("member_id")
    @Expose
    private String memberId;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("middle_name")
    @Expose
    private String middleName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("date_of_birth")
    @Expose
    private String dateOfBirth;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("url")
    @Expose
    private Object url;
    @SerializedName("times_topics_url")
    @Expose
    private String timesTopicsUrl;
    @SerializedName("times_tag")
    @Expose
    private String timesTag;
    @SerializedName("govtrack_id")
    @Expose
    private String govtrackId;
    @SerializedName("cspan_id")
    @Expose
    private String cspanId;
    @SerializedName("votesmart_id")
    @Expose
    private String votesmartId;
    @SerializedName("icpsr_id")
    @Expose
    private String icpsrId;
    @SerializedName("twitter_account")
    @Expose
    private String twitterAccount;
    @SerializedName("facebook_account")
    @Expose
    private String facebookAccount;
    @SerializedName("youtube_account")
    @Expose
    private String youtubeAccount;
    @SerializedName("crp_id")
    @Expose
    private String crpId;
    @SerializedName("google_entity_id")
    @Expose
    private String googleEntityId;
    @SerializedName("rss_url")
    @Expose
    private String rssUrl;
    @SerializedName("domain")
    @Expose
    private String domain;
    @SerializedName("in_office")
    @Expose
    private String inOffice;
    @SerializedName("current_party")
    @Expose
    private String currentParty;
    @SerializedName("most_recent_vote")
    @Expose
    private String mostRecentVote;
    @SerializedName("roles")
    @Expose
    private List<Role> roles = null;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Object getUrl() {
        return url;
    }

    public void setUrl(Object url) {
        this.url = url;
    }

    public String getTimesTopicsUrl() {
        return timesTopicsUrl;
    }

    public void setTimesTopicsUrl(String timesTopicsUrl) {
        this.timesTopicsUrl = timesTopicsUrl;
    }

    public String getTimesTag() {
        return timesTag;
    }

    public void setTimesTag(String timesTag) {
        this.timesTag = timesTag;
    }

    public String getGovtrackId() {
        return govtrackId;
    }

    public void setGovtrackId(String govtrackId) {
        this.govtrackId = govtrackId;
    }

    public String getCspanId() {
        return cspanId;
    }

    public void setCspanId(String cspanId) {
        this.cspanId = cspanId;
    }

    public String getVotesmartId() {
        return votesmartId;
    }

    public void setVotesmartId(String votesmartId) {
        this.votesmartId = votesmartId;
    }

    public String getIcpsrId() {
        return icpsrId;
    }

    public void setIcpsrId(String icpsrId) {
        this.icpsrId = icpsrId;
    }

    public String getTwitterAccount() {
        return twitterAccount;
    }

    public void setTwitterAccount(String twitterAccount) {
        this.twitterAccount = twitterAccount;
    }

    public String getFacebookAccount() {
        return facebookAccount;
    }

    public void setFacebookAccount(String facebookAccount) {
        this.facebookAccount = facebookAccount;
    }

    public String getYoutubeAccount() {
        return youtubeAccount;
    }

    public void setYoutubeAccount(String youtubeAccount) {
        this.youtubeAccount = youtubeAccount;
    }

    public String getCrpId() {
        return crpId;
    }

    public void setCrpId(String crpId) {
        this.crpId = crpId;
    }

    public String getGoogleEntityId() {
        return googleEntityId;
    }

    public void setGoogleEntityId(String googleEntityId) {
        this.googleEntityId = googleEntityId;
    }

    public String getRssUrl() {
        return rssUrl;
    }

    public void setRssUrl(String rssUrl) {
        this.rssUrl = rssUrl;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getInOffice() {
        return inOffice;
    }

    public void setInOffice(String inOffice) {
        this.inOffice = inOffice;
    }

    public String getCurrentParty() {
        return currentParty;
    }

    public void setCurrentParty(String currentParty) {
        this.currentParty = currentParty;
    }

    public String getMostRecentVote() {
        return mostRecentVote;
    }

    public void setMostRecentVote(String mostRecentVote) {
        this.mostRecentVote = mostRecentVote;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

}
