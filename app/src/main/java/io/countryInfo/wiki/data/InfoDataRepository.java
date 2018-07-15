package io.countryInfo.wiki.data;

import io.countryInfo.wiki.data.remote.InfoApi;
import io.countryInfo.wiki.data.remote.InfoApiClient;
import io.countryInfo.wiki.model.CountryInfo;
import io.countryInfo.wiki.model.Resource;
import io.countryInfo.wiki.model.Status;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class InfoDataRepository {
    private InfoApi infoApi;

    public InfoDataRepository() {
        initAPI();
    }

    private void initAPI() {
        Retrofit retrofit = InfoApiClient.getRetrofitClient();
        infoApi = retrofit.create(InfoApi.class);
    }

    public Single<Resource> getCountryInfoFromCloud(){
        return infoApi.getCountryInfo()
                .subscribeOn(Schedulers.io())
                .map(new Function<CountryInfo, Resource>() {
                    @Override
                    public Resource apply(CountryInfo countryInfo) throws Exception {
                        return new Resource(countryInfo, Status.SUCCESS);
                    }
                }).onErrorResumeNext(new Function<Throwable, SingleSource<? extends Resource>>() {
                    @Override
                    public SingleSource<? extends Resource> apply(Throwable throwable) throws Exception {
                        return Single.just(new Resource(null,Status.FAILURE));
                    }
                });
    }

}
