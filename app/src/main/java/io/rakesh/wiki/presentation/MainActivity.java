package io.rakesh.wiki.presentation;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import io.rakesh.wiki.R;

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
    }
}
