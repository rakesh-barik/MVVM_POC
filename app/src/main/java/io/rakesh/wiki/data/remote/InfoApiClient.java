package io.rakesh.wiki.data.remote;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.rakesh.wiki.BuildConfig;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class InfoApiClient {
    private static final String BASE_URL = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/";
    public static final int CONNECTION_TIMEOUT = 30;
    public static final int READ_TIMEOUT = 30;
    private static Retrofit retrofit = null;

    private static OkHttpClient.Builder httpClientBuilder = null;

    public static Retrofit getRetrofitClient(){
        return getRetrofitBuild();
    }


    @NonNull
    private static Retrofit getRetrofitBuild() {

        if(retrofit == null){
            retrofit =  new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClient().build())
                    .build();
        }

        return retrofit;
    }

    private static OkHttpClient.Builder getOkHttpClient() {
        if(httpClientBuilder == null) {
            httpClientBuilder = new OkHttpClient.Builder();

            httpClientBuilder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
            httpClientBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                httpClientBuilder.addNetworkInterceptor(interceptor);
            }
            httpClientBuilder.retryOnConnectionFailure(true);
            httpClientBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);
                }
            });
        }
        return httpClientBuilder;
    }

}
