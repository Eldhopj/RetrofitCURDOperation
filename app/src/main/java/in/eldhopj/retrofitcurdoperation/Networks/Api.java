package in.eldhopj.retrofitcurdoperation.Networks;

import in.eldhopj.retrofitcurdoperation.ModelClass.DefaultResponse;
import in.eldhopj.retrofitcurdoperation.ModelClass.LoginResponse;
import in.eldhopj.retrofitcurdoperation.ModelClass.UsersResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("createuser") //createuser -> Endpoint
    /*<DefaultResponse> -> This call will return us an object of DefaultResponse
    Note : If we don't know what will be the response we will get use Call<ResponseBody>*/
    Call<DefaultResponse> CreateUser(
            @Field("email") String emailId, //the param of the @Field() must be same as in the API
            @Field("password") String password,
            @Field("name") String name,
            @Field("school") String school
    );

    @FormUrlEncoded
    @POST("userlogin")
    Call<LoginResponse> userLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("allusers")
    Call<UsersResponse> getUsers();
}
