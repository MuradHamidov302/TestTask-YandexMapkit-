package m.h.testapp.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseLogin {
    @SerializedName("token")
    @Expose
    private String token;

    public String getToken() {
        return token;
    }

}
