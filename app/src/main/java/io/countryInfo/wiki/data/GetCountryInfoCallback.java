package io.countryInfo.wiki.data;

import io.countryInfo.wiki.model.CountryInfo;

public interface GetCountryInfoCallback {
    void onCountryInfoFetchSuccess(CountryInfo countryInfo);
    void onCountryInfoFetchFailure(MessageEvent messageEvent);
}
