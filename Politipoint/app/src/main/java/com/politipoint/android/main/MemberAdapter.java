package com.politipoint.android.main;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.politipoint.android.Util.MemberService;
import com.politipoint.android.Util.RestClient;
import com.politipoint.android.app.R;
import com.politipoint.android.detail.DetailsActivity;
import com.politipoint.android.models.DetailResults;
import com.politipoint.android.models.Member;
import com.politipoint.android.models.SenateDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {

    private List<Member> memberList;
    private Context contxt;

    public MemberAdapter(List<Member> contactList) {
        this.memberList = contactList;
    }


    @Override
    public int getItemCount() {
        return memberList.size();
    }

    @Override
    public void onBindViewHolder(MemberViewHolder contactViewHolder, int i) {
        Member ci = memberList.get(i);
        contactViewHolder.vName.setText(ci.getFirstName()+" "+ ci.getMiddleName() + " "+ ci.getLastName() + " (" + ci.getParty() + ")");
        //contactViewHolder.vStateLabel.setText(ci.getLastName());
        contactViewHolder.vState.setText(ci.getState());
        //contactViewHolder.vTitle.setText(ci.getFirstName() + " " + ci.getLastName());
    }

    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout, viewGroup, false);

        contxt = viewGroup.getContext();

        final int temp = i;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberService serviceAPI = RestClient.getClient();
                Call<DetailResults> loadSizeCall = serviceAPI.loadSenateDetail(memberList.get(temp).getId());
                loadSizeCall.enqueue(new Callback<DetailResults>() {
                    @Override
                    public void onResponse(Call<DetailResults> call, Response<DetailResults > response) {

                        DetailResults test = response.body();
                        SenateDetail result = test.getResults().get(0);
                        MemberAdapter.this.startDetails(result);


                    }

                    @Override
                    public void onFailure(Call<DetailResults> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });
            }
        });

        return new MemberViewHolder(itemView);
    }

    public void startDetails(SenateDetail result){

        Intent i = new Intent(contxt, DetailsActivity.class);
        i.putExtra("MyResult", result);
        contxt.startActivity(i);

    }

    public static class MemberViewHolder extends RecyclerView.ViewHolder {

        protected TextView vName;
        protected TextView vStateLabel;
        protected TextView vState;
        protected TextView vTitle;

        public MemberViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.txtName);
            vStateLabel = (TextView)  v.findViewById(R.id.txtStateLabel);
            vState = (TextView)  v.findViewById(R.id.txtState);
            vTitle = (TextView) v.findViewById(R.id.title);
        }
    }
}