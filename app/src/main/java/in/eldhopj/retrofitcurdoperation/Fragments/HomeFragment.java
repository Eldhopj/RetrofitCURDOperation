package in.eldhopj.retrofitcurdoperation.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.eldhopj.retrofitcurdoperation.R;
import in.eldhopj.retrofitcurdoperation.Storage.SharedPrefsManager;


public class HomeFragment extends Fragment {

TextView emailTV,nameTV,schoolTV;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailTV = view.findViewById(R.id.emailTV);
        nameTV = view.findViewById(R.id.nameTV);
        schoolTV =view.findViewById(R.id.schoolTV);

        emailTV.setText(SharedPrefsManager.getInstance(getActivity()).getUser().getEmail());
        nameTV.setText(SharedPrefsManager.getInstance(getActivity()).getUser().getName());
        schoolTV.setText(SharedPrefsManager.getInstance(getActivity()).getUser().getSchool());

    }

}
