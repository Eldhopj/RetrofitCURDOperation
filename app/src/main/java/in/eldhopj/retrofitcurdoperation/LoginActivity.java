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
import android.widget.Toast;

import in.eldhopj.retrofitcurdoperation.ModelClass.LoginResponse;
import in.eldhopj.retrofitcurdoperation.Networks.RetrofitClient;
import in.eldhopj.retrofitcurdoperation.Storage.SharedPrefsManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private TextInputLayout editTextEmail, editTextPassword;
    String emailID,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        editTextEmail.getEditText().addTextChangedListener(watch);
        editTextPassword.getEditText().addTextChangedListener(watch);
    }

    private final TextWatcher watch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.d("beforeTextChanged:", "");
        // removes the error message when starts typing
            editTextEmail.setError("");
            editTextPassword.setError("");
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

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
        else {
            editTextPassword.setError(null);
            return true;
        }
    }

    public void register(View view) {
        Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        if (!(validateEmail() & validatePassword() )){
            return;
        }
        else {
            loginUser();
        }
    }

    /**Retrofit Login operation*/
    private void loginUser() {
        Call<LoginResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .userLogin(emailID,password);

        // To execute the http call we need to call the method enqueue
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body() != null){
                    LoginResponse loginResponse = response.body();
                    if (!loginResponse.getError()){ // if error flag is false proceed else display the message
                        //login logic

                        // Save the user details into shared prefs when login
                        SharedPrefsManager.getInstance(LoginActivity.this)
                                .saveUser(loginResponse.getUser());

                        profileActivityIntent();

                        Log.d(TAG, "onResponse: "+loginResponse.getMessage());
                    }else {
                        Log.d(TAG, "onResponse: "+loginResponse.getMessage());
                    }
                    Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }

    /**If user is already logged in skip login screen*/
    @Override
    protected void onStart() {
        super.onStart();
        if (SharedPrefsManager.getInstance(getApplicationContext()).isLoggedIn()){
            profileActivityIntent();
        }
    }

    void profileActivityIntent(){
        Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);//We need to close all the existing activity because we don't want our user to navigate back on backButton press
        startActivity(intent);
    }
}
