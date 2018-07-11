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

import io.countryInfo.wiki.R;
import io.countryInfo.wiki.model.Resource;
import io.countryInfo.wiki.model.Status;


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
        viewModel.getCountryInfoLiveData().observe(this, new Observer<Resource>() {
            @Override
            public void onChanged(@Nullable Resource resource) {
                if (resource != null && resource.getStatus() == Status.SUCCESS) {
                    getSupportActionBar().setTitle(resource.getCountryInfo().getTitle());
                    countryInfoAdapter.setRows(resource.getCountryInfo().getRows());
                    setLoadingIndicator(false);
                } else {
                    showError();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        if (!isConnectedToNetwork()) {
            showError();
        } else {
            if (viewModel != null) {
                viewModel.onCleared();
                viewModel.getCountryInfoFromCloud();
            }
        }

    }

    private void showError() {
        setLoadingIndicator(false);
        Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
    }

    private void setLoadingIndicator(boolean active) {
        progressBar.setVisibility(active ? View.VISIBLE : View.INVISIBLE);
        refreshLayout.setRefreshing(active);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
