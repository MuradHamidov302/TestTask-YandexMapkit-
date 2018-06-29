package m.h.testapp.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BASE_URL {


    //login--------------BASE_URL-API_LOGIN--http://kuzya.saffman.co.uk--------------------------------------

    public static String API_LOGIN = "http://kuzya.saffman.co.uk";

   public static Retrofit retrofitLogin = new Retrofit.Builder()
            .baseUrl(BASE_URL.API_LOGIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
   public static RetrofitServices retrofitServicesLogin = retrofitLogin.create(RetrofitServices.class);


    //booklist--------------BASE_URL-API_BOOKLIST--http://api.audiokitab.com--------------------------------------


    public static String API_BOOKLIST = "http://api.audiokitab.com";

    public static Retrofit retrofitBooklist = new Retrofit.Builder()
            .baseUrl(BASE_URL.API_BOOKLIST)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static RetrofitServices retrofitServicesBooklist = retrofitBooklist.create(RetrofitServices.class);

}
