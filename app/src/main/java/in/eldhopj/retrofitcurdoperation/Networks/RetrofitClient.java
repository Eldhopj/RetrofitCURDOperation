package in.eldhopj.retrofitcurdoperation.Networks;

import android.util.Base64;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static final String AUTH = "Basic "+ Base64.encodeToString(("belalkhan:123456").getBytes(),Base64.NO_WRAP);
//    private static final String BASE_URL = "http://192.168.43.243:81/MyApi/public/";
    private static final String BASE_URL = "http://simplifiedlabs.xyz/MyApi/public/";
    private static RetrofitClient mInstance; //Singleton instance
    private Retrofit retrofit; // retrofit object

    //Inside this constructor we initialize the retrofit object
    private RetrofitClient() {
        //HTTP inspector start here
        /*Create basic auth and send it via header
        * This header is added to all the request calls */
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {

                        Request original = chain.request(); // Create a original object

                        //Add our auth header in the  original
                        Request.Builder requestBuilder= original.newBuilder()
                                .addHeader("Authorization",AUTH)
                                .method(original.method(),original.body());//We will pass the original method and original body

                        Request request = requestBuilder.build();
                        return chain.proceed(request); // Pass the new request
                    }
                })
                .build(); //HTTP inspector ends here

       /**HttpLoggingInterceptor is used to log the data during the network call.*/
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        httpClient.addInterceptor(logging);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient) //Send the header with all the requests NOTE : use this if we using HTTP inspector
//                .client(httpClient.build())
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
