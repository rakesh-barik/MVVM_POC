package io.rakesh.wiki.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;

import io.rakesh.wiki.data.cache.InfoDAO;
import io.rakesh.wiki.data.cache.InfoDb;
import io.rakesh.wiki.data.remote.InfoApi;
import io.rakesh.wiki.data.remote.InfoApiClient;
import io.rakesh.wiki.model.CountryInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InfoDataRepository {
    private static InfoDataRepository instance = null;
    private InfoApi infoApi;
    private InfoDAO infoDAO;
    private LiveData<CountryInfo> infoLiveData;


    private InfoDataRepository() {
        initAPI();
    }

    public void setApplication(Application application) {
        initDatabase(application);
    }

    public static InfoDataRepository getInstance() {
        if (instance == null) {
            synchronized (InfoDataRepository.class) {
                if (instance == null) {
                    instance = new InfoDataRepository();
                }
            }
        }
        return instance;
    }



    private void initAPI() {
        Retrofit retrofit = InfoApiClient.getRetrofitClient();
        infoApi = retrofit.create(InfoApi.class);
    }

    private void initDatabase(Application application) {
        InfoDb db = InfoDb.getDatabase(application);
        infoDAO = db.infoDAO();
        infoLiveData = infoDAO.getCountryInfo();
    }

    public LiveData<CountryInfo> getCountryInfoFromDb() {
        return infoLiveData;
    }

    public void getCountryInfoFromCloud() {
        Call<CountryInfo> call = infoApi.getCountryInfo();
        call.enqueue(new Callback<CountryInfo>() {
            @Override
            public void onResponse(Call<CountryInfo> call, Response<CountryInfo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CountryInfo countryInfo = response.body();
                    EventBus.getDefault().post(new MessageEvent("FETCHED DATA FROM CLOUD"));
                    insert(countryInfo);
                }
            }

            @Override
            public void onFailure(Call<CountryInfo> call, Throwable t) {
                //We can still granularize this error messages.
                EventBus.getDefault().post(new MessageEvent("NETWORK ERROR"));
            }
        });
    }

    public void insert(CountryInfo info) {
        new insertAsyncTask(infoDAO).execute(info);
    }

    private static class insertAsyncTask extends AsyncTask<CountryInfo, Void, Void> {

        private InfoDAO mAsyncTaskDao;

        insertAsyncTask(InfoDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CountryInfo... params) {
            mAsyncTaskDao.deleteAll();
            mAsyncTaskDao.insert(params[0]);
            EventBus.getDefault().post(new MessageEvent("DATA INSERTED TO DB"));
            return null;
        }
    }

}
