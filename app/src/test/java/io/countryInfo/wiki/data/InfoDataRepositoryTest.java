package io.countryInfo.wiki.data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

import io.countryInfo.wiki.data.remote.InfoApi;
import io.countryInfo.wiki.model.CountryInfo;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class InfoDataRepositoryTest {

    private MockWebServer mockWebServer;
    private InfoApi infoApi;

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
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        infoApi = retrofit.create(InfoApi.class);

        InfoDataRepository.getInstance();

    }

    @Test
    public void testGetCountryInfoFromCloud() throws IOException {
        MockResponse mockResponse = new MockResponse().setResponseCode(200)
                .setBody(sampleJson);
        mockWebServer.enqueue(mockResponse);
        Response<CountryInfo> response = infoApi.getCountryInfo().execute();
        assertEquals(response.code(),200);
        assert response.body() != null;
        assertEquals(response.body() != null ? response.body().getTitle() : null,"About Canada");
        assert response.body() != null;
        assertEquals(response.body() != null ? response.body().getRows().get(0).getTitle() : null, "Beavers");
    }



    @After
    public void tearDown() throws IOException {
        // We're done with tests, shut it down
        mockWebServer.shutdown();
    }
}
