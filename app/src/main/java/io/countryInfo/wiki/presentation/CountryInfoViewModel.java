package io.countryInfo.wiki.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import io.countryInfo.wiki.data.InfoDataRepository;
import io.countryInfo.wiki.model.Resource;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CountryInfoViewModel extends ViewModel{

    private MutableLiveData<Resource> countryInfoLiveData;

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public LiveData<Resource> getCountryInfoLiveData() {
        if(countryInfoLiveData == null){
            countryInfoLiveData = new MutableLiveData<>();
        }
        return countryInfoLiveData;
    }

    public void getCountryInfoFromCloud() {
        new InfoDataRepository()
                .getCountryInfoFromCloud()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Resource>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Resource resource) {
                countryInfoLiveData.setValue(resource);
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }
}
