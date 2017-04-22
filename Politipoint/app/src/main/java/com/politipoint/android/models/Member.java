package com.politipoint.android.models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Member implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("api_uri")
    @Expose
    private String apiUri;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("middle_name")
    @Expose
    private String middleName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("party")
    @Expose
    private String party;
    @SerializedName("leadership_role")
    @Expose
    private String leadershipRole;
    @SerializedName("twitter_account")
    @Expose
    private String twitterAccount;
    @SerializedName("facebook_account")
    @Expose
    private String facebookAccount;
    @SerializedName("youtube_account")
    @Expose
    private String youtubeAccount;
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
    @SerializedName("crp_id")
    @Expose
    private String crpId;
    @SerializedName("google_entity_id")
    @Expose
    private String googleEntityId;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("rss_url")
    @Expose
    private String rssUrl;
    @SerializedName("contact_form")
    @Expose
    private String contactForm;
    @SerializedName("domain")
    @Expose
    private String domain;
    @SerializedName("in_office")
    @Expose
    private String inOffice;
    @SerializedName("dw_nominate")
    @Expose
    private String dwNominate;
    @SerializedName("ideal_point")
    @Expose
    private String idealPoint;
    @SerializedName("seniority")
    @Expose
    private String seniority;
    @SerializedName("total_votes")
    @Expose
    private String totalVotes;
    @SerializedName("missed_votes")
    @Expose
    private String missedVotes;
    @SerializedName("total_present")
    @Expose
    private String totalPresent;
    @SerializedName("ocd_id")
    @Expose
    private String ocdId;
    @SerializedName("office")
    @Expose
    private String office;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("fax")
    @Expose
    private String fax;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("senate_class")
    @Expose
    private String senateClass;
    @SerializedName("state_rank")
    @Expose
    private String stateRank;
    @SerializedName("lis_id")
    @Expose
    private String lisId;
    private final static long serialVersionUID = 7748415840127041081L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Member() {
    }

    /**
     *
     * @param ocdId
     * @param leadershipRole
     * @param votesmartId
     * @param phone
     * @param senateClass
     * @param state
     * @param facebookAccount
     * @param idealPoint
     * @param party
     * @param rssUrl
     * @param youtubeAccount
     * @param id
     * @param googleEntityId
     * @param missedVotes
     * @param totalVotes
     * @param crpId
     * @param domain
     * @param firstName
     * @param seniority
     * @param office
     * @param middleName
     * @param lastName
     * @param cspanId
     * @param fax
     * @param twitterAccount
     * @param totalPresent
     * @param dwNominate
     * @param contactForm
     * @param stateRank
     * @param inOffice
     * @param govtrackId
     * @param url
     * @param icpsrId
     * @param lisId
     * @param apiUri
     */
    public Member(String id, String apiUri, String firstName, String middleName, String lastName, String party, String leadershipRole, String twitterAccount, String facebookAccount, String youtubeAccount, String govtrackId, String cspanId, String votesmartId, String icpsrId, String crpId, String googleEntityId, String url, String rssUrl, String contactForm, String domain, String inOffice, String dwNominate, String idealPoint, String seniority, String totalVotes, String missedVotes, String totalPresent, String ocdId, String office, String phone, String fax, String state, String senateClass, String stateRank, String lisId) {
        super();
        this.id = id;
        this.apiUri = apiUri;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.party = party;
        this.leadershipRole = leadershipRole;
        this.twitterAccount = twitterAccount;
        this.facebookAccount = facebookAccount;
        this.youtubeAccount = youtubeAccount;
        this.govtrackId = govtrackId;
        this.cspanId = cspanId;
        this.votesmartId = votesmartId;
        this.icpsrId = icpsrId;
        this.crpId = crpId;
        this.googleEntityId = googleEntityId;
        this.url = url;
        this.rssUrl = rssUrl;
        this.contactForm = contactForm;
        this.domain = domain;
        this.inOffice = inOffice;
        this.dwNominate = dwNominate;
        this.idealPoint = idealPoint;
        this.seniority = seniority;
        this.totalVotes = totalVotes;
        this.missedVotes = missedVotes;
        this.totalPresent = totalPresent;
        this.ocdId = ocdId;
        this.office = office;
        this.phone = phone;
        this.fax = fax;
        this.state = state;
        this.senateClass = senateClass;
        this.stateRank = stateRank;
        this.lisId = lisId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApiUri() {
        return apiUri;
    }

    public void setApiUri(String apiUri) {
        this.apiUri = apiUri;
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

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getLeadershipRole() {
        return leadershipRole;
    }

    public void setLeadershipRole(String leadershipRole) {
        this.leadershipRole = leadershipRole;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRssUrl() {
        return rssUrl;
    }

    public void setRssUrl(String rssUrl) {
        this.rssUrl = rssUrl;
    }

    public String getContactForm() {
        return contactForm;
    }

    public void setContactForm(String contactForm) {
        this.contactForm = contactForm;
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

    public String getDwNominate() {
        return dwNominate;
    }

    public void setDwNominate(String dwNominate) {
        this.dwNominate = dwNominate;
    }

    public String getIdealPoint() {
        return idealPoint;
    }

    public void setIdealPoint(String idealPoint) {
        this.idealPoint = idealPoint;
    }

    public String getSeniority() {
        return seniority;
    }

    public void setSeniority(String seniority) {
        this.seniority = seniority;
    }

    public String getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(String totalVotes) {
        this.totalVotes = totalVotes;
    }

    public String getMissedVotes() {
        return missedVotes;
    }

    public void setMissedVotes(String missedVotes) {
        this.missedVotes = missedVotes;
    }

    public String getTotalPresent() {
        return totalPresent;
    }

    public void setTotalPresent(String totalPresent) {
        this.totalPresent = totalPresent;
    }

    public String getOcdId() {
        return ocdId;
    }

    public void setOcdId(String ocdId) {
        this.ocdId = ocdId;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSenateClass() {
        return senateClass;
    }

    public void setSenateClass(String senateClass) {
        this.senateClass = senateClass;
    }

    public String getStateRank() {
        return stateRank;
    }

    public void setStateRank(String stateRank) {
        this.stateRank = stateRank;
    }

    public String getLisId() {
        return lisId;
    }

    public void setLisId(String lisId) {
        this.lisId = lisId;
    }


}
