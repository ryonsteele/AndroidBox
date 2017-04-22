package com.politipoint.android.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.politipoint.android.app.R;
import com.politipoint.android.models.Member;
import java.util.List;

public class MainActivity extends Activity implements MainView, AdapterView.OnItemClickListener {

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

    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.onItemClicked(position);
    }
}
