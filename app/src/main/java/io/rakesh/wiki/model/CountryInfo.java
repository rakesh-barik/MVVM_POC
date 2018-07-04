
package io.rakesh.wiki.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
@Entity(tableName = "country_info")
public class CountryInfo {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    int countryId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("rows")
    @Expose
    @TypeConverters(InfoTypeConverter.class)
    private List<Row> rows = null;

    @NonNull
    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(@NonNull int countryId) {
        this.countryId = countryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

}
