package io.countryInfo.wiki.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import io.countryInfo.wiki.data.InfoDataRepository;
import io.countryInfo.wiki.model.CountryInfo;

public class CountryInfoViewModel extends ViewModel {

    private MutableLiveData<CountryInfo> countryInfoLiveData;

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public LiveData<CountryInfo> getCountryInfoLiveData() {
        if(countryInfoLiveData == null){
            countryInfoLiveData = new MutableLiveData<>();
            getCountryInfoFromCloud();
            countryInfoLiveData = InfoDataRepository.getInstance().getCountryInfo();
        }
        return countryInfoLiveData;
    }

    public void getCountryInfoFromCloud() {
        InfoDataRepository.getInstance().getCountryInfoFromCloud();
    }

}
