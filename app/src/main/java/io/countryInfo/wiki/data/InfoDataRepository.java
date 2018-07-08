package io.countryInfo.wiki.data;

import android.arch.lifecycle.MutableLiveData;

import org.greenrobot.eventbus.EventBus;

import io.countryInfo.wiki.data.remote.InfoApi;
import io.countryInfo.wiki.data.remote.InfoApiClient;
import io.countryInfo.wiki.model.CountryInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InfoDataRepository {
    private static InfoDataRepository instance = null;
    private InfoApi infoApi;
    private MutableLiveData<CountryInfo> infoLiveData;


    private InfoDataRepository() {
        infoLiveData = new MutableLiveData<>();
        initAPI();
    }

    public static InfoDataRepository getInstance() {
        if (instance == null) {
            synchronized (InfoDataRepository.class) {
                if (instance == null) {
                    instance = new InfoDataRepository();
                }
            }
        }
        return instance;
    }



    private void initAPI() {
        Retrofit retrofit = InfoApiClient.getRetrofitClient();
        infoApi = retrofit.create(InfoApi.class);
    }

    public MutableLiveData<CountryInfo> getCountryInfo() {
        return infoLiveData;
    }

    public void getCountryInfoFromCloud() {
        Call<CountryInfo> call = infoApi.getCountryInfo();
        call.enqueue(new Callback<CountryInfo>() {
            @Override
            public void onResponse(Call<CountryInfo> call, Response<CountryInfo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CountryInfo countryInfo = response.body();
                    infoLiveData.setValue(countryInfo);
                }
            }

            @Override
            public void onFailure(Call<CountryInfo> call, Throwable t) {
                //We can still granularize this error messages.
                EventBus.getDefault().post(new MessageEvent("NETWORK ERROR"));
            }
        });
    }
}
