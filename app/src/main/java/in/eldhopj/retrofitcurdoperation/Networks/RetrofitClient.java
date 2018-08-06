package in.eldhopj.retrofitcurdoperation.Networks;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://192.168.43.243:81/MyApi/public/";
    private static RetrofitClient mInstance; //Singleton instance
    private Retrofit retrofit; // retrofit object

    //Inside this constructor we initialize the retrofit object
    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    // synchronized method to get the Singleton instance of RetrofitClient class
    // synchronized because we need to get single instance only
    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();// This will return the instance of RetrofitClient class
        }
        return mInstance;
    }

    //Method to get the API
    public Api getApi() {
        return retrofit.create(Api.class);


    }
}
