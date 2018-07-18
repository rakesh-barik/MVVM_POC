package io.countryInfo.wiki.presentation;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.Objects;

import io.countryInfo.wiki.R;
import io.countryInfo.wiki.model.Resource;
import io.countryInfo.wiki.model.Status;
import io.countryInfo.wiki.utils.ConnectivityChangeReceiver;
import io.countryInfo.wiki.utils.ListSpacingDecoration;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private CountryInfoViewModel viewModel;
    private CountryInfoAdapter countryInfoAdapter;
    private SwipeRefreshLayout refreshLayout;
    private Snackbar messageBar;
    private ConnectivityChangeReceiver changeReceiver;
    private boolean isConnectedToNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRefreshLayout();

        initRecyclerView();

        initSnackBar();

        loadCountryInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerNetworkChangeReceiver();
    }

    private void initRefreshLayout() {
        refreshLayout = findViewById(R.id.pull_to_refresh);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark
        );
    }

    private void initRecyclerView() {
        RecyclerView infoRecyclerView = findViewById(R.id.info_recycler);
        infoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        countryInfoAdapter = new CountryInfoAdapter();
        infoRecyclerView.setAdapter(countryInfoAdapter);
        RecyclerView.ItemDecoration listSpacing = new ListSpacingDecoration(this,R.dimen.card_margin);
        infoRecyclerView.addItemDecoration(listSpacing);
    }

    private void initSnackBar() {
        messageBar = Snackbar.make(findViewById(R.id.container_layout),
                R.string.check_network_connection, Snackbar.LENGTH_INDEFINITE);
        messageBar.getView().setBackgroundColor(getResources()
                .getColor(R.color.colorAccent, null));
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityChangeReceiver.isConnected()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean isActive) {
                        Log.d("MainActivity:isConnected:", isActive.toString());
                        isConnectedToNetwork = isActive;
                        if (isActive) {
                            dismissMessageBar();
                            if (countryInfoAdapter.getItemCount() == 0) {
                                setLoadingIndicator(true);
                                viewModel.getCountryInfoFromCloud();
                            }
                        } else {
                            showError();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void dismissMessageBar() {
        messageBar.dismiss();
    }

    private void registerNetworkChangeReceiver() {
        changeReceiver = new ConnectivityChangeReceiver();
        this.registerReceiver(
                changeReceiver,
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void loadCountryInfo() {
        setLoadingIndicator(true);
        viewModel = ViewModelProviders.of(this).get(CountryInfoViewModel.class);
        viewModel.getCountryInfoLiveData().observe(this, new Observer<Resource>() {
            @Override
            public void onChanged(@Nullable Resource resource) {
                if (resource != null && resource.getStatus() == Status.SUCCESS) {
                    Objects.requireNonNull(getSupportActionBar()).setTitle(resource.getCountryInfo().getTitle());
                    countryInfoAdapter.setRows(resource.getCountryInfo().getRows());

                    setLoadingIndicator(false);
                    dismissMessageBar();
                } else {
                    showError();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        if (isConnectedToNetwork && viewModel != null) {
            viewModel.onCleared();
            viewModel.getCountryInfoFromCloud();
            dismissMessageBar();
        } else {
            showError();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
            unregisterReceiver(changeReceiver);
    }


    private void showError() {
        setLoadingIndicator(false);
        messageBar.show();
    }

    private void setLoadingIndicator(final boolean active) {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(active);
            }
        });
    }

}
