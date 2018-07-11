package io.countryInfo.wiki.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import io.countryInfo.wiki.data.GetCountryInfoCallback;
import io.countryInfo.wiki.data.InfoDataRepository;
import io.countryInfo.wiki.data.MessageEvent;
import io.countryInfo.wiki.model.CountryInfo;
import io.countryInfo.wiki.model.Resource;
import io.countryInfo.wiki.model.Status;

public class CountryInfoViewModel extends ViewModel implements GetCountryInfoCallback{

    private MutableLiveData<Resource> countryInfoLiveData;

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public LiveData<Resource> getCountryInfoLiveData() {
        if(countryInfoLiveData == null){
            countryInfoLiveData = new MutableLiveData<>();
            getCountryInfoFromCloud();
        }
        return countryInfoLiveData;
    }

    public void getCountryInfoFromCloud() {
        InfoDataRepository.getInstance().getCountryInfoFromCloud(this);
    }

    @Override
    public void onCountryInfoFetchSuccess(CountryInfo countryInfo) {
        countryInfoLiveData.setValue(new Resource(countryInfo, Status.SUCCESS,null));
    }

    @Override
    public void onCountryInfoFetchFailure(MessageEvent messageEvent) {
        countryInfoLiveData.setValue(new Resource(null, Status.FAILURE,messageEvent.getMessage()));
    }
}
