package m.h.testapp.retrofit;


import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface RetrofitServices {

    @FormUrlEncoded
    @POST("/account/api/code/")
    Call<JSONObject> phonelogin(@Field("phone_number") String phone);

    @FormUrlEncoded
    @POST("/social-auth/api/social-login/")
    Call<JSONObject> facebooklogin(@Field("access_token") String token,
                                   @Field("provider") String provider);


}
