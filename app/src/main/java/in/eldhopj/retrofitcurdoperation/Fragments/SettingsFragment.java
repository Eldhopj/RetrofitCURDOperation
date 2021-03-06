package in.eldhopj.retrofitcurdoperation.Fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import in.eldhopj.retrofitcurdoperation.LoginActivity;
import in.eldhopj.retrofitcurdoperation.ModelClass.DefaultResponse;
import in.eldhopj.retrofitcurdoperation.ModelClass.LoginResponse;
import in.eldhopj.retrofitcurdoperation.ModelClass.User;
import in.eldhopj.retrofitcurdoperation.Networks.NetworkUtility;
import in.eldhopj.retrofitcurdoperation.Networks.RetrofitClient;
import in.eldhopj.retrofitcurdoperation.R;
import in.eldhopj.retrofitcurdoperation.Storage.SharedPrefsManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingsFragment extends Fragment implements View.OnClickListener {
    private EditText editTextEmail, editTextName, editTextSchool;
    private EditText editTextCurrentPassword, editTextNewPassword;
    private static final String TAG = "SettingsFragment";

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextName = view.findViewById(R.id.editTextName);
        editTextSchool = view.findViewById(R.id.editTextSchool);
        editTextCurrentPassword = view.findViewById(R.id.editTextCurrentPassword);
        editTextNewPassword = view.findViewById(R.id.editTextNewPassword);

        view.findViewById(R.id.buttonSave).setOnClickListener(this);
        view.findViewById(R.id.buttonChangePassword).setOnClickListener(this);
        view.findViewById(R.id.buttonLogout).setOnClickListener(this);
        view.findViewById(R.id.buttonDelete).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonSave:
                updateProfile(); // Update user profile
                break;
            case R.id.buttonChangePassword:
                updatePassword(); // Update password
                break;
            case R.id.buttonLogout:
                logoutUser(); //Logout user
                break;
            case R.id.buttonDelete:
                deleteUser(); // Delete user
                break;
        }
    }


    private void updateProfile() {
        String email = editTextEmail.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String school = editTextSchool.getText().toString().trim();

        //Validations starts here
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (name.isEmpty()) {
            editTextName.setError("Name required");
            editTextName.requestFocus();
            return;
        }

        if (school.isEmpty()) {
            editTextSchool.setError("School required");
            editTextSchool.requestFocus();
            return;
        }
        //Validations ends here

        /**Update user profile using PUT request*/

        User user = SharedPrefsManager.getInstance(getActivity()).getUser();

        Call<LoginResponse> call = RetrofitClient.getInstance().getApi()
                .updateUser //passing the values into server
                        (user.getId(), // Getting the user ID from the shared prefs , ID is permanent we cant edit ID, NOTE : in real world we wont save or pass ID due to security reasons.
                        email,
                        name,
                        school);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (!response.isSuccessful()) { // Prevents error like 404
                    Log.d(TAG, "onResponse: "+ response.code());

                    //NOTE : check response codes for better message  :- https://www.restapitutorial.com/httpstatuscodes.html
                    if(response.code() == 504) { // 504 -  Gateway Timeout
                        Toast.makeText(getContext(), "Can't load data.\\nCheck your network connection.", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                if (!(response.body().getError())){ // If there is no error
                    // Get new user response from server and save it into shared prefs
                    SharedPrefsManager.getInstance(getActivity()).saveUser(response.body().getUser());
                }
                Log.d(TAG, "onResponse: "+ response.body().getMessage());
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getCause());
                if (!call.isCanceled()){
                    if(NetworkUtility.isKnownException(t)){
                        Toast.makeText(getContext(), "Can't load data.\nCheck your network connection.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void updatePassword(){
        String currentPassword = editTextCurrentPassword.getText().toString().trim();
        String newPassword = editTextNewPassword.getText().toString().trim();

        if (currentPassword.isEmpty()) {
            editTextCurrentPassword.setError("Password required");
            editTextCurrentPassword.requestFocus();
            return;
        }

        if (newPassword.isEmpty()) {
            editTextNewPassword.setError("Enter new password");
            editTextNewPassword.requestFocus();
            return;
        }

        /**Update the current password using PUT request*/

        User user = SharedPrefsManager.getInstance(getActivity()).getUser(); // for to get the email ID

        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().
                updatePassword(currentPassword,
                        newPassword,
                        user.getEmail()); // Get user email from the shared prefs

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (!response.isSuccessful()) { // Prevents error like 404
                    Log.d(TAG, "onResponse: "+ response.code());

                    //NOTE : check response codes for better message  :- https://www.restapitutorial.com/httpstatuscodes.html
                    if(response.code() == 504) { // 504 -  Gateway Timeout
                        Toast.makeText(getContext(), "Can't load data.\\nCheck your network connection.", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }

                DefaultResponse defaultResponse = response.body();
                Log.d(TAG, "onResponse: "+defaultResponse.getMessage());
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getCause());
                if (!call.isCanceled()){
                    if(NetworkUtility.isKnownException(t)){
                        Toast.makeText(getContext(), "Can't load data.\nCheck your network connection.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**User Logout*/
    private void logoutUser() {
        SharedPrefsManager.getInstance(getActivity()).clear();
        Intent intent = new Intent(getActivity(),LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);//We need to close all the existing activity because we don't want our user to navigate back on backButton press
        startActivity(intent);
    }

    private void deleteUser(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Confirm Delete...");
        alertDialog.setMessage("Are you sure you want delete this?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                /**Delete user*/

                User user = SharedPrefsManager.getInstance(getActivity()).getUser(); // for to get the email ID
                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi()
                        .deleteUser(user.getId()); //gets ID from the shared prefs
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        if (!response.isSuccessful()) { // Prevents error like 404
                            Log.d(TAG, "onResponse: "+ response.code());

                            //NOTE : check response codes for better message  :- https://www.restapitutorial.com/httpstatuscodes.html
                            if(response.code() == 504) { // 504 -  Gateway Timeout
                                Toast.makeText(getContext(), "Can't load data.\\nCheck your network connection.", Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                        DefaultResponse defaultResponse = response.body();
                        if (!defaultResponse.isError()) { // If flag is false , ie delete successful
                            logoutUser(); // delete the data's in shared prefs
                        }
                        Log.d(TAG, "onResponse: "+defaultResponse.getMessage());
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: "+t.getCause());
                        if (!call.isCanceled()){
                            if(NetworkUtility.isKnownException(t)){
                                Toast.makeText(getContext(), "Can't load data.\nCheck your network connection.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
