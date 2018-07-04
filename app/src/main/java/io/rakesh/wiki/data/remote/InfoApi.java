package io.rakesh.wiki.data.remote;

import io.rakesh.wiki.model.CountryInfo;
import retrofit2.Call;
import retrofit2.http.GET;

public interface InfoApi {
    @GET("facts.json")
    Call<CountryInfo> getCountryInfo();
}
