package io.countryInfo.wiki.data.cache;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import io.countryInfo.wiki.model.CountryInfo;
import io.countryInfo.wiki.model.Row;


@Database(entities = {CountryInfo.class,Row.class}, version = 1, exportSchema = false)
public abstract class InfoDb extends RoomDatabase {
    public abstract InfoDAO infoDAO();

    private static InfoDb INSTANCE;

    public static InfoDb getDatabase(final Context context) {
        if(INSTANCE == null){
            synchronized ((InfoDb.class)){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            InfoDb.class,
                            "info_database").addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                }
            };

}
