package io.rakesh.wiki.data.cache;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.rakesh.wiki.model.CountryInfo;
import io.rakesh.wiki.model.Row;


@Dao
public interface InfoDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CountryInfo countryInfo);

    @Query("SELECT * FROM country_info")
    LiveData<CountryInfo> getCountryInfo();

    @Query("DELETE FROM country_info")
    void deleteAll();

    @Query("SELECT * FROM `row` where countryId IS :countryId")
    LiveData<List<Row>> getRows(int countryId);

}
