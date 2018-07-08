package io.countryInfo.wiki.data.remote;

import android.support.annotation.NonNull;
import android.support.test.espresso.IdlingRegistry;

import com.jakewharton.espresso.OkHttp3IdlingResource;

import io.countryInfo.wiki.BuildConfig;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class InfoApiClient {
    private static final String BASE_URL = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/";
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient = null;

    public static Retrofit getRetrofitClient() {
        return getRetrofitBuild();
    }


    @NonNull
    private static Retrofit getRetrofitBuild() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClient())
                    .build();

        }

        return retrofit;
    }

    private static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        if (BuildConfig.DEBUG) {
            IdlingRegistry.getInstance().register(OkHttp3IdlingResource.create("okhttp", okHttpClient));
        }
        return okHttpClient;
    }

}
