package io.rakesh.wiki.data;

import android.util.Log;

import io.rakesh.wiki.data.remote.InfoApi;
import io.rakesh.wiki.data.remote.InfoApiClient;
import io.rakesh.wiki.model.CountryInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InfoDataRepository {
    private static InfoDataRepository instance = null;
    private InfoApi infoApi;

    public InfoDataRepository() {
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

    private void getCountryInfoFromCloud() {
        Call<CountryInfo> call = infoApi.getCountryInfo();
        call.enqueue(new Callback<CountryInfo>() {
            @Override
            public void onResponse(Call<CountryInfo> call, Response<CountryInfo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CountryInfo countryInfo = response.body();
                    Log.d("COUNTRY INFO FROM API-->",countryInfo.getTitle());

                }
            }

            @Override
            public void onFailure(Call<CountryInfo> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

}
