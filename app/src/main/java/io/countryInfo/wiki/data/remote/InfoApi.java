package io.countryInfo.wiki.data.remote;

import io.countryInfo.wiki.model.CountryInfo;
import io.reactivex.Single;
import retrofit2.http.GET;

public interface InfoApi {
    @GET("facts.json")
    Single<CountryInfo> getCountryInfo();
}
