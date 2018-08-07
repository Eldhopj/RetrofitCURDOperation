package in.eldhopj.retrofitcurdoperation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import in.eldhopj.retrofitcurdoperation.Fragments.HomeFragment;
import in.eldhopj.retrofitcurdoperation.Fragments.PeopleFragment;
import in.eldhopj.retrofitcurdoperation.Fragments.SettingsFragment;
import in.eldhopj.retrofitcurdoperation.Storage.SharedPrefsManager;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottomNavigationView = findViewById(R.id.bottomNavigation);//reference to the bottom nav view
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);//Pass the onClick_bottom navi listener to out bottom navigation
        displayFragment(new HomeFragment()); // To show when activity is started
    }

    //For onClick_bottom navi listener
    private BottomNavigationView.OnNavigationItemSelectedListener navListener= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) { //item -> gives which item is selected
            Fragment selectedFragment = null;

            switch (item.getItemId()){
                case R.id.homeNav:
                    selectedFragment=new HomeFragment(); // create home fragment
                    break;
                case R.id.peopleNav:
                    selectedFragment=new PeopleFragment();
                    break;
                case R.id.settingsNav:
                    selectedFragment=new SettingsFragment();
                    break;
            }
            if (selectedFragment != null){
                displayFragment(selectedFragment);
            }
            return true; //true -> show the selected item
        }
    };

    private void displayFragment(Fragment fragment){
        //To show the fragments
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,fragment)
                .commit();
    }
            //Bottom Navigation ends here

    /**Verify user is logged or not, if not send user back to login activity*/
    @Override
    protected void onStart() {
        super.onStart();
        if (!(SharedPrefsManager.getInstance(getApplicationContext()).isLoggedIn())) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);//We need to close all the existing activity because we don't want our user to navigate back on backButton press
            startActivity(intent);
        }
    }


}
