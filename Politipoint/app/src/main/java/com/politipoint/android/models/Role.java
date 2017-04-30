package com.politipoint.android.models;

/**
 * Created by ryon on 4/29/2017.
 */

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Role implements Serializable{

    @SerializedName("congress")
    @Expose
    private String congress;
    @SerializedName("chamber")
    @Expose
    private String chamber;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("party")
    @Expose
    private String party;
    @SerializedName("leadership_role")
    @Expose
    private String leadershipRole;
    @SerializedName("fec_candidate_id")
    @Expose
    private String fecCandidateId;
    @SerializedName("seniority")
    @Expose
    private String seniority;
    @SerializedName("senate_class")
    @Expose
    private String senateClass;
    @SerializedName("state_rank")
    @Expose
    private String stateRank;
    @SerializedName("lis_id")
    @Expose
    private String lisId;
    @SerializedName("ocd_id")
    @Expose
    private String ocdId;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("office")
    @Expose
    private String office;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("fax")
    @Expose
    private String fax;
    @SerializedName("contact_form")
    @Expose
    private String contactForm;
    @SerializedName("committees")
    @Expose
    private List<Object> committees = null;

    public String getCongress() {
        return congress;
    }

    public void setCongress(String congress) {
        this.congress = congress;
    }

    public String getChamber() {
        return chamber;
    }

    public void setChamber(String chamber) {
        this.chamber = chamber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getFecCandidateId() {
        return fecCandidateId;
    }

    public void setFecCandidateId(String fecCandidateId) {
        this.fecCandidateId = fecCandidateId;
    }

    public String getSeniority() {
        return seniority;
    }

    public void setSeniority(String seniority) {
        this.seniority = seniority;
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

    public String getOcdId() {
        return ocdId;
    }

    public void setOcdId(String ocdId) {
        this.ocdId = ocdId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public String getContactForm() {
        return contactForm;
    }

    public void setContactForm(String contactForm) {
        this.contactForm = contactForm;
    }

    public List<Object> getCommittees() {
        return committees;
    }

    public void setCommittees(List<Object> committees) {
        this.committees = committees;
    }

}