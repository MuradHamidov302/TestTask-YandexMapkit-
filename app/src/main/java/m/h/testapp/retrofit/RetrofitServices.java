package m.h.testapp.retrofit;


import m.h.testapp.booklist.response.SuccesProfile;
import m.h.testapp.login.ResponseLogin;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;


public interface RetrofitServices {

//login--------------BASE_URL-API_LOGIN--http://kuzya.saffman.co.uk--------------------------------------
    @FormUrlEncoded
    @POST("/account/api/code/")
    Call<ResponseBody> phonelogin(@Field("phone_number") String phone);

    @FormUrlEncoded
    @POST("/account/api/mobile-login/")
    Call<ResponseLogin> phoneconfirme(@Field("phone_number") String phone , @Field("confirm_code") String confrim);

    @FormUrlEncoded
    @POST("/social-auth/api/social-login/")
    Call<ResponseBody> facebooklogin(@Field("access_token") String token,
                                   @Field("provider") String provider);

//booklist--------------BASE_URL-API_BOOKLIST--http://api.audiokitab.com--------------------------------------

    @GET
    Call<SuccesProfile> booklist(@Url String url);


}
