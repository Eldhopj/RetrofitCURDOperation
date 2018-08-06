package in.eldhopj.retrofitcurdoperation.ModelClass;

import com.google.gson.annotations.SerializedName;
/**Model classes are for parsing the JSON into java objects*/
public class DefaultResponse {
    @SerializedName("error") // Json Object Key Name
    private boolean error;

    @SerializedName("message")
    private String message;

    public DefaultResponse(boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
