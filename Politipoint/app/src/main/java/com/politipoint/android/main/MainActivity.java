package com.politipoint.android.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends Activity implements MainView, RecyclerView.OnClickListener {

//    private ListView listView;
//    private ProgressBar progressBar;
    private MainPresenter presenter;
    private RecyclerView recList;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MainPresenterImpl(this, new FindItemsInteractorImpl());

        setContentView(R.layout.activity_my);
        recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        recList.setOnClickListener(this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
    }


    @Override protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override public void showProgress() {
 //       progressBar.setVisibility(View.VISIBLE);
 //       listView.setVisibility(View.INVISIBLE);
    }

    @Override public void hideProgress() {
 //       progressBar.setVisibility(View.INVISIBLE);
 //       listView.setVisibility(View.VISIBLE);
    }

    @Override public void setItems(List<Member> ca) {
        recList.setAdapter(new MemberAdapter(ca));
    }

    @Override public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override public void onClick(View view) {
        presenter.onItemClicked(recList.getChildLayoutPosition(view));
    }

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
            contactViewHolder.vName.setTag(Integer.valueOf(i));
        }

        @Override
        public MemberAdapter.MemberViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            final View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.card_layout, viewGroup, false);

            contxt = viewGroup.getContext();

            final int temp = i;


            return new MemberAdapter.MemberViewHolder(itemView, i);
        }

        public void startDetails(SenateDetail result){

            Intent i = new Intent(contxt, DetailsActivity.class);
            i.putExtra("MyResult", result);
            contxt.startActivity(i);

        }

         class MemberViewHolder extends RecyclerView.ViewHolder {

            protected TextView vName;
            protected TextView vStateLabel;
            protected TextView vState;
            protected TextView vTitle;

            public MemberViewHolder(View v, int i) {
                super(v);

                vName =  (TextView) v.findViewById(R.id.txtName);
                vStateLabel = (TextView)  v.findViewById(R.id.txtStateLabel);
                vState = (TextView)  v.findViewById(R.id.txtState);
                vTitle = (TextView) v.findViewById(R.id.title);
                vName.setTag(Integer.valueOf(i));

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer position;

                        position = getLayoutPosition();

                        MemberService serviceAPI = RestClient.getClient();
                        Call<DetailResults> loadSizeCall = serviceAPI.loadSenateDetail(memberList.get(position).getId());
                        loadSizeCall.enqueue(new Callback<DetailResults>() {
                            @Override
                            public void onResponse(Call<DetailResults> call, Response<DetailResults > response) {

                                DetailResults test = response.body();
                                SenateDetail result = test.getResults().get(0);
                                startDetails(result);


                            }

                            @Override
                            public void onFailure(Call<DetailResults> call, Throwable t) {
                                System.out.println(t.getMessage());
                            }
                        });
                    }
                });


            }
        }
    }


}
