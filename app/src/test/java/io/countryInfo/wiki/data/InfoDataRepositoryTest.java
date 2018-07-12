package io.countryInfo.wiki.data;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import io.countryInfo.wiki.data.remote.InfoApi;
import io.countryInfo.wiki.model.CountryInfo;
import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static junit.framework.Assert.assertEquals;

public class InfoDataRepositoryTest {

    private MockWebServer mockWebServer;
    private InfoApi infoApi;
    TestSubscriber<CountryInfo> mSubscriber;

    private String sampleJson = "{\n" +
            "\"title\":\"About Canada\",\n" +
            "\"rows\":[\n" +
            "\t{\n" +
            "\t\"title\":\"Beavers\",\n" +
            "\t\"description\":\"Beavers are second only to humans in their ability to manipulate and change their environment. They can measure up to 1.3 metres long. A group of beavers is called a colony\",\n" +
            "\t\"imageHref\":\"http://upload.wikimedia.org/wikipedia/commons/thumb/6/6b/American_Beaver.jpg/220px-American_Beaver.jpg\"\n" +
            "\t}";

    @Before
    public void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        Retrofit retrofit =  new Retrofit.Builder()
                .baseUrl("https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        infoApi = retrofit.create(InfoApi.class);
    }

    @Test
    public void testGetCountryInfoFromCloud() throws IOException {
        MockResponse mockResponse = new MockResponse().setResponseCode(200)
                .setBody(sampleJson);
        mockWebServer.enqueue(mockResponse);
        TestObserver observer = new TestObserver();
        infoApi.getCountryInfo().subscribe(observer);
        observer.assertNoErrors();
        observer.awaitTerminalEvent();
        observer.assertComplete();
        CountryInfo  countryInfo = (CountryInfo) observer.values().get(0);
        assertEquals("About Canada", countryInfo.getTitle());
    }



    @After
    public void tearDown() throws IOException {
        // We're done with tests, shut it down
        mockWebServer.shutdown();
    }
}
