package in.eldhopj.retrofitcurdoperation.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import in.eldhopj.retrofitcurdoperation.ModelClass.DefaultResponse;
import in.eldhopj.retrofitcurdoperation.ModelClass.LoginResponse;
import in.eldhopj.retrofitcurdoperation.ModelClass.User;
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
                break;
            case R.id.buttonDelete:
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
                if (!(response.body().getError())){ // If there is no error
                    // Get new user response from server and save it into shared prefs
                    SharedPrefsManager.getInstance(getActivity()).saveUser(response.body().getUser());
                }
                Log.d(TAG, "onResponse: "+ response.body().getMessage());
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
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
                Log.d(TAG, "onResponse: "+response.body().getMessage());
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }
}
