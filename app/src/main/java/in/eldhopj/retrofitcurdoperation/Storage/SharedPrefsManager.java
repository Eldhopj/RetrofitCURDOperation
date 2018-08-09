package in.eldhopj.retrofitcurdoperation.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import in.eldhopj.retrofitcurdoperation.ModelClass.User;

/**SharedPrefsManager is to save an retrieve data from shared prefs*/

public class SharedPrefsManager {

    private static SharedPrefsManager mInstance;
    public static final String SHARED_PREF_NAME = "my_shared_prefs";
    private Context mCtx;

    public SharedPrefsManager(Context mCtx) { // constructor
        this.mCtx = mCtx;
    }

    // synchronised because we only wants a single instance, and return an instance of this class
    public static synchronized SharedPrefsManager getInstance(Context mCtx) {
        if (mInstance == null) { // Object is not yet created
            mInstance = new SharedPrefsManager(mCtx); // instance created
        }
        return mInstance;
    }

    /**
     * saving user info into shared prefs
     */
    public void saveUser(User user) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("id", user.getId()); // in real don't save ID
        editor.putString("email", user.getEmail());
        editor.putString("name", user.getName());
        editor.putString("school", user.getSchool());

        editor.apply();
    }

    /**
     * Checking whether the user is logged in or not
     */
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("email", null) != null; // if the user is logged in the email wont be null
    }

    /**
     * Getting the user info from shared prefs
     */
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        User user = new User(
                sharedPreferences.getInt("id", -1),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("name", null),
                sharedPreferences.getString("school", null)
        );
        return user;
    }

    /**Clearing users prefs when logged out*/
    public void clear() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

    }
}
