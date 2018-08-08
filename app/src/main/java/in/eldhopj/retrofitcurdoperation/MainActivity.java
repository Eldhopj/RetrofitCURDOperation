package in.eldhopj.retrofitcurdoperation;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import in.eldhopj.retrofitcurdoperation.ModelClass.DefaultResponse;
import in.eldhopj.retrofitcurdoperation.Networks.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Commit 1:Sign Up using Retrofit POST Request
 *          Create an API interface (Api)
 *          Create a Singleton class for defining the retrofit client(RetrofitClient)
 *          User create operation
 *              Parse the Json response with the help of a Model Class (GSON method)
 *              Parse the error Json response in traditional way
 *
 * Commit 2: User Login
 *          Create POST request for login (Api)
 *          Store the user details in memory using shared prefs (SharedPrefsManager) <New/>
 *          Create Model class for parsing the Login response as well as for shared prefs (LoginResponse)
 *          If user is already logged in autoLogin into profile activity
 *
 * Commit 3: Verify user is logged or not, if not send user back to login activity (ProfileActivity)
 *          Bottom Navigation
 *          On HomeFragment display the current users details
 *
 * Commit 4: Get all users using GET request
 *          Displaying all users in using RecyclerView in PeopleFragment
 *          */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextInputLayout editTextEmail, editTextPassword, editTextName, editTextSchool;
    String name,emailID,password,school;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextName = findViewById(R.id.editTextName);
        editTextSchool = findViewById(R.id.editTextSchool);

        editTextName.getEditText().addTextChangedListener(watch);
        editTextSchool.getEditText().addTextChangedListener(watch);
    }

    private final TextWatcher watch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.d("beforeTextChanged:", "");
// removes the error message when starts typing
            editTextEmail.setError("");
            editTextName.setError("");
            editTextPassword.setError("");
            editTextSchool.setError("");

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };



    private boolean validateName() {
        name = editTextName.getEditText().getText().toString().trim();
        if (name.isEmpty()) {
            editTextName.setError("Field can't be empty");
            return false;
        } else {
            editTextName.setError(null);
            return true;
        }
    }
    private boolean validateEmail() {
        emailID = editTextEmail.getEditText().getText().toString().trim();
        if (!(Patterns.EMAIL_ADDRESS.matcher(emailID).matches())) {
            editTextEmail.setError("Enter a valid Email Address");
            return false;
        } else {
            editTextEmail.setError(null);
            return true;
        }
    }
    private boolean validatePassword(){
        password = editTextPassword.getEditText().getText().toString().trim();
        if (password.isEmpty()){
            editTextPassword.setError("Field Cant be Empty");
            return false;
        }
        else if (password.length() < 6){
            editTextPassword.setError("Minimum 6 characters needed");
            return false;
        }
        else {
            editTextPassword.setError(null);
            return true;
        }
    }
    private boolean validateSchool() {
        school = editTextSchool.getEditText().getText().toString().trim();
        if (name.isEmpty()) {
            editTextSchool.setError("Field can't be empty");
            return false;
        } else {
            editTextSchool.setError(null);
            return true;
        }
    }
    public void register(View view) {
        if (!(validateEmail() & validateName() & validatePassword() & validateSchool())){
            return;
        }
        else {
            registerUser();
        }
    }

    public void login(View view) {
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }


    /**Retrofit create operation*/

    void registerUser(){
        Call<DefaultResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .CreateUser(emailID,password,name,school);

        // To execute the http call we need to call the method enqueue
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if(response.body() !=null) {
                    if (response.code() == 201) { //201 is defined in the API code for successive creation of user
                        /**Parsing JSON in GSON way*/
                        DefaultResponse defaultResponse = response.body();// The response come here as JSON
                        Log.d(TAG, "onSuccessResponse: " + defaultResponse.getMessage());
                    }
                }
                else {
                    try {
                        if (response.errorBody() != null) {
                            /**Parsing JSON in Traditional way*/
                            String error = response.errorBody().string(); // Storing JSON response into an string
                            JSONObject jsonObject = new JSONObject(error); // Json Object
                            Log.d(TAG, "onFailureResponse: " + jsonObject.getString("message")); //"message" -> is the key in JSON
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });
    }

}
