package io.countryInfo.wiki.presentation;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.countryInfo.wiki.R;
import io.countryInfo.wiki.data.MessageEvent;
import io.countryInfo.wiki.model.CountryInfo;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    CountryInfoViewModel viewModel;
    RecyclerView infoRecyclerView;
    ProgressBar progressBar;
    CountryInfoAdapter countryInfoAdapter;
    SwipeRefreshLayout refreshLayout;
    IdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        refreshLayout = findViewById(R.id.pull_to_refresh);
        refreshLayout.setOnRefreshListener(this);

        infoRecyclerView = findViewById(R.id.info_recycler);
        infoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        countryInfoAdapter = new CountryInfoAdapter();
        infoRecyclerView.setAdapter(countryInfoAdapter);
        loadCountryInfo();
    }

    private void loadCountryInfo() {
        viewModel = ViewModelProviders.of(this).get(CountryInfoViewModel.class);
        viewModel.getCountryInfoLiveData().observe(this, new Observer<CountryInfo>() {
            @Override
            public void onChanged(@Nullable CountryInfo countryInfo) {
                if (countryInfo != null) {
                    getSupportActionBar().setTitle(countryInfo.getTitle());
                    countryInfoAdapter.setRows(countryInfo.getRows());
                    setLoadingIndicator(false);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        if (!isConnectedToNetwork()) {
            setLoadingIndicator(false);
            Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
        if (viewModel != null) {
            viewModel.onCleared();
            viewModel.getCountryInfoFromCloud();
        }

    }

    private void setLoadingIndicator(boolean active) {
        progressBar.setVisibility(active ? View.VISIBLE : View.INVISIBLE);
        refreshLayout.setRefreshing(active);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent event) {
        Toast.makeText(this, event.getMessage(), Toast.LENGTH_LONG).show();
        setLoadingIndicator(false);
    }

    private boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
