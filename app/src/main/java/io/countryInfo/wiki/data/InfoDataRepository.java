package io.countryInfo.wiki.data;

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

    private InfoDataRepository() {
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


    public void getCountryInfoFromCloud(final GetCountryInfoCallback callback) {
        Call<CountryInfo> call = infoApi.getCountryInfo();
        call.enqueue(new Callback<CountryInfo>() {
            @Override
            public void onResponse(Call<CountryInfo> call, Response<CountryInfo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onCountryInfoFetchSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<CountryInfo> call, Throwable t) {
                //We can still granularize this error messages.
                callback.onCountryInfoFetchFailure(new MessageEvent("NETWORK ERROR"));
            }
        });
    }
}
