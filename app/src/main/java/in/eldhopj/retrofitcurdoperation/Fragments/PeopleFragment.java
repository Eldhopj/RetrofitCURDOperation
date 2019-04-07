package in.eldhopj.retrofitcurdoperation.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.eldhopj.retrofitcurdoperation.Adapter;
import in.eldhopj.retrofitcurdoperation.ModelClass.User;
import in.eldhopj.retrofitcurdoperation.ModelClass.UsersResponse;
import in.eldhopj.retrofitcurdoperation.Networks.RetrofitClient;
import in.eldhopj.retrofitcurdoperation.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PeopleFragment extends Fragment{ //Implement the OnItemClickListener interface

    private static final String TAG = "PeopleFragment";
    ProgressDialog progressDialog;

    public PeopleFragment() {
        // Required empty public constructor
    }

    //Define the variables
    private RecyclerView mRecyclerView;
    private List<User> mUserList;
    private Adapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_people, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUserList = new ArrayList<>();//Inside this list item we get all our values
        initRecyclerView(view,mUserList);

        //Progress dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait..."); // set message
        progressDialog.show();// show progress dialog

        getPeopleList();
    }

    private void getPeopleList() {
        /**Fetching all users using @GET request*/
        Call<UsersResponse> call = RetrofitClient.getInstance().getApi().getUsers();
        call.enqueue(new Callback<UsersResponse>() {
            @Override
            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {

                if (!response.isSuccessful()) { // Prevents error like 404
                    Toast.makeText(getActivity(), "Code : " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                UsersResponse usersResponse = response.body();// The response come here as JSON

                if (!(usersResponse.getError()));// Error flag outside the ArrayList, to check if there is any error or not
                {
                    mUserList = usersResponse.getUsers();// All the data which gets from server is stored in the array list
                    progressDialog.dismiss(); //dismiss progress dialog
                    mAdapter.notifyDataSetChanged();//Notify  data change to mAdapter
                }

                //Handling Onclick
                mAdapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        User clickedItem = mUserList.get(position); // We get the item at the clicked position out of our list items
                        Log.d(TAG, "onItemClick: "+clickedItem.getEmail());
                    }
                });
            }

            @Override
            public void onFailure(Call<UsersResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                progressDialog.dismiss(); //dismiss progress dialog
            }
        });
    }

    private void initRecyclerView(View view,List<User> listItems){
        /**bind with xml*/
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true); // setting it to true allows some optimization to our view , avoiding validations when mRecyclerAdapter content changes

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false)); //it can be GridLayoutManager or StaggeredGridLayoutManager

        /**set the mRecyclerAdapter to the recycler view*/
        mAdapter = new Adapter(listItems, getActivity());
//      mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL)); // Divider decorations
        mRecyclerView.setAdapter(mAdapter);
    }
}
