package io.rakesh.wiki.presentation;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import io.rakesh.wiki.R;
import io.rakesh.wiki.model.CountryInfo;

public class MainActivity extends AppCompatActivity {

    CountryInfoViewModel viewModel;
    RecyclerView infoRecyclerView;
    ProgressBar progressBar;
    CountryInfoAdapter countryInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        infoRecyclerView = findViewById(R.id.info_recycler);
        infoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        countryInfoAdapter = new CountryInfoAdapter();
        infoRecyclerView.setAdapter(countryInfoAdapter);

        viewModel = ViewModelProviders.of(this).get(CountryInfoViewModel.class);
        loadCountryInfo();
    }

    private void loadCountryInfo() {
        viewModel.getCountryInfoLiveData().observe(this, new Observer<CountryInfo>() {
            @Override
            public void onChanged(@Nullable CountryInfo countryInfo) {
                if(countryInfo != null){
                    System.out.println("LIVE DATA-> "+ countryInfo.getTitle());
                    getSupportActionBar().setTitle(countryInfo.getTitle());
                    countryInfoAdapter.setrows(countryInfo.getRows());
                    countryInfoAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

}
