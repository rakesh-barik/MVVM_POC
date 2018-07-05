package io.rakesh.wiki.data.cache;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import io.rakesh.wiki.model.CountryInfo;
import io.rakesh.wiki.model.Row;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class InfoDAOTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private InfoDAO infoDAO;
    private InfoDb infoDb;



    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        infoDb = Room.inMemoryDatabaseBuilder(context, InfoDb.class)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build();
        infoDAO = infoDb.infoDAO();
    }

    @After
    public void closeDb() throws IOException {
        infoDb.close();
    }

    @Test
    public void insertAndGetCountryInfo() throws Exception {
        CountryInfo countryInfo = generateTestData();

        infoDAO.insert(countryInfo);
        List<CountryInfo> countryInfoList = Collections.singletonList(LiveDataTestUtil.getValue(infoDAO.getCountryInfo()));
        assertEquals(countryInfoList.get(0).getTitle(), countryInfo.getTitle());
    }



    @Test
    public void deleteAll() throws InterruptedException {
        CountryInfo countryInfo = generateTestData();
        infoDAO.insert(countryInfo);
        infoDAO.deleteAll();
        List<CountryInfo> countryInfoList = Collections.singletonList(LiveDataTestUtil.getValue(infoDAO.getCountryInfo()));
        assertEquals(countryInfoList.get(0),null);
    }

    @NonNull
    private CountryInfo generateTestData() {
        CountryInfo countryInfo = new CountryInfo();
        countryInfo.setTitle(getRandomString());
        Row row = new Row();
        row.setTitle(getRandomString());
        row.setDescription(getRandomString());
        row.setImageHref(getRandomString());
        List<Row> rows = new ArrayList<>();
        rows.add(row);
        countryInfo.setRows(rows);
        return countryInfo;
    }

    private String getRandomString(){
       return UUID.randomUUID().toString();
    }
}
