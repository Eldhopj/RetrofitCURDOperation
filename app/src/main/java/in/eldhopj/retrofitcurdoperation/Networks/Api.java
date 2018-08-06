package in.eldhopj.retrofitcurdoperation.Networks;

import in.eldhopj.retrofitcurdoperation.ModelClass.DefaultResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    /**
     *
     * CreateUser() -> Function
     *  */
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
}
