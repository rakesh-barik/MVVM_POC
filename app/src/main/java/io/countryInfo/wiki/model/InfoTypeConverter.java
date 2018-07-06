package io.countryInfo.wiki.model;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class InfoTypeConverter {

    private static Gson gson = new Gson();

    @TypeConverter
    public static String rowObjectListToString(List<Row> rowList){
        return gson.toJson(rowList);
    }

    @TypeConverter
    public static List<Row> stringToRowList(String data){
        if(data == null){
            return Collections.emptyList();
        }
        Type lisType = new TypeToken<List<Row>>() {}.getType();

        return gson.fromJson(data, lisType);
    }
}
