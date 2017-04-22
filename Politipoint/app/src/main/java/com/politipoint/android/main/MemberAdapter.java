package com.politipoint.android.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.politipoint.android.app.R;
import com.politipoint.android.models.Member;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {

    private List<Member> memberList;

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
        contactViewHolder.vName.setText(ci.getFirstName());
        contactViewHolder.vSurname.setText(ci.getLastName());
        contactViewHolder.vEmail.setText(ci.getOffice());
        contactViewHolder.vTitle.setText(ci.getFirstName() + " " + ci.getLastName());
    }

    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout, viewGroup, false);

        return new MemberViewHolder(itemView);
    }

    public static class MemberViewHolder extends RecyclerView.ViewHolder {

        protected TextView vName;
        protected TextView vSurname;
        protected TextView vEmail;
        protected TextView vTitle;

        public MemberViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.txtName);
            vSurname = (TextView)  v.findViewById(R.id.txtSurname);
            vEmail = (TextView)  v.findViewById(R.id.txtEmail);
            vTitle = (TextView) v.findViewById(R.id.title);
        }
    }
}