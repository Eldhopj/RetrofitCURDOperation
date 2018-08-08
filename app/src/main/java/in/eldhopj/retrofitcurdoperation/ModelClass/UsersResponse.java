package in.eldhopj.retrofitcurdoperation.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UsersResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("users")
    @Expose
    private List<User> users = null;

    /**
     * No args constructor for use in serialization
     */
    public UsersResponse() {
    }

    /**
     * @param users
     * @param error
     */
    public UsersResponse(Boolean error, List<User> users) {
        super();
        this.error = error;
        this.users = users;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
