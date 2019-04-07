package in.eldhopj.retrofitcurdoperation.Networks;

import in.eldhopj.retrofitcurdoperation.ModelClass.DefaultResponse;
import in.eldhopj.retrofitcurdoperation.ModelClass.LoginResponse;
import in.eldhopj.retrofitcurdoperation.ModelClass.UsersResponse;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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

    /**Dynamic URL*/
    @FormUrlEncoded
    @PUT("updateuser/{id}") // Give the user ID we wanna update, {} -> Dynamic url
    Call<LoginResponse> updateUser( // We are using login response because both's fields are same
            @Path("id") int id, //@Path("id") -> the path name must be same as the path name in the dynamic url
            @Field("email") String email,
            @Field("name") String name,
            @Field("school") String school
    );

    @FormUrlEncoded
    @PUT("updatepassword")
    Call<DefaultResponse> updatePassword(
            @Field("currentpassword") String currentPassword,
            @Field("newpassword") String newPassword,
            @Field("email") String email
    );

    @DELETE("deleteuser/{id}")
    Call<DefaultResponse> deleteUser(
            @Path("id") int id
    );

    /**More on URL manipulation @See <https://www.youtube.com/watch?v=TyJEDhauUeQ/>*/

    /**Query*/
   /*end point /posts?userId=1&_sort=id
   * This is an query so we have to use @Query annotation
   *
   * eg : @GET("posts")
   *      Call<sample> getPosts(
   *                            @Query("userId") int userID
   *                            @Query("_sort") int id) // Here retrofit will automatically put ? mark and the userId at the end of URL
   *
   * NOTE: if we don't want to pass a value we can put null,
   * Pro Tip : donn't use primitive variable use Integer instead of int for passing null value
    * */

   /**Array of params*/
   /*We can put query as 1.List, 2.vargs, 3.Array , retrofit will automatically handles and put end points.

   * eg : @Query("userId") Integer[] userId
   *
   *Note : if we are passing vargs it must declare in last */

    /**Query Map / FieldMap*/
    /* @QueryMap is used when we want to pass which queries we are using in run time
    NOTE: Similar to QueryMap there is FieldMap for post requests

    Eg : @GET("posts")
            Call<List<Post>> getPosts(@QueryMap Map<String, String> parameters);

    In activity declare an HashMap and pass the key value pairs, Key will the query url
    Map<String, String> parameters = new HashMap<>();
        parameters.put("userId", "1");
        parameters.put("_sort", "id");
        parameters.put("_order", "desc");
    Pass the parameter into the call function*/

    /**Passing Url*/
//    @GET //NOTE : nothing in GET method
//    Call<List<Comments>> getComments(@Url String url);
//
//    In activity pass the url as param
}
