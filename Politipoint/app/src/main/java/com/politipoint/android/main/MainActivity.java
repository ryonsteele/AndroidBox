package com.politipoint.android.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.politipoint.android.Util.ImageUrl;
import com.politipoint.android.Util.MemberService;
import com.politipoint.android.Util.RestClient;
import com.politipoint.android.app.R;
import com.politipoint.android.detail.DetailsActivity;
import com.politipoint.android.models.DetailResults;
import com.politipoint.android.models.Member;
import com.politipoint.android.models.SenateDetail;

import java.io.InputStream;
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

        public Bitmap bmp;

        @Override
        public int getItemCount() {
            return memberList.size();
        }

        @Override
        public void onBindViewHolder(MemberViewHolder contactViewHolder, int i) {
            Member ci = memberList.get(i);

            contactViewHolder.vName.setText(ci.getName()+ " (" + ci.getParty() + ")");
            contactViewHolder.vRole.setText(ci.getRole());
            contactViewHolder.vState.setText(ci.getState());
            String img = ImageUrl.getImage(ci.getId());
            if(img != null) new DownloadImageTask((ImageView)  contactViewHolder.vImage).execute(img);
            contactViewHolder.vName.setTag(Integer.valueOf(i));
        }

        private class DownloadImageTask extends AsyncTask<String, Void, RoundedBitmapDrawable> {
            ImageView bmImage;

            public DownloadImageTask(ImageView bmImage) {
                this.bmImage = bmImage;
            }

            protected RoundedBitmapDrawable doInBackground(String... urls) {
                String urldisplay = urls[0];
                Bitmap mIcon11 = null;
                RoundedBitmapDrawable dr = null;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
                    mIcon11 = Bitmap.createScaledBitmap(mIcon11, 400, 500, false);
                    Resources res = getResources();
                    dr = RoundedBitmapDrawableFactory.create(res, mIcon11);
                    dr.setCornerRadius(Math.max(mIcon11.getWidth(), mIcon11.getHeight()) / 1.0f);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //return mIcon11;
                return dr;
            }

            protected void onPostExecute(RoundedBitmapDrawable result) {
                bmImage.setImageDrawable(result);
                //bmImage.setImageBitmap(Bitmap.createScaledBitmap(result, 400, 500, false));
            }}

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
             protected TextView vRole;
            protected TextView vState;
            protected TextView vTitle;
            protected ImageView vImage;

            public MemberViewHolder(View v, int i) {
                super(v);

                vName =  (TextView) v.findViewById(R.id.txtName);
                vStateLabel = (TextView)  v.findViewById(R.id.txtStateLabel);
                vRole = (TextView)  v.findViewById(R.id.txtRole);
                vState = (TextView)  v.findViewById(R.id.txtState);
                vTitle = (TextView) v.findViewById(R.id.title);
                vImage = (ImageView) v.findViewById(R.id.imageView1);
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
