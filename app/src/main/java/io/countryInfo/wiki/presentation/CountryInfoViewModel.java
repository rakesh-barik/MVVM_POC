package io.countryInfo.wiki.presentation;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import io.countryInfo.wiki.data.InfoDataRepository;
import io.countryInfo.wiki.model.CountryInfo;

public class CountryInfoViewModel extends AndroidViewModel {

    private LiveData<CountryInfo> countryInfoLiveData;

    public CountryInfoViewModel(@NonNull Application application) {
        super(application);
        InfoDataRepository infoDataRepository = InfoDataRepository.getInstance();
        infoDataRepository.setApplication(application);
        fetchCountryInfoFromCloud();
        countryInfoLiveData = infoDataRepository.getCountryInfoFromDb();
    }

    public void fetchCountryInfoFromCloud() {
        InfoDataRepository.getInstance().getCountryInfoFromCloud();
    }

    @NonNull
    @Override
    public <T extends Application> T getApplication() {
        return super.getApplication();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public LiveData<CountryInfo> getCountryInfoLiveData() {
        return countryInfoLiveData;
    }

}
